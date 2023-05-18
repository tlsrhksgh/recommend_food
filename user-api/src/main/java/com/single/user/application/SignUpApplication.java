package com.single.user.application;

import com.single.user.client.mailgun.MailgunClient;
import com.single.user.client.mailgun.SendMailForm;
import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.exception.ErrorCode;
import com.single.user.exception.MemberException;
import com.single.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpService signUpService;

    public void memberVerify(String email, String code) {
        signUpService.verifyEmail(email, code);
    }

    public String memberSignUp(SignUpForm form) {
        if(signUpService.isEmailExist(form.getEmail())) {
            throw new MemberException(ErrorCode.ALREADY_REGISTER_MEMBER);
        } else {
            Member member = signUpService.signUp(form);

            String verifyCode = this.getRandomCode();

            SendMailForm mailForm = SendMailForm.builder()
                    .from("sin@test.com")
                    .to(form.getEmail())
                    .subject("회원가입 인증 관련 메일입니다.")
                    .text(getVerificationEmail(form.getEmail(), form.getName(), verifyCode))
                    .build();

            mailgunClient.sendEmail(mailForm);
            signUpService.changeMemberValidateStatus(member.getId(), verifyCode);
            return "회원 가입에 성공하였습니다.";
        }
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmail(String email, String name, String code) {
        StringBuilder sb = new StringBuilder();
        return sb.append("안녕하세요 ").append(name + "님 \n").append("본인 인증을 위해 아래 링크를 클릭해주세요. \n\n")
                .append("http://localhost:8081/member/signup" + "/verify?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}
