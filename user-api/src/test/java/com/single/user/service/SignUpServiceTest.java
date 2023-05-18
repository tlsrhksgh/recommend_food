package com.single.user.service;

import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.domain.repository.MemberRepository;
import com.single.user.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    @DisplayName("회원가입 성공")

    void registerMemberSuccess() {
        //given
    public void registerMemberSuccess() {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        given(memberRepository.save(any()))
                .willReturn(Member.from(form));

        Member member = signUpService.signUp(form);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(Member.from(form));

        Member member = signUpService.signUp(form);

        assertEquals(member.getName(), "test");
        assertEquals(member.getPhone(), "010-1111-1234");
        assertEquals(member.getEmail(), "test@naver.com");
    }

    @Test
    @DisplayName("중복된 계정 체크")
    void exist_EmailCheck() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(Member.from(form)));

        //when
        boolean result = signUpService.isEmailExist(form.getEmail());

        //then
        assertTrue(result);
        verify(memberRepository, times(1)).findByEmail(form.getEmail());
    }

    @Test
    @DisplayName("메일 체크 시 중복되는 메일 없음")
    void isEmailExist_withExistingEmail_shouldReturnTrue() {
        //given
    @DisplayName("중복된 계정으로 인한 실패")
    public void alreadyRegisterMember() {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        boolean result = signUpService.isEmailExist(form.getEmail());

        // Then
        assertFalse(result);
        verify(memberRepository, times(1)).findByEmail(form.getEmail());
    }

    @Test
    @DisplayName("가입된 사용자를 찾을 수 없음")
    void userNotFound() {
        //given
        String email = "test@test.com";
        String code = "1234";

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(MemberException.class, () -> signUpService.verifyEmail(email, code));
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("이미 인증된 사용자 이메일로 인한 인증 실패")
    void alreadyVerify_Member() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        String code = "1234";
        Member member = Member.from(form);
        member.setVerify(true);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        //when
        //then
        assertThrows(MemberException.class, () -> signUpService.verifyEmail(form.getEmail(), code));
        assertTrue(member.isVerify());
        verify(memberRepository, times(1)).findByEmail(form.getEmail());
    }

    @Test
    @DisplayName("인증 코드 불일치")
    void wrong_VerificationCode() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();
        String code = "1234";

        Member member = Member.from(form);
        member.setVerificationCode("5678");

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        //when
        //then
        assertThrows(MemberException.class, () -> signUpService.verifyEmail(form.getEmail(), code));
        assertFalse(member.isVerify());
        verify(memberRepository, times(1)).findByEmail(form.getEmail());
    }

    @Test
    @DisplayName("날짜가 만료된 코드 인증 실패")
    void expired_VerificationCode() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();
        String code = "1234";

        Member member = Member.from(form);
        member.setVerificationCode(code);
        member.setVerifyExpiredAt(LocalDateTime.now().minusDays(1));

        given(memberRepository.findByEmail(member.getEmail()))
                .willReturn(Optional.of(member));

        //when
        //then
        assertThrows(MemberException.class, () -> signUpService.verifyEmail(member.getEmail(), code));
        assertFalse(member.isVerify());
        verify(memberRepository, times(1)).findByEmail(member.getEmail());

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(Member.from(form)));

        assertThrows(MemberException.class, () -> signUpService.signUp(form));
    }
}