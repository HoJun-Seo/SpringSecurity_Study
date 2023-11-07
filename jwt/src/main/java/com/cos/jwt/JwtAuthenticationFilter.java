package com.cos.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 존재한다.
// /login URI 로 요청해서 username, password 를 post 로 전송하면 
// UsernamePasswordAuthenticationFilter 필터가 동작한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도 중");

        // 1. username, password 를 받음
        // 2. 정상인지 로그인 시도, authenticationManager 로 로그인을 시도하면 PrincipalDetailsService 가
        // 호출됨
        // loadUserByUsername() 메서드가 호출됨
        // 3. 메서드 실행 결과 정상적으로 PrincipalDetails 객체가 리턴되면 이 객체가 세션이 저장됨
        // 4. JWT 토큰을 만들어서 응답해주면 됨

        // JWT 를 쓰는데 굳이 세션에 PrincipalDetails 객체를 담는 이유는
        return super.attemptAuthentication(request, response);
    }
}