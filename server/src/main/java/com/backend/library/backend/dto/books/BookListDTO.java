package com.backend.library.backend.dto.books;

import java.util.Set;

import com.backend.library.backend.dto.GenreDTO;
import com.backend.library.backend.dto.authors.AuthorListDTO;
import com.backend.library.backend.persistence.entities.enums.BookStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookListDTO(
                String isbn,

                String cover,

                @NotBlank(message = "BookListDTO title cannot be blank") String title,
                AuthorListDTO author,
                @NotNull(message = "Book editorial cannot be null") String editorial,
                @NotNull(message = "BookListDTO status cannot be null") BookStatus status,
                @NotNull(message = "BookListDTO price cannot be null") Double price,
                @NotNull(message = "Media review cannot be null") Double reviewMean,
                @NotNull(message = "Number of reviews cannot be null") Integer nReviews,
                @NotNull(message = "Genres cannot be null")  Set<GenreDTO> genres,
                @NotNull(message = "Stock cannot be null") Integer stock) {

}
