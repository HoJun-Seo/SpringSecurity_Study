package com.cos.security1.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {

    // 의존성 추가
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({ "", "/" })
    public String index() {
        // 머스테치(Mustache) 는 스프링에서 권장하는 템플릿 엔진 중 하나이다.
        // View 파일을 찾는 기본적인 경로는 src/main/resources/ 로 잡혀있다.
        // 뷰 리졸버 경로는 templates(prefix), .mustache(suffix) 로 잡혀있다.
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    // 스프링 시큐리티가 해당 주소를 낚아챔 -> SecurityConfig 파일 생성 후 작동하지 않음(낚아채지 못함)
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        // ID 는 데이터베이스에서 키 값에 대해
        // Auto_Increment 전략이 적용되었기 때문에 setter 를 사용해줄 필요가 없다.
        // createDate 는 @CreationTimestamp 어노테이션을 통해
        // 생성날짜가 자동으로 생성되었으므로 setter 를 사용해줄 필요가 없다.

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); // 인코딩된 패스워드 저장
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}