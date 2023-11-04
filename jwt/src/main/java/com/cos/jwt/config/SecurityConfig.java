package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.filter.MyFilter1;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        // CorsConfig 클래스에서 등록한 CorsFilter 스프링 빈 의존성 주입(DI)
        private final CorsFilter corsFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf((csrfConfig) -> csrfConfig.disable());

                // 세션을 사용하지 않겠다는 설정
                // JWT 를 활용하면 세션을 사용하지 않는 설정이 기본이다.
                http.sessionManagement(
                                (sessionManagement) -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // formLogin 과 기본적인 http 로그인 기능을 사용하지 않음
                http.formLogin((formLogin) -> formLogin.disable());
                http.httpBasic((httpBasic) -> httpBasic.disable());

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

                // CORS 필터 추가
                // 모든 요청을 이 필터를 거치게 되며, 이렇게 되면 서버는 모든 CORS 정책들에 대해 벗어나게 된다.
                // 즉, cross-origin 요청이 와도 모두 허용된다.
                // 인증이 필요없는 요청의 경우 컨트롤러에 @CrossOrigin 어노테이션을 달아주는 것으로 해결할 수 있으나
                // 인증이 필요한 요청들의 경우 이와 같이 Security 필터에 직접 CORS 와 관련된 요청 허용설정을 등록해주어야 한다.
                http.addFilter(corsFilter);

                return http.build();
        }
}