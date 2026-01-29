package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.UserRegisterDTO;
import com.helmes.sectorsapi.dto.UserAuthDTO;
import com.helmes.sectorsapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody @Valid UserRegisterDTO dto) {
        var user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserAuthDTO dto) {
        var token = userService.authenticate(dto);
        return ResponseEntity.ok(token);
    }
}

