package com.dj.trip.domain.oauth.controller;

import com.dj.trip.domain.oauth.OauthServerType;
import com.dj.trip.domain.oauth.dto.request.OauthLoginRequest;
import com.dj.trip.domain.oauth.dto.response.OauthLoginResponse;
import com.dj.trip.domain.oauth.service.OauthService;
import com.dj.trip.global.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/login/social")
    public ResponseDto<?> login(@RequestBody OauthLoginRequest oauthLoginRequest,
                                HttpServletResponse response) {
        return new ResponseDto<>(HttpStatus.OK.value(),
                "로그인 성공 ", oauthService.login(oauthLoginRequest, response));
    }

}
