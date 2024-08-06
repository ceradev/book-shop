package com.backend.library.backend.mappers.implementations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.backend.library.backend.dto.carts.CartBookDTO;
import com.backend.library.backend.dto.carts.CartDTO;
import com.backend.library.backend.mappers.interfaces.CartMapper;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.CartBook;

@Service
public class CartMapperImpl implements CartMapper {

        // Hay que hacer cambios
        @Override
        public CartDTO cartToCartDTO(Cart cart) {
            List<CartBook> cartBooks = cart.getBooks();
            Set<CartBookDTO> items = null;
            Double totalAmount = 0.0;
            if (cartBooks != null) {
                totalAmount = cartBooks.stream()
                        .mapToDouble(cartBook -> cartBook.getBook().getPrice() * cartBook.getQuantity())
                        .sum();
    
                items = cartBooks.stream()
                        .map(cartBook -> bookToBookItemDTO(cartBook.getBook(), cartBook.getQuantity()))
                        .collect(Collectors.toSet());
    
                totalAmount = Math.round(totalAmount * 100.0) / 100.0;
            }
    
            return CartDTO.builder()
                    .id(cart.getId())
                    .userId(cart.getUserId())
                    .amount(totalAmount)
                    .totalItems(cart.getBooks() != null ? cart.getBooks().size() : 0)
                    .items(items)
                    .build();
        }
        
        @Override
        public Book cartBookDTOtoBook(CartBookDTO cartBookDTO) {
                Book book = Book.builder()
                                .isbn(cartBookDTO.isbn())
                                .cover(cartBookDTO.cover())
                                .title(cartBookDTO.title())
                                .editorial(cartBookDTO.editorial())
                                .edition(cartBookDTO.edition())
                                .price(cartBookDTO.price())
                                .stock(cartBookDTO.stock())
                                .build();
                return book;
        }

    @Override
    public CartBookDTO bookToBookItemDTO(Book book, Integer quantity) {// por aqui tengo que recibir la cantidad de
                                                                       // elementos
        // BookInCartResponseDTO bookDTO = this.booktoBookInCartResponseDTO(book);
        CartBookDTO bookItemDTO = CartBookDTO.builder()
                .isbn(book.getIsbn())
                .cover(book.getCover())
                .quantity(quantity)
                .edition(book.getEdition())
                .editorial(book.getEditorial())
                .price(book.getPrice())
                .stock(book.getStock())
                .title(book.getTitle())
                // .quantity(book.getQuantity())
                .build();

        return bookItemDTO;
    }

    @Override
    public CartBookDTO cartBookToCartBookDTO(CartBook cartBook) {
        return CartBookDTO.builder()
                .isbn(cartBook.getBook().getIsbn())
                .cover(cartBook.getBook().getCover())
                .title(cartBook.getBook().getTitle())
                .editorial(cartBook.getBook().getEditorial())
                .edition(cartBook.getBook().getEdition())
                .price(cartBook.getBook().getPrice())
                .stock(cartBook.getBook().getStock())
                .quantity(cartBook.getQuantity())
                .build();
    }

    @Override
    public CartBook cartBookDTOtoCartBook(CartBookDTO cartBookDto, Book book, Cart cart) {
        CartBook cartBook = CartBook.builder()
                .book(book)
                .cart(cart)
                .quantity(cartBookDto.quantity())
                .build();
        return cartBook;
    }

}
