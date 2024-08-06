package com.backend.library.backend.dto.authors;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthorDetailsDTO(
                Long authorId,

                @NotBlank(message = "Author firstName cannot be blank") String firstName,

                @NotBlank(message = "Author lastName cannot be blank") String lastName,

                String biography,

                String portrait) {

}
