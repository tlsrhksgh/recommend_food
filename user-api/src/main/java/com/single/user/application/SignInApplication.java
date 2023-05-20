package com.single.user.application;

import com.single.user.config.JwtAuthenticationProvider;
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
        Member member = memberService.findValidCustomer(form.getEmail(), form.getPassword());

        return jwtProvider.createToken(form.getEmail());
    }
}
