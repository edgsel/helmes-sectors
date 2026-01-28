package com.helmes.sectorsapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDTO {

    @Size(min = 2, max = 240)
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    private String applicantName;

    @NotEmpty(message = "Selected sectors cannot be empty")
    private Set<Long> sectorIds;

    @NotNull(message = "Terms must be accepted")
    @AssertTrue(message = "Terms must be accepted")
    private Boolean agreedToTerms;
}
