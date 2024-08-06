package com.backend.library.backend.services.interfaces;

import org.springframework.http.ResponseEntity;

import com.backend.library.backend.persistence.entities.Cart;

public interface ICartService {

    ResponseEntity<?> getUserCart();

    ResponseEntity<?> addBookToCart(String bookId, int quantity);

    ResponseEntity<?> removeFromCart(String bookId);

    ResponseEntity<?> updateCartBookQuantity(String bookId, int quantity);

    ResponseEntity<?> clearCart();

    Cart createCartForUser(String userId);

    Cart getCartById(Long id);

}
