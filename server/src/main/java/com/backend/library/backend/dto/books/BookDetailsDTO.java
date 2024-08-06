package com.backend.library.backend.dto.books;

import java.util.Date;
import java.util.Set;
import com.backend.library.backend.dto.GenreDTO;
import com.backend.library.backend.dto.authors.AuthorListDTO;
import com.backend.library.backend.persistence.entities.enums.BookStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookDetailsDTO(
        @NotBlank(message = "BookDetailsDTO isbn cannot be blank") String isbn,

        String cover,

        @NotBlank(message = "BookDetailsDTO title cannot be blank") String title,

        AuthorListDTO author,

        @NotBlank(message = "BookDetailsDTO editorial cannot be blank") String editorial,

        String synopsis,

        @NotBlank(message = "BookDetailsDTO edition cannot be blank") String edition,

        @NotNull(message = "BookDetailsDTO status cannot be null") BookStatus status,

        @NotNull(message = "BookDetailsDTO price cannot be null") Double price,

        @NotNull(message = "BookDetailsDTO publishDate cannot be null") Date publishDate,
        @NotNull(message = "Stock cannot be null") Integer stock,

        Double reviewMean,
        
        Integer nReviews,

        Set<GenreDTO> genres,

        Integer salesAmount

) {

}
