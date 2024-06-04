package com.dj.trip.domain.mail.service;

import com.dj.trip.domain.mail.MailVo;
import com.dj.trip.domain.mail.dto.CheckAuthenticationKeyRequestDto;
import com.dj.trip.domain.mail.mapper.MailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MailServiceImpl implements MailService{

    private final MailMapper mailMapper;
    private final JavaMailSender emailSender;

    // 이메일 전송
    @Override
    public void sendMessage(String email, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("DuJeong");
        helper.setTo(email);
        helper.setSubject(subject);
        String verificationCode = generateRandomCode();
        helper.setText(text);
        emailSender.send(message);
    }

    // 인증코드 만들기
    @Override
    public String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    @Override
    public String resendAuthenticationKey(String email) throws MessagingException{
        mailMapper.deleteMailByEmail(email);
        String key = generateRandomCode();
        mailMapper.insertMail(new MailVo(email, key, null));
        sendMessage(email, "<두정> 이메일 인증 인증번호입니다.", "인증번호는 " + key + "입니다.");
        return key;
    }

    @Override
    public void deleteAuthenticationKey(String email) {
        mailMapper.deleteMailByEmail(email);
    }

    @Override
    public int checkAuthenticationKey(CheckAuthenticationKeyRequestDto checkAuthenticationKeyRequestDto) {
        MailVo mailvo = mailMapper.selectMailByEmail(checkAuthenticationKeyRequestDto.getEmail());
        LocalDateTime expirationDateTime = LocalDateTime.parse(mailvo.getExpirationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (currentDateTime.isAfter(expirationDateTime)) {
            return 2; // 만료된 인증번호인 경우
        }
        if (!Objects.equals(mailvo.getKey(), checkAuthenticationKeyRequestDto.getKey())) {
            return 1;   // 인증번호가 같지 않을 경우
        }
        mailMapper.deleteMailByEmail(checkAuthenticationKeyRequestDto.getEmail());
        return 0;   // 인증 완료인 경우
    }

}
