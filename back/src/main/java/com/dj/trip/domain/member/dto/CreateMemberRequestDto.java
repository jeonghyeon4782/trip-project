package com.dj.trip.domain.member.dto;

import com.dj.trip.domain.oauth.OauthServerType;
import jakarta.validation.constraints.Email;
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
public class CreateMemberRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 6, max = 12, message = "아이디는 6자 이상 12자 이하입니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 영어 소문자와 숫자로만 허용되며, 공백이 없어야 합니다.")
    private String memberId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@_!*$%#])[A-Za-z\\d@_!*$%#]{8,}$", message = "비밀번호는 영어 소문자, 대문자, 숫자, 특수 문자(@ _ ! * $ % #)를 모두 포함하여 8자 이상이어야 합니다.")
    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상 20자 이하입니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]*$", message = "닉네임은 영어 소문자, 대문자, 한글, 숫자로만 이루어져야 하며, 공백이 없어야 합니다.")
    @Size(min = 3, max = 10, message = "닉네임은 3자 이상 10자 이하입니다.")
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[^\\s]+$", message = "이메일에는 공백이 포함될 수 없습니다.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;

    private char role;

    private OauthServerType oauthServiceType;
}
