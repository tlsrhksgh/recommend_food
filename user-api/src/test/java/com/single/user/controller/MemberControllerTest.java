package com.single.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.service.SignUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignUpService signUpService;

    @Test
    public void successSignUp() throws Exception {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();
        given(signUpService.signUp(any()))
                .willReturn(Member.from(form));

        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입에 성공 하였습니다!"))
                .andDo(print());
    }
}