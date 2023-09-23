package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({ "", "/" })
    public String index() {
        // 머스테치(Mustache) 는 스프링에서 권장하는 템플릿 엔진 중 하나이다.
        // View 파일을 찾는 기본적인 경로는 src/main/resources/ 로 잡혀있다.
        // 뷰 리졸버 경로는 templates(prefix), .mustache(suffix) 로 잡혀있다.
        return "index";
    }
}
