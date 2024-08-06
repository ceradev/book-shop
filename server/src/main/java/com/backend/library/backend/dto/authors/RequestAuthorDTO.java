package com.backend.library.backend.dto.authors;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestAuthorDTO {

    @NotBlank(message = "Author firstName cannot be blank")
    private String firstName;

    @NotBlank(message = "Author lastName cannot be blank")
    private String lastName;

    private String biography;

    private String portrait;
}
