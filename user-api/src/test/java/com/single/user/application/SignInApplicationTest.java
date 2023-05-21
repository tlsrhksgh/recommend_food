package com.single.user.application;

import com.single.user.config.JwtAuthenticationProvider;
import com.single.user.domain.member.SignInForm;
import com.single.user.domain.model.Member;
import com.single.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignInApplicationTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtAuthenticationProvider jwtProvider;

    @InjectMocks
    private SignInApplication signInApplication;

    @Test
    @DisplayName("멤버 로그인 성공 후 토큰 발급")
    void memberLoginSuccess() {
        //given
        String email = "test@test.com";
        String password = "1234";
        SignInForm form = new SignInForm(email, password);

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name("test")
                .phone("01012341234")
                .verify(true)
                .build();

        given(memberService.findValidMember(email, password))
                .willReturn(member);
        given(jwtProvider.createToken(anyString()))
                .willReturn("JWTTOKEN");

        //when
        String token = signInApplication.MemberLoginToken(form);

        assertEquals(token, "JWTTOKEN");
    }
}