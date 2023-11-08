package com.cos.jwt.config.filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 매개변수 다운 캐스팅
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터 1");

            if (headerAuth.equals("cos")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("인증 안됨");
            }
        }
    }
}