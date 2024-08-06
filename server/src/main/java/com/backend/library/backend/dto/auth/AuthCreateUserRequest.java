package com.backend.library.backend.dto.auth;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
    @NotBlank(message = "User name cannot be blank")
    String name,
    
    @NotBlank(message = "User lastName cannot be blank")
    String surname,

    @NotBlank(message = "User username cannot be blank")
    String username,
    
    @NotBlank(message = "User email cannot be blank")
    @Email(message = "Please provide a valid email address")
    String email,

    @NotBlank(message = "User password cannot be blank")
    String password,

    @NotBlank(message = "User role cannot be blank")
    Set<String> roles
) {

}
