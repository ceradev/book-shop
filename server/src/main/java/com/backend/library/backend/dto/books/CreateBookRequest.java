package com.backend.library.backend.dto.books;

import java.util.Set;

import com.backend.library.backend.persistence.entities.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateBookRequest(
    @NotBlank(message = "Book isbn cannot be blank")
    String isbn,   

    String cover,

    @NotBlank(message = "Book title cannot be blank")
    String title,

    Long authorId,

    @NotNull(message = "Book editorial cannot be null")
    Long editorialId,    

    String synopsis,

    @NotBlank(message = "Book edition cannot be blank")
    String edition,

    @NotNull(message = "Book price cannot be null")
    Double price,
    
    Set<Genre> genres
    
) {

}
