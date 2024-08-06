package com.backend.library.backend.dto.books;

import java.util.Date;
import java.util.Set;
import com.backend.library.backend.dto.GenreDTO;
import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.persistence.entities.enums.BookStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookDTO(
        String isbn,

        String cover,

        @NotBlank(message = "BookDTO title cannot be blank") String title,

        AuthorDetailsDTO author,

        @NotNull(message = "BookDTO editorial cannot be null") String editorial,

        String synopsis,

        @NotBlank(message = "BookDTO edition cannot be blank") String edition,

        @NotNull(message = "BookDTO status cannot be null") BookStatus status,

        @NotNull(message = "BookDTO price cannot be null") Double price,

        @NotNull(message = "BookDTO publishDate cannot be null") Date publishDate,

        Set<GenreDTO> genres,

        Integer salesAmount

) {

}
