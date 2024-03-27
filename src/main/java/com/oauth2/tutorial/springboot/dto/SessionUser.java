package com.oauth2.tutorial.springboot.dto;

import java.io.Serializable;

import com.oauth2.tutorial.springboot.domain.User;

import lombok.Getter;

// jwt를 사용하면 session을 사용하지 않는 것이 맞다. (STATELESS) -> 테스트를 위해 임시로 사용

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;
    private String nickname;
    
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.nickname = user.getNickname();
    }
}
