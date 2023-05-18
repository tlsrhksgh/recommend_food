package com.single.user.domain.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpForm {
    private String email;
    private String name;
    private String password;
    private String phone;
}
