package com.dj.trip.domain.member.service;

import com.dj.trip.domain.image.service.ImageServiceUtils;
import com.dj.trip.domain.mail.MailVo;
import com.dj.trip.domain.mail.mapper.MailMapper;
import com.dj.trip.domain.mail.service.MailService;
import com.dj.trip.domain.member.Member;
import com.dj.trip.domain.member.MemberInfo;
import com.dj.trip.domain.member.dto.AuthenticationEmailResponseDto;
import com.dj.trip.domain.member.dto.CreateMemberRequestDto;
import com.dj.trip.domain.member.dto.FindPasswordRequestDto;
import com.dj.trip.domain.member.dto.request.ModifyMemberRequest;
import com.dj.trip.domain.member.dto.response.GetMemberByPasswordResponse;
import com.dj.trip.domain.member.dto.response.GetMemberResponse;
import com.dj.trip.domain.member.mapper.MemberMapper;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final MailMapper mailMapper;
    private final JWTUtil jwtUtil;
    private final ImageServiceUtils imageServiceUtils;

    // 회원가입
    @Override
    public void createMember(CreateMemberRequestDto dto) throws Exception {
        dto.setPassword(encoder.encode(dto.getPassword()));
        dto.setRole('U');
        memberMapper.insertMember(modelMapper.map(dto, Member.class));
    }

    @Override
    public boolean duplicateCheckMemberId(String memberId) throws Exception {
        Member member = memberMapper.selectMemberByMemberId(memberId);
        return member == null;
    }

    @Override
    public boolean duplicateCheckNickname(String nickname) throws Exception {
        Member member = memberMapper.selectMemberByNickname(nickname);
        return member == null;
    }

    @Override
    public AuthenticationEmailResponseDto authenticationEmail(String email) throws Exception {
        if (memberMapper.selectMemberByEmail(email) != null) {
            return null;
        }
        String key = mailService.generateRandomCode();
        mailService.sendMessage(email, "<두정> 이메일 인증 인증번호입니다.", "인증번호는 " + key + "입니다.");
        mailMapper.insertMail(new MailVo(email, key, null));
        return new AuthenticationEmailResponseDto(email, key);
    }

    @Override
    public void logout(HttpServletResponse response) {
        jwtUtil.setHeaderAccessTokenEmpty(response);
        jwtUtil.setHeaderRefreshTokenEmpty(response);
    }

    @Override
    public String findMemberId(String email) {
        if (memberMapper.selectMemberByEmail(email) == null) return null;
        return memberMapper.selectMemberByEmail(email).getMemberId();
    }

    @Override
    public boolean findPassword(FindPasswordRequestDto findPasswordRequestDto) throws Exception {
        Member findMember = memberMapper.selectMemberByMemberIdAndEmail(findPasswordRequestDto.getMemberId(), findPasswordRequestDto.getEmail());
        if (findMember == null) return false;
        CreateMemberRequestDto dto = modelMapper.map(findMember, CreateMemberRequestDto.class);
        String newPassword = generatePassword();
        dto.setPassword(encoder.encode(newPassword));

        // 이메일 전송
        mailService.sendMessage(findMember.getEmail(), "<두정> 새로운 비밀번호입니다.", "비밀번호는 " + newPassword + "입니다.");

        // 업데이트
        memberMapper.updateMember(modelMapper.map(dto, Member.class));

        return true;
    }

    @Override
    public GetMemberResponse getMember(String memberId) {
        MemberInfo memberInfo = memberMapper.selectMemberInfoByMemberId(memberId);
        String imageUrl = null;

        if (memberInfo != null) {
            if (memberInfo.getImageUrl() != null) {
                imageUrl = imageServiceUtils.getImageUrl(memberInfo.getImageUrl());
            }

            return new GetMemberResponse(
                    memberInfo.getMemberId(),
                    memberInfo.getNickname(),
                    memberInfo.getEmail(),
                    imageUrl,
                    memberInfo.getReviews(),
                    memberInfo.getScore()
            );
        }
        return null;
    }

    @Override
    public GetMemberByPasswordResponse getMemberByPassword(String memberId, String password) {
        MemberInfo memberInfo = memberMapper.selectMemberPswInfoByMemberId(memberId);

        if (memberInfo == null || !encoder.matches(password, memberInfo.getPassword())) {

            throw new InsufficientAuthenticationException("잘못된 요청");
        }

        String imageUrl = null;
        if (memberInfo.getImageUrl() != null) {
            imageUrl = imageServiceUtils.getImageUrl(memberInfo.getImageUrl());
        }

        return new GetMemberByPasswordResponse(
                memberInfo.getMemberId(),
                memberInfo.getNickname(),
                memberInfo.getEmail(),
                imageUrl,
                memberInfo.getPassword()
        );
    }

    @Override
    public void modifyMember(String memberId, ModifyMemberRequest modigyMemberRequest, MultipartFile file) {
        String fileName = null;
        if (file != null) {
            fileName = imageServiceUtils.upload(file);
        }

        String password = null;
        System.out.println("password= " + " " + modigyMemberRequest.password());
        if (!modigyMemberRequest.password().isEmpty()) {
            password = encoder.encode(modigyMemberRequest.password());
        }

        Member member = Member
                .modifyMember(
                        memberId,
                        modigyMemberRequest.nickname(),
                        modigyMemberRequest.email(),
                        password,
                        fileName
                );

        if (fileName != null) {
            // 해당 id의 이미지 이름으로 삭제 요청
            String image_url = memberMapper.getImageUrl(memberId);
            if (image_url != null) {
                imageServiceUtils.deleteImage(image_url);
            }
        }

        if (memberMapper.modifyMember(member) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }

    @Override
    public void deleteMember(String password, String memberId, HttpServletResponse response) {
        if (!encoder.matches(password, memberMapper.getPassword(memberId))) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
        jwtUtil.setHeaderAccessTokenEmpty(response);
        jwtUtil.setHeaderRefreshTokenEmpty(response);
        memberMapper.deleteMember(memberId);
    }

    public static String generatePassword() {
        int length = 10; // 비밀번호 길이 (10자 이상 20자 이하)
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!#@";
        String allChars = upperCaseLetters + lowerCaseLetters + numbers + specialChars;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        password.append(upperCaseLetters.charAt(secureRandom.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(secureRandom.nextInt(lowerCaseLetters.length())));
        password.append(numbers.charAt(secureRandom.nextInt(numbers.length())));
        password.append(specialChars.charAt(secureRandom.nextInt(specialChars.length())));
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(secureRandom.nextInt(allChars.length())));
        }
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = secureRandom.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }
        return new String(passwordArray);
    }
}
