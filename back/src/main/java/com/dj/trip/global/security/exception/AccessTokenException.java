package com.dj.trip.global.security.exception;

import com.dj.trip.global.dto.ResponseDto;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException {

    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR {
        UNACCEPT(401,"토큰이 없거나 너무 짧습니다."),
        BADTYPE(401, "토큰 타입이 Bearer이 아닙니다."),
        MALFORM(403, "잘못된 형식의 토큰입니다"),
        BADSIGN(403, "잘못된 서명이 있는 토큰입니다"),
        EXPIRED(403, "만료된 토큰입니다");

        private int status;
        private String msg;

        TOKEN_ERROR(int status, String msg){
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {
            return this.status;
        }

        public String getMsg() {
            return this.msg;
        }
    }

    public AccessTokenException(TOKEN_ERROR error){
        super(error.name());
        this.token_error = error;
    }

    public void sendResponseError(HttpServletResponse response){

        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setStatus(token_error.getStatus());
        responseDto.setMsg(token_error.getMsg());
        responseDto.setData(null);

        String responseStr = gson.toJson(responseDto);

        try {
            response.getWriter().println(responseStr);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
