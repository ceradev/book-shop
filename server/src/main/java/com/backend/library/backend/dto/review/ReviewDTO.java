package com.backend.library.backend.dto.review;

import com.backend.library.backend.dto.users.UserDTO;

import lombok.Builder;

@Builder
public record ReviewDTO(
    Long id,
    UserDTO user,
    Integer rating,
    String comment,
    Boolean isPurchased
) {


}
