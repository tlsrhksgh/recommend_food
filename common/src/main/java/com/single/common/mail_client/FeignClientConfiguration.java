package com.single.common.mail_client;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public MailgunClient mailgunClient() {
        return Feign.builder()
                .target(MailgunClient.class, "https://api.mailgun.net/v3/");
    }
}

