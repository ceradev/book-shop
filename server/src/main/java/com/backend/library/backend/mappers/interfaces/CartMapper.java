package com.backend.library.backend.mappers.interfaces;

import com.backend.library.backend.dto.carts.CartDTO;
import com.backend.library.backend.dto.carts.CartBookDTO;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.CartBook;

public interface CartMapper {

    CartDTO cartToCartDTO(Cart cart);

    CartBookDTO cartBookToCartBookDTO(CartBook cartBook);

    CartBook cartBookDTOtoCartBook(CartBookDTO cartBookDto, Book book, Cart cart);

    CartBookDTO bookToBookItemDTO(Book book, Integer quantity);

    Book cartBookDTOtoBook(CartBookDTO cartBookDTO);
}
