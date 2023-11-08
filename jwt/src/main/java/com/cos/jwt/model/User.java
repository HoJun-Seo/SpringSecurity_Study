package com.cos.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String roles; // USER, ADMIN
    private String email;

    // 만약 유저 Role 이 하나만 존재하면 굳이 getRoleList() 와 같은 메서드를 만들어줄 필요가 없다.
    // 아래의 메서드는 한 유저가 두 가지 이상의 권한을 가지고 있을 경우
    // "USER, ADMIN" 과 같은 형태로 정보가 저장되는데
    // 여기서 서로 다른 권한을 구분지은 형태의 리스트를 만들어서 반환하는 방식으로 메서드를 작성하였다.

    // 물론 여기서 roles 를 String 타입이 아닌 Role 객체를 따로 만들어서 관리해줄 수도 있다.
    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));

        }
        return new ArrayList<>();
    }
}