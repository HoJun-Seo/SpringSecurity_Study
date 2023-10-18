package com.cos.security1.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// 스프링 빈 등록을 위한 @Component 어노테이션 추가
@Component
public class CustomBcryptPasswordEncoder extends BCryptPasswordEncoder {

}
