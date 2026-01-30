package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(String username, String passwordHash) {
        return User.builder()
            .username(username)
            .passwordHash(passwordHash)
            .build();
    }
}
