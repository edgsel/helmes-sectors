package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.ApplicationDTO;
import com.helmes.sectorsapi.dto.ApplicationResponseDTO;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> save(@AuthenticationPrincipal User user, @RequestBody @Valid ApplicationDTO applicationDTO) {
       var application = applicationService.saveApplication(user, applicationDTO);

       return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }
}
