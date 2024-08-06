package com.backend.library.backend.dto.carts;

import lombok.Builder;

@Builder
public record CartBookDTO(
        String isbn,
        String cover,
        String title,
        String editorial,
        String edition,
        Double price,
        Integer stock,
        Integer quantity) {

}
