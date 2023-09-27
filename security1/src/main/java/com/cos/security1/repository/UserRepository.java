package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// JpaRepository 가 기본적인 CRUD 함수를 들고있다.
// @Repository 라는 어노테이션이 없어도 IoC 가 되어있는 상태이다.
// 스프링이 컴포넌트 스캔을 할 때 JpaRepisitory 를 상속한 인터페이스가 있는 것을 보고
// 자동으로 JpaRepository 를 상속받은 인터페이스의 객체를 생성하여 스프링 빈으로 등록시켜준다. 
public interface UserRepository extends JpaRepository<User, Integer> {

}