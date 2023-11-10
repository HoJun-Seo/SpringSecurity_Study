package com.cos.jwt.config.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA 방식은 아니고 Hash 암호방식
        String jwtToken = JWT.create()
                .withSubject("cos 토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 토큰 만료시간, 시간을 짧게 해줘야 탈취가 되도
                                                                                    // 공격허용
                                                                                    // 시간을 최소화 시킬 수 있다.
                                                                                    // (60,000 * 10 : 10분) -> 이 시간이 지나면
                                                                                    // 토큰을 새로 만들어주어야 한다.
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos")); // sign 은 현재 서버만 알고 있는 고유한 값이어야함
        // withClaim 은 비공개 클레임인데 여기는 넣고싶은 key, value 값을 아무렇게나 넣어도 된다.

        response.addHeader("Authorization", "Bearer " + jwtToken); // 응답 헤더에 토큰 담아주기
    }
}