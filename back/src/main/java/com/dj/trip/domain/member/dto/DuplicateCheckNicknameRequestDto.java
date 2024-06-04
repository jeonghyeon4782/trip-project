package com.dj.trip.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateCheckNicknameRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]*$", message = "닉네임은 영어 소문자, 대문자, 한글, 숫자로만 이루어져야 하며, 공백이 없어야 합니다.")
    @Size(min = 3, max = 10, message = "닉네임은 3자 이상 10자 이하입니다.")
    private String nickname;
}
