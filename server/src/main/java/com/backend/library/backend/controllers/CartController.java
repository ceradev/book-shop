package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.library.backend.dto.carts.CartBookDTO;
import com.backend.library.backend.services.interfaces.ICartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/cart")
@Tag(name = "Cart Controller", description = "Controller for managing users carts")
public class CartController {
    private ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Obtain all cart items for a specific user", description = "Obtain all cart items for a client user who is logged in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CartBookDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "ForBidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) })
    @GetMapping()
    public ResponseEntity<?> getCartByUser() {
        return cartService.getUserCart();
    }

    @Operation(summary = "Add item (Book) to user shopping cart", description = "Add a new book to  user shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CartBookDTO.class))),
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = CartBookDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) })
    @PostMapping("/{isbn}/{quantity}")
    public ResponseEntity<?> addItemToTheCart(@PathVariable String isbn,
            @PathVariable int quantity) {
        return cartService.addBookToCart(isbn, quantity);
    }

    @Operation(summary = "Remove a item (Book) from user shopping cart", description = "Remove a item (Book) from user shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) })
    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> removeFromCart(@PathVariable String isbn) {
        return cartService.removeFromCart(isbn);
    }

    @Operation(summary = "Update a item (Book) shopping cart", description = "Update a item (Book) shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CartBookDTO.class))),
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = CartBookDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) })
    @PutMapping("/{isbn}/{quantity}")
    public ResponseEntity<?> updateCartBookQuantity(@PathVariable String isbn,
            @PathVariable int quantity) {
        return cartService.updateCartBookQuantity(isbn, quantity);
    }

    @Operation(summary = "Clear user shopping cart", description = "Remove all items (Books) from  shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) })
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return cartService.clearCart();
    }
}
