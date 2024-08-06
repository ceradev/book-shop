package com.backend.library.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GenreDTO(
    
    Long id,

    @NotBlank(message = "Genre name cannot be blank")
    String name
    
    ) {    
}
