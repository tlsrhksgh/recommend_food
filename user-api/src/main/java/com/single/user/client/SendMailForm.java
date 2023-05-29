package com.single.user.client;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendMailForm {
    private String from;
    private String to;
    private String subject;
    private String text;
}
