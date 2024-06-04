package com.dj.trip.domain.mail.service;

import com.dj.trip.domain.mail.dto.CheckAuthenticationKeyRequestDto;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendMessage(String email, String subject, String text) throws MessagingException;
    String generateRandomCode();
    String resendAuthenticationKey(String email) throws MessagingException;
    void deleteAuthenticationKey(String email);
    int checkAuthenticationKey(CheckAuthenticationKeyRequestDto checkAuthenticationKeyRequestDto);
}
