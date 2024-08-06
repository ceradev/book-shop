package com.backend.library.backend.dto.carts;

import java.util.Set;

import lombok.Builder;

@Builder
public record CartDTO(
                Long id,
                String userId,
                Integer totalItems,
                Double amount,
                Double shipping,
                Set<CartBookDTO> items) {
                    public CartDTO {
                        if (shipping == null) {
                            shipping = 4.99;
                        }
                    }

}
