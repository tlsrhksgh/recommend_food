package com.single.user.config.filter;

import com.single.user.config.JwtAuthenticationProvider;
import com.single.user.service.MemberService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
@WebFilter(urlPatterns = "/member/*")
public class MemberFilter implements Filter {
    private final JwtAuthenticationProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH_TOKEN");
        if(!jwtProvider.validateToken(token)) {
            throw new ServletException("Invalid token");
        }
        String userPk = jwtProvider.getUserPk(token);
        memberService.findByEmail(userPk)
                .orElseThrow(() -> new ServletException("Invalid user info"));
        chain.doFilter(request, response);
    }
}
