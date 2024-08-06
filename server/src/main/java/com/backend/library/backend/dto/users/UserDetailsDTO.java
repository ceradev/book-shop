package com.backend.library.backend.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDetailsDTO(
    @NotBlank String id,
    @NotBlank String username,
    @NotBlank String name,
    @NotBlank String surname,
    @NotBlank String email,
    @NotBlank String role
) {}
