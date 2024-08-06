package com.backend.library.backend.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.CartBook;

@Repository
public interface CartBookRepository extends JpaRepository<CartBook, Long> {
    CartBook findByCartAndBook(Cart cart, Book book);
    Optional<CartBook> findAllByBookIsbn(String bookISBN);
    List<CartBook> deleteAllByBookIsbn(String bookISBN);
}
