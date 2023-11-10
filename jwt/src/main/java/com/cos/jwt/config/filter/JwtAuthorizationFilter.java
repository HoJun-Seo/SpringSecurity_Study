package com.cos.jwt.config.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// 시큐리티가 필터를 가지고 있는데, 그 필터중에 BasicAuthenticationFilter 가 있다.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게되어 있다.
// 만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        System.out.println("인증이나 권한이 필요한 주소가 요청됨");
    }
}
