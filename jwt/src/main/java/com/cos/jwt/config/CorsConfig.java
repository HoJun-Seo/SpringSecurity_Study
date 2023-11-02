package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 클라이언트 측에서 자바스크립트로 서버측에 요청을 보내면 서버측이 응답을 해줄지 말지 결정하기 위한 설정
        // 만약 클라이언트 측에서(프론트) jquery ajax, fetch, axios 와 같은 자바스크립트 라이브러리들로
        // 서버측에 비동기 방식으로 데이터를 요청하면 응답을 돌려줄지 말지 결정하는 설정이다.
        // true 이면 허용, false 이면 불가
        config.setAllowCredentials(true);

        // 모든 IP 에 대한 요청 허용
        config.addAllowedOrigin("*");
        // 모든 header 에 대한 요청 허용
        config.addAllowedHeader("*");
        // 모든 HTTP Method 에 대한 요청 허용
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}