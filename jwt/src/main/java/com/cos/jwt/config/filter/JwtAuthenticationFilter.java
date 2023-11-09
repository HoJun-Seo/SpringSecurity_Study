package com.cos.jwt.config.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
        try {
            ObjectMapper orm = new ObjectMapper(); // JSON 데이터를 파싱해주는 클래스
            User user = orm.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            // 로그인을 시도하기 위해 토큰을 만들어주자.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword());

            System.out.println("------- authenticationToken --------");
            System.out.println("authenticationToken : " + authenticationToken.toString());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("=======================");
            System.out.println(principalDetails.getUser().getUsername());
            System.out.println(principalDetails.getUser().getPassword());
            System.out.println(principalDetails.getUser().getEmail());
            System.out.println(principalDetails.getUser().getRoles());
            System.out.println(principalDetails.getUser().getRoleList());
            // 위의 출력문이 정상적으로 잘 나왔다면 인증이 정상적으로 되서
            // authentication 객체가 세션 영역에 잘 저장되었다는 뜻이며 이는 로그인이 되었다는 의미이다.
            return authentication;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("=====================");

        return null; // 오류가 날 경우 null 리턴
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증 완료");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}