package com.dj.trip.global.security.handler;

import com.dj.trip.global.dto.ResponseDto;
import com.dj.trip.global.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("------------------------------------------LoginSuccessHandler------------------------------------------");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claims = Map.of("memberId", authentication.getName());
        // Access Token의 유효기간
        String accessToken = jwtUtil.getAccessToken(claims);
        log.info("------------------------------------------새로운 Access Token 생성-----------------------------------------");
        // Refresh Token의 유효기간
        String refreshToken = jwtUtil.getRefreshToken(claims);
        log.info("------------------------------------------새로운 Refresh Token 생성-----------------------------------------");


        // ResponseDto 객체 생성하여 데이터 설정
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();
        responseDto.setStatus(HttpServletResponse.SC_OK);
        responseDto.setMsg("로그인 성공");

        jwtUtil.setHeaderAccessToken(response, accessToken);
        jwtUtil.setHeaderRefreshToken(response, refreshToken);

        // ResponseDto 객체를 JSON 형식으로 변환하여 클라이언트에게 반환
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseDto);
        response.getWriter().println(jsonResponse);
    }

}
