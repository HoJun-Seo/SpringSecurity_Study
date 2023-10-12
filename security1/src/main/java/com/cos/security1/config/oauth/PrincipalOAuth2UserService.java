package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    // 후처리 기능을 제공해주는 함수 오버라이드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest 객체가 무엇인지 확인해보자.
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
        return super.loadUser(userRequest);
    }
}
