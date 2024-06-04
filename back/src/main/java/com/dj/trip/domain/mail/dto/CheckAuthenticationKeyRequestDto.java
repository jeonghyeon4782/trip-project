package com.dj.trip.domain.mail.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckAuthenticationKeyRequestDto {
    private String email;
    private String key;
}
