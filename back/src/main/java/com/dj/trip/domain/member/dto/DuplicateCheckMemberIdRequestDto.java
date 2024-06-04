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
public class DuplicateCheckMemberIdRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 6, max = 12, message = "아이디는 6자 이상 12자 이하입니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 영어 소문자와 숫자로만 허용되며, 공백이 없어야 합니다.")
    private String memberId;
}
