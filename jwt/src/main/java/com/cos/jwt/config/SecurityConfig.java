package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.config.filter.JwtAuthenticationFilter;
import com.cos.jwt.config.filter.JwtAuthorizationFilter;
import com.cos.jwt.config.filter.MyFilter1;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        // CorsConfig 클래스에서 등록한 CorsFilter 스프링 빈 의존성 주입(DI)
        private final CorsFilter corsFilter;
        private final UserRepository userRepository;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                // http.addFilterBefore(new MyFilter1(), SecurityContextHolderFilter.class); ->
                // JWT 실습을 위한 주석처리
                http.csrf((csrfConfig) -> csrfConfig.disable());

                // 세션을 사용하지 않겠다는 설정
                // JWT 를 활용하면 세션을 사용하지 않는 설정이 기본이다.
                http.sessionManagement(
                                (sessionManagement) -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // formLogin 과 기본적인 http 로그인 기능을 사용하지 않음
                http.formLogin((formLogin) -> formLogin.disable());
                http.httpBasic((httpBasic) -> httpBasic.disable())
                                .apply(new MyCustomDsl());

                // 권한별 특정 URI 접근 요청 허가
                http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/v1/user/**")
                                .access(new WebExpressionAuthorizationManager(
                                                "hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')"))
                                .requestMatchers("/api/v1/manager/**")
                                .access(new WebExpressionAuthorizationManager(
                                                "hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')"))
                                .requestMatchers("/api/v1/admin/**")
                                .access(new WebExpressionAuthorizationManager(
                                                "hasRole('ROLE_ADMIN')"))
                                .anyRequest().permitAll());

                return http.build();
        }

        public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

                @Override
                public void configure(HttpSecurity http) throws Exception {
                        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
                        http.addFilter(corsFilter).addFilter(new JwtAuthenticationFilter(authenticationManager))
                                        .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
                }
        }
}