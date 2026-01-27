package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.UserDataDTO;
import com.helmes.sectorsapi.service.UserDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
public class UserDataController {

    private final UserDataService userDataService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid UserDataDTO userDataDTO) {
        userDataService.saveUser(userDataDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
