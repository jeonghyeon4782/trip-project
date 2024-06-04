package com.dj.trip.domain.mail.controller;

import com.dj.trip.domain.mail.dto.CheckAuthenticationKeyRequestDto;
import com.dj.trip.domain.mail.service.MailService;
import com.dj.trip.domain.member.dto.AuthenticationEmailRequestDto;
import com.dj.trip.global.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    // 인증번호 재발급
    @PostMapping("/resend-authentication-key")
    public ResponseEntity<ResponseDto<String>> resendAuthenticationKey(@Valid @RequestBody AuthenticationEmailRequestDto authenticationEmailRequestDto) throws Exception{
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(new ResponseDto<>(HttpStatus.OK.value(), "3분 이내에 인증번호를 입력해주세요.", mailService.resendAuthenticationKey(authenticationEmailRequestDto.getEmail())));
    }

    // 인증번호 재발급
    @PostMapping("/delete-authentication-key")
    public ResponseEntity<ResponseDto<String>> deleteAuthenticationKey(@Valid @RequestBody AuthenticationEmailRequestDto authenticationEmailRequestDto) throws Exception{
        mailService.deleteAuthenticationKey(authenticationEmailRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(new ResponseDto<>(HttpStatus.OK.value(), "인증번호 삭제 완료.", null));
    }

    // 인증번호 확인
    @PostMapping("/check-authentication-key")
    public ResponseEntity<ResponseDto<Integer>> checkAuthenticationKey(@RequestBody CheckAuthenticationKeyRequestDto checkAuthenticationKeyRequestDto) throws Exception{
        int result = mailService.checkAuthenticationKey(checkAuthenticationKeyRequestDto);
        System.out.println("result : " + result);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(new ResponseDto<>(HttpStatus.OK.value(), "이메일 인증이 완료되었습니다.", 0));
        } else if (result == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), "인증번호가 일치하지 않습니다.", 1));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), "만료된 인증번호 입니다.", 2));
    }
}
