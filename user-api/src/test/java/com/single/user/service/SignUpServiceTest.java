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
import java.util.Optional;

import static com.single.user.exception.ErrorCode.EXPIRE_VERIFICATION_CODE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    @DisplayName("회원가입 성공")
    public void testSignUp_registerMemberSuccess() {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        given(memberRepository.save(any()))
                .willReturn(Member.from(form));

        //when
        Member member = signUpService.signUp(form);

        assertEquals(member.getName(), "test");
        assertEquals(member.getPhone(), "010-1111-1234");
        assertEquals(member.getEmail(), "test@naver.com");
        verify(memberRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("중복된 계정 체크")
    void testIsEmailExist_exist_EmailCheck() {
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
    @DisplayName("중복된 계정으로 인한 실패")
    public void testVerifyEmail_alreadyRegisterMember() {
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
    void testVerifyEmail_userNotFound() {
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
    void testVerifyEmail_alreadyVerify_Member() {
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
    void testVerifyEmail_wrong_VerificationCode() {
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
    void testVerifyEmail_expired_VerificationCode() {
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
        Exception exception = assertThrows(MemberException.class, () -> {
            signUpService.verifyEmail(member.getEmail(), code);
        });

        //then
        assertEquals(EXPIRE_VERIFICATION_CODE.getContent(), exception.getMessage());
        assertFalse(member.isVerify());
        verify(memberRepository, times(1)).findByEmail(member.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 에러 발생")
    void testChangeMemberValidateStatus_NonExistMember() {
        //given
        given(memberRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(MemberException.class, () -> signUpService.changeMemberValidateStatus(1L, "1234"));
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("사용자 인증 메일 기한과 인증 코드 DB속성 변경")
    void testChangeMEmberValidateStatus_ChangeEmailValidDateAndValidCode() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        String code = "1234";
        LocalDateTime validExpiredAt = LocalDateTime.now().plusDays(1);

        Member member = Member.from(form);
        member.setId(1L);
        member.setVerificationCode(code);
        member.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        //when
        LocalDateTime date = signUpService.changeMemberValidateStatus(1L, code);

        //then
        assertEquals(date, member.getVerifyExpiredAt());
        assertEquals(code, member.getVerificationCode());
    }
}