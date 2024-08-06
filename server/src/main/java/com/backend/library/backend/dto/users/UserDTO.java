package com.backend.library.backend.dto.users;

import lombok.Builder;

@Builder
public record UserDTO(
    String id,
    String username,
    String name,
    String surname,
    String email
) {}
