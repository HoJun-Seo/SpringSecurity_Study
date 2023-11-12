package com.cos.jwt.config.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 시큐리티가 필터를 가지고 있는데, 그 필터중에 BasicAuthenticationFilter 가 있다.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게되어 있다.
// 만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    // UserRepository 의존성 주입을 위한 생성자 코드 변경
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소가 요청됨");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("요청 헤더 Authorization 값 : " + jwtHeader);

        // 헤더가 있는지 확인하고, 헤더 값이 정상적이지 않으면 그대로 필터를 타게 만든 다음 메서드 종료
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        } else {
            // JWT 토큰 검증을 통해 정상적인 사용자인지 확인

            // 검증을 위한 키값인 Bearer 을 제외시키고 넘어온 순수 JWT 토큰만 추출
            String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

            // 서명을 통해 요청으로 넘어온 JWT 토큰이 정상인지 확인(verify) 하고, 정상일 경우 username 속성을 가져온다.
            String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username")
                    .asString();

            // 서명이 정상적으로 완료된 경우
            if (username != null) {
                User userEntity = userRepository.findByUsername(username);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                // 로그인을 시켜서 서비스를 제공해주고자 하는것이 목적이 아님, 로그인은 이미 되어있는 상태
                // 그저 단순히 유저에 대한 정보가 담긴 객체로서
                // 스프링 시큐리티 세션에 적재하여 사용하기 위해 Authentication 객체를 만드는것일 뿐이므로
                // Credentials 에 엄격하게 유저 비밀번호를 담아줄 필요는 없음
                // 그저 현재 세션에 로그인 되어있는 유저가 어떤 권한을 가지고 있는지만 getAuthorities() 메서드를 통해 적재해주면 된다.

                // 스프링 시큐리티 세션에 담을 Authentication 객체를 만들기
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                        principalDetails.getAuthorities());

                // 스프링 시큐리티 세션 영역 불러오기
                // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        }
    }
}