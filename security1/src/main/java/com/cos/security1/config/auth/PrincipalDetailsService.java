package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 과 같이 코드를 작성해두었는데
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 PrincipalDetailsService 객체를 통해
// loadUserByUsername 함수가 실행된다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 시큐리티 session = Authentication 타입 객체 <= UserDetails 타입 객체가 들어와야 함
    // 이 메소드에서 리턴된 객체는 Authentication 타입 객체의 내부에 적재된다.
    // 정확한 형태 : 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username : " + username);
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            System.out.println("======== 유저 정보 찾기 완료 ========");
            System.out.println("username : " + userEntity.getUsername());
            System.out.println("password : " + userEntity.getPassword());
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}