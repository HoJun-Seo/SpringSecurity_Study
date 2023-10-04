package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// JpaRepository 가 기본적인 CRUD 함수를 들고있다.
// @Repository 라는 어노테이션이 없어도 IoC 가 되어있는 상태이다.
// 스프링이 컴포넌트 스캔을 할 때 JpaRepisitory 를 상속한 인터페이스가 있는 것을 보고
// 자동으로 JpaRepository 를 상속받은 인터페이스의 객체를 생성하여 스프링 빈으로 등록시켜준다. 
public interface UserRepository extends JpaRepository<User, Integer> {

    // findBy 까지는 규칙, Username 은 문법
    // select * from user where username = ? 과 같은 SQL 쿼리가
    // 하이버네이트를 통해 자동으로 작성되어 데이터베이스로 전달된다.
    // 이때 위의 ? 에는 파라미터로 넘어온 username 이 적재되게 된다.
    public User findByUsername(String username);
}