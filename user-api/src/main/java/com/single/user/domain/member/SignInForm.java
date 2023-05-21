package com.single.user.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInForm {
    private String email;
    private String password;
}
