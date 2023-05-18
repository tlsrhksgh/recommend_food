package com.single.user.domain.model;

import com.single.user.domain.member.SignUpForm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phone;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    public static Member from(SignUpForm form) {
        return Member.builder()
                .email(form.getEmail())
                .password(form.getPassword())
                .name(form.getName())
                .phone(form.getPhone())
                .verify(false)
                .build();
    }
}
