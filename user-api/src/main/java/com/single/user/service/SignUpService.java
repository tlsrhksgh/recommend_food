package com.single.user.service;

import com.single.user.domain.member.SignInForm;
import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.domain.repository.MemberRepository;
import com.single.user.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static com.single.user.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final MemberRepository memberRepository;

    public Member signUp(SignUpForm form) {
        return memberRepository.save(Member.from(form));
    }

    public boolean isEmailExist(String email) {
        return memberRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    public Member signIn(SignInForm form) {
        Optional<Member> member = memberRepository.findByEmail(form.getEmail().toLowerCase(Locale.ROOT));
        if (member.isPresent()) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }

        return null;
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        if(member.isVerify()) {
            throw new MemberException(ALREADY_VERIFY);
        } else if(!member.getVerificationCode().equals(code)) {
            throw new MemberException(WRONG_VERIFICATION_CODE);
        } else if(member.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new MemberException(EXPIRE_VERIFICATION_CODE);
        } else {
            member.setVerify(true);
        }
    }

    @Transactional
    public LocalDateTime changeMemberValidateStatus(Long memberId, String verificationCode) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if(optionalMember.isEmpty()) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }

        Member member = optionalMember.get();
        member.setVerificationCode(verificationCode);
        member.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

        return member.getVerifyExpiredAt();
    }
}
