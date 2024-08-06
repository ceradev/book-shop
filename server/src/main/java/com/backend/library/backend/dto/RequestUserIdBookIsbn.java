package com.backend.library.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestUserIdBookIsbn(
    @NotBlank
    String userId,
    @NotBlank
    String bookIsbn
) {

}
