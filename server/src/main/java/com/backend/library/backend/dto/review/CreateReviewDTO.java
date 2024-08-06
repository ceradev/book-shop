package com.backend.library.backend.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReviewDTO(
    Integer id,
    @NotBlank(message = "Book ISBN cannot be blank")
    String bookIsbn,
    @NotBlank(message = "User ID cannot be blank")
    String userId,
    @NotNull(message = "Rating cannot be null")
    Integer rating,
    String comment,
    @NotNull(message = "IsPurchased cannot be null")
    Boolean isPurchased
    
    ) {

}
