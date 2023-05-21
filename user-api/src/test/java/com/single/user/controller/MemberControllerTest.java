package com.single.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.user.application.SignUpApplication;
import com.single.user.domain.member.SignUpForm;
import com.single.user.domain.model.Member;
import com.single.user.service.SignUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private SignUpApplication signUpApplication;

    @MockBean
    private SignUpService signUpService;

    @Test
    void successSignUp() throws Exception {
        SignUpForm form = SignUpForm.builder()
                .email("test@naver.com")
                .name("test")
                .phone("010-1111-1234")
                .password("test")
                .build();

        given(signUpApplication.memberSignUp(any()))
                .willReturn("회원 가입에 성공 하였습니다.");
        given(signUpService.signUp(any()))
                .willReturn(Member.from(form));

        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원 가입에 성공 하였습니다."))
                .andDo(print());
    }

    @Test
    void successVerify() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String email = "test@test.com";
        String code = "1234";
        map.add("email", email);
        map.add("code", code);

        mockMvc.perform(get("/member/verify")
                .params(map))
                .andExpect(status().isOk())
                .andExpect(content().string("인증이 완료 되었습니다."))
                .andDo(print());
        verify(signUpApplication, times(1)).memberVerify(email, code);
    }

    @Test
    void successSignIn() {

    }
}