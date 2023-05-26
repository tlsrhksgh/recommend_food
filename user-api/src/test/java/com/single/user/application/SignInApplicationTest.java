package com.single.user.application;

import com.single.user.config.JwtAuthenticationProvider;
import com.single.user.domain.member.SignInForm;
import com.single.user.domain.model.Member;
import com.single.user.exception.ErrorCode;
import com.single.user.exception.MemberException;
import com.single.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.single.user.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignInApplicationTest {
    @Mock
    private MemberService memberService;

    @Mock
    private JwtAuthenticationProvider jwtProvider;

    @InjectMocks
    private SignInApplication signInApplication;

    @Test
    @DisplayName("멤버 로그인 성공 후 토큰 발급")
    void testMemberLoginToken_memberLoginSuccess() {
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

    @Test
    @DisplayName("존재하지 않는 사용자로 토근 발급 실패")
    void testMemberLoginToken_memberLoginFailed() {
        //given
        String email = "test@test.com";
        String password = "1234";
        SignInForm form = new SignInForm(email, password);
        given(memberService.findValidMember(form.getEmail(), form.getPassword()))
                .willReturn(null);

        //when
        String result = signInApplication.MemberLoginToken(form);

        //then
        assertEquals(result, "존재하지 않는 회원입니다");
    }
}