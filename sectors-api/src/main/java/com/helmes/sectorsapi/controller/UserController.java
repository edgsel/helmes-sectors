package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.response.AuthResponseDTO;
import com.helmes.sectorsapi.dto.request.UserAuthDTO;
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
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserAuthDTO dto) {
        var token = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid UserAuthDTO dto) {
        var token = userService.authenticate(dto);
        return ResponseEntity.ok(token);
    }
}

