package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 기본 필터 체인에 등록됨
public class SecurityConfig {

    // @Bean 어노테이션을 붙여주면 해당 메서드의 리턴되는 객체를
    // 스프링 컨테이너에 IoC 로 등록해준다.(스프링 빈 등록)
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrfConfig) -> csrfConfig.disable());
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**")
                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')"))
                .requestMatchers("/admin/**")
                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')"))
                .anyRequest().permitAll());

        // formLogin 설정 추가
        // login -> loginForm (동사 -> 명사로 요청 URI 이름 변경)
        http.formLogin((formLogin) -> formLogin.loginPage("/loginForm"));

        return http.build();
    }
}