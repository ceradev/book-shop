package com.backend.library.backend.dto.review;

import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.users.UserDTO;

import lombok.Builder;

@Builder
public record ReviewBookDTO(
    Long id,
    UserDTO user,
    Integer rating,
    String comment,
    Boolean isPurchased,
    String isbn,
    String title,
    AuthorDetailsDTO author
) {


}
