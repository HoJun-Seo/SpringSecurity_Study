package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

    // 해당 User 의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return user.getRole();
            }

        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되었는지 여부 확인 메소드, true 를 반환하면 계정이 만료되지 않았다는 뜻이다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겼는지 여부를 확인하는 메소드
    // true 를 반환하면 계정이 잠겨있지 않음을 나타낸다.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 너무 오래된것이 아닌지 체크하는 메소드
    // true 를 반환하면 그렇지 않음을 뜻한다.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되어있는지 여부를 확인하는 메소드
    // true 를 반환하면 계정이 활성화 되어있음을 뜻한다.
    @Override
    public boolean isEnabled() {
        return true;

        // 이 메소드에서 false 를 반환하는 경우는 다음과 같다.
        /*
         * 우리 사이트에서 1년동안 회원이 로그인을 하지 않아 휴면 계정으로 전환된 경우
         * User 정보에 TimeStamp loginDate 와 같은정보가 있으면 확인 가능하다.
         * uset.getLoginDate(); 메소드를 통해 최종 로그인 날짜를 가져와서
         * 현재시간 - 최종 로그인 시간을 계산한 결과가 1년을 초과하면 false 를 반환하는 방식
         */
    }
}