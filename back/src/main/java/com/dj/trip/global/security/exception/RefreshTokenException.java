package com.dj.trip.global.security.exception;

import com.dj.trip.global.dto.ResponseDto;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class RefreshTokenException extends RuntimeException {

    private ErrorCase errorCase;

    public enum ErrorCase {
        NO_ACCESS, NO_REFRESH, OLD_REFRESH
    }

    public RefreshTokenException(ErrorCase errorCase) {
        super(errorCase.name());
        this.errorCase = errorCase;
    }

    public void sendResponseError(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        ResponseDto<Object> responseDto;

        switch (errorCase) {
            case NO_ACCESS:
                responseDto = ResponseDto.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .msg("Access Token이 없습니다.")
                        .data(null)
                        .build();
                break;
            case NO_REFRESH:
                responseDto = ResponseDto.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .msg("Refresh Token이 없습니다.")
                        .data(null)
                        .build();
                break;
            case OLD_REFRESH:
                responseDto = ResponseDto.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .msg("Refresh Token이 만료되었습니다.")
                        .data(null)
                        .build();
                break;
            default:
                responseDto = ResponseDto.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .msg("인증 오류가 발생했습니다.")
                        .data(null)
                        .build();
                break;
        }

        // Gson을 사용하여 ResponseDto를 JSON 문자열로 변환
        String responseStr = gson.toJson(responseDto);

        try {
            response.getWriter().println(responseStr);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
