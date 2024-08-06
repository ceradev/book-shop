package com.backend.library.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank(message = "User name cannot be blank") String username,
        @NotBlank(message = "User password cannot be blank") String password) {
}
