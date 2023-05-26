package com.single.user.service;

import com.single.user.domain.model.Member;
import com.single.user.domain.repository.MemberRepository;
import com.single.user.exception.ErrorCode;
import com.single.user.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.single.user.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findValidMember(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if(!member.getEmail().equals(email)) {
            throw new MemberException(LOGIN_MAIL_CHECK_FAIL);
        } else if(!member.getPassword().equals(password)) {
            throw new MemberException(LOGIN_PASSWORD_CECK_FAIL);
        } else if(!member.isVerify()) {
            throw new MemberException(NOT_VERIFIED_EMAIL_CODE);
        }

        return member;
    }
}
