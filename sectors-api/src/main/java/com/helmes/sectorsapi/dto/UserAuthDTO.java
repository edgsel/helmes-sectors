package com.helmes.sectorsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO {

    @NotNull
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}