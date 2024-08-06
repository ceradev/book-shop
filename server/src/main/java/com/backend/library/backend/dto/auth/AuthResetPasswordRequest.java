package com.backend.library.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthResetPasswordRequest(
    @NotBlank(message = "User name cannot be blank") String username,
    @NotBlank(message = "Old password cannot be blank") String oldPassword,
    @NotBlank(message = "New password cannot be blank") String newPassword
) {
    
}
