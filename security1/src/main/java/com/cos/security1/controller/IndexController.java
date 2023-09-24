package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
    @GetMapping("/login")
    @ResponseBody
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    @ResponseBody
    public String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    @ResponseBody
    public String joinProc() {
        return "회원가입 완료됨";
    }
}
