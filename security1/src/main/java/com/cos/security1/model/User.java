package com.cos.security1.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data // getter, setter
public class User {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN

    // 일반 사용자, OAuth 로그인 사용자 구분을 위한 속성
    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;
}