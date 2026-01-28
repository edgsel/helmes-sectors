package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.ApplicationDTO;
import com.helmes.sectorsapi.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> save(
        @PathVariable("userId") Long userId,
        @RequestBody @Valid ApplicationDTO applicationDTO
    ) {
        applicationService.saveApplication(userId, applicationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
