package com.single.user.service;

import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.domain.repository.MemberRepository;
import com.single.user.exception.ErrorCode;
import com.single.user.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.single.user.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입된 계정 조회 성공")
    void testFindByEmail_already_RegisterMember() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@test.com")
                .password("1234")
                .name("test")
                .phone("01012341234")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(Member.from(form)));

        //when
        Member member = memberRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        //then
        assertEquals(form.getEmail(), member.getEmail());
        verify(memberRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 메일로 인한 Exception 발생")
    void testFindValidMember_InvalidEmail() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("test@test.com")
                .password("1234")
                .name("test")
                .phone("01012341234")
                .build();

        given(memberRepository.findByEmail(form.getEmail()))
                .willReturn(Optional.empty());


        //when
        //then
        assertThrows(MemberException.class, () -> memberService.findValidMember(form.getEmail(), form.getPassword()));
        verify(memberRepository, times(1)).findByEmail(form.getEmail());
    }
}