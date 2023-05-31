package com.single.user.application;

import com.single.common.jwt.config.JwtAuthenticationProvider;
import com.single.user.domain.member.SignInForm;
import com.single.user.domain.model.Member;
import com.single.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInApplication {
    private final MemberService memberService;
    private final JwtAuthenticationProvider jwtProvider;

    public String MemberLoginToken(SignInForm form) {
        Member member = memberService.findValidMember(form.getEmail(), form.getPassword());

        return member != null ? jwtProvider.createToken(form.getEmail()) : "존재하지 않는 회원입니다";
    }
}
