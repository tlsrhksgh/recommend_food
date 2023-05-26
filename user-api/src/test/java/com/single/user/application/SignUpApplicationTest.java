package com.single.user.application;

import com.single.user.client.mailgun.MailgunClient;
import com.single.user.client.mailgun.SendMailForm;
import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.exception.MemberException;
import com.single.user.service.SignUpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpApplicationTest {
    @Mock
    private MailgunClient mailgunClient;

    @Mock
    private SignUpService signUpService;

    @InjectMocks
    private SignUpApplication signUpApplication;

    @Captor
    private ArgumentCaptor<SendMailForm> sendMailFormCaptor;

    @Test
    @DisplayName("Service verifyEmail 메서드 호출 성공")
    void testMemberVerify_CallVerifyEmail() {
        // given
        String email = "test@example.com";
        String code = "1234";

        // when
        signUpApplication.memberVerify(email, code);

        // then
        verify(signUpService, times(1)).verifyEmail(email, code);
    }

    @Test
    @DisplayName("중복된 사용자 계정으로 인한 회원 가입 실패")
    void testMemberSignUp_alreadyMember_SignUp_Failed() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@test.com")
                .password("1234")
                .name("test")
                .phone("01012341234")
                .build();

        given(signUpService.isEmailExist(form.getEmail()))
                .willReturn(true);

        //when
        //then
        assertThrows(MemberException.class, () -> signUpApplication.memberSignUp(form));
        verify(mailgunClient, never()).sendEmail(any());
        verify(signUpService, never()).changeMemberValidateStatus(anyLong(), anyString());
        verify(signUpService, never()).signUp(form);
    }

    @Test
    @DisplayName("회원가입 성공")
    void testMemberSignUp_MemberRegister_Success() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@test.com")
                .password("1234")
                .name("test")
                .phone("01012341234")
                .build();

        Member member = Member.from(form);

        given(signUpService.isEmailExist(anyString()))
                .willReturn(false);
        given(signUpService.signUp(any()))
                .willReturn(member);

        //when
        String returnValue = signUpApplication.memberSignUp(form);


        //then
        assertEquals(returnValue, "회원 가입에 성공하였습니다.");
        verify(mailgunClient, times(1)).sendEmail(sendMailFormCaptor.capture());
        SendMailForm capturedMailForm = sendMailFormCaptor.getValue();
        assertEquals(capturedMailForm.getTo(), "test@test.com");
        assertEquals(form.getEmail(), capturedMailForm.getTo());
        assertEquals("회원가입 인증 관련 메일입니다.", capturedMailForm.getSubject());
        Assertions.assertTrue(capturedMailForm.getText().contains(form.getEmail()));
    }
}