package com.dj.trip.domain.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailVo {
    private String email;
    private String key;
    private String expirationDate;
}
