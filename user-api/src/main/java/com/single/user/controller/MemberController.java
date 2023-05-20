package com.single.user.controller;

import com.single.user.application.SignInApplication;
import com.single.user.application.SignUpApplication;
import com.single.user.domain.member.SignInForm;
import com.single.user.domain.member.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final SignUpApplication signUpApplication;
    private final SignInApplication signInApplication;

    @PostMapping("/signup")
    public ResponseEntity<String> memberSignUp(@RequestBody SignUpForm form) {
        return ResponseEntity.ok(signUpApplication.memberSignUp(form));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyMember(String email, String code) {
        signUpApplication.memberVerify(email, code);
        return ResponseEntity.ok("인증이 완료 되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> memberSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(signInApplication.MemberLoginToken(form));
    }
}