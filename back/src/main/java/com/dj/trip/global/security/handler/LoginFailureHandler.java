package com.dj.trip.global.security.handler;

import com.dj.trip.global.dto.ResponseDto;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("------------------------------------------LoginFailureHandler------------------------------------------");

        // 클라이언트에게 전달할 메시지 설정
        String message = "아이디 또는 비밀번호가 잘못되었습니다.";

        // ResponseDto 객체 생성하여 데이터 설정
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseDto.setMsg(message);
        responseDto.setData(null); // 데이터 없음

        // ResponseDto 객체를 JSON 형식으로 변환하여 클라이언트에게 반환
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseDto);
        response.getWriter().println(jsonResponse);
    }

}
