package com.helmes.sectorsapi.controller;

import com.helmes.sectorsapi.dto.request.ApplicationDTO;
import com.helmes.sectorsapi.dto.response.ApplicationResponseDTO;
import com.helmes.sectorsapi.dto.response.ApplicationSummaryResponseDTO;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ApplicationSummaryResponseDTO>> getAllApplications(@AuthenticationPrincipal User user) {
        var applications = applicationService.fetchAllApplications(user);

        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponseDTO> getByApplicationId(@PathVariable UUID applicationId, @AuthenticationPrincipal User user) {
        var application = applicationService.fetchApplication(user, applicationId);

        return ResponseEntity.ok(application);
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> save(@RequestBody @Valid ApplicationDTO applicationDTO, @AuthenticationPrincipal User user) {
       var application = applicationService.saveApplication(user, applicationDTO);

       return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }
}
