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
    public void registerMemberSuccess() {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(Member.from(form));

        Member member = signUpService.signUp(form);

        assertEquals(member.getName(), "test");
        assertEquals(member.getPhone(), "010-1111-1234");
        assertEquals(member.getEmail(), "test@naver.com");
    }

    @Test
    @DisplayName("중복된 계정으로 인한 실패")
    public void alreadyRegisterMember() {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(Member.from(form)));

        assertThrows(MemberException.class, () -> signUpService.signUp(form));
    }
}