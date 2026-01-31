package com.helmes.sectorsapi.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateApplicationDTO(
    @Size(min = 3, max = 100)
    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Name can only contain letters and spaces")
    String applicantName,

    @NotEmpty(message = "Selected sectors cannot be empty")
    Set<Long> sectorIds,

    @NotNull(message = "Terms must be accepted")
    @AssertTrue(message = "Terms must be accepted")
    Boolean agreedToTerms
) {}
