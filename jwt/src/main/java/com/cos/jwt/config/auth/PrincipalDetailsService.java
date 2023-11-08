package com.cos.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login 요청이 왔을 때 동작
// Spring Security 에서 기본적으로 지정되어 있는 로그인 요청 주소가 /login 이다. 
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService 의 loadUserByUsername()");
        User userEntity = userRepository.findByUsername(username);
        System.out.println("----- loadUserByUsername 실행 -----");
        System.out.println("userEntity : " + userEntity);
        return new PrincipalDetails(userEntity);
    }
}