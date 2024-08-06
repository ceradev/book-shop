package com.backend.library.backend.services.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.backend.library.backend.dto.carts.CartBookDTO;
import com.backend.library.backend.dto.carts.CartDTO;
import com.backend.library.backend.dto.users.UserDTO;
import com.backend.library.backend.handlers.exceptions.NotFoundException;
import com.backend.library.backend.handlers.exceptions.UnexpectedException;
import com.backend.library.backend.mappers.interfaces.CartMapper;
import com.backend.library.backend.mappers.interfaces.UserMapper;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.CartBook;
import com.backend.library.backend.persistence.repositories.BookRepository;
import com.backend.library.backend.persistence.repositories.CartBookRepository;
import com.backend.library.backend.persistence.repositories.CartRepository;
import com.backend.library.backend.services.interfaces.ICartService;
import com.backend.library.backend.utils.KeycloakProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartBookRepository cartBookRepository;
    private final UserServiceImpl userService;

    @Override
    public ResponseEntity<?> getUserCart() {
        // 1. Obtener el usuario logueado.
        UserDTO user = userService.getUserDTO();

        // 2. Obtener el carrito del usuario logueado, si no hay ningun carrito de
        // compra relacionado con el usuario si creara una.
        Cart cart = getCartOrCreateIfExists(user);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(cartMapper.cartToCartDTO(cart));
        } catch (InternalServerError e) {
            log.error("The cart could not be retrieved.", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The cart could not be retrieved.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> addBookToCart(String isbn, int quantity) {
        if (quantity <= 0) {
            log.error("The quantity cannot be negative.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The quantity cannot be negative.");
        }
        if (isbn.isEmpty() || isbn == null) {
            log.error("The isbn cannot be null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The isbn cannot be null.");
        }
        // 1. Obetener el usuario logueado
        UserDTO user = userService.getUserDTO();

        // 2. verficar si el cart exsite para el usuario, si no se crea una
        Cart cart = getCartOrCreateIfExists(user);

        // 3. Obtener el libro si el libro existe si no se lanzara un NOT_FOUND.
        Book book = getBookByIdIfExists(isbn);
        Integer existingStock = book.getStock();
        // 4. verificar si el stock es suficiente, y si la cantidad que se solicita
        // existe.
        // si no se lanzara un BAD_REQUEST
        if (book.getStock() >= quantity) {
            // declarar CartBookDTO
            // 5. verificar si el libro esta en el carrito
            CartBook existingCartBook = cartBookRepository.findByCartAndBook(cart, book);
            // si no esta en el carrito si crea uno nuevo
            if (existingCartBook == null) {
                existingCartBook = CartBook.builder()
                        .quantity(quantity)
                        .book(book)
                        .cart(cart)
                        .build();
            } else {
                // si existe se actualiza la cantidad, y se asegura que la cantidad del cesta no
                // sea
                // mayor la del stock
                if (existingStock < (existingCartBook.getQuantity() + quantity)) {

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Insufficient quantity available for book with ID: " + isbn);
                }
                existingCartBook.setQuantity(existingCartBook.getQuantity() + quantity);
            }
            // Paso 6 guardar el cartbook en la base de datos
            try {
                CartBookDTO cartBookAdded = cartMapper.cartBookToCartBookDTO(cartBookRepository.save(existingCartBook));
                return ResponseEntity.status(HttpStatus.OK)
                        .body(cartBookAdded);
            } catch (Exception e) {
                log.error("Cannot add a book to the cart ", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot add a book to the cart");
            }

        } else {
            log.error("Insufficient quantity available for book with ID: " + isbn);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient quantity available for book with ID: " + isbn);
        }
    }

    @Override
    public ResponseEntity<?> removeFromCart(String isbn) {
        if (isbn.isEmpty() || isbn == null) {
            log.error("The isbn cannot be null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The isbn cannot be null.");
        }
        UserDTO user = userService.getUserDTO();
        Book book = getBookByIdIfExists(isbn);
        Cart cart = getCartOrCreateIfExists(user);

        CartBook existingCartBook = cartBookRepository.findByCartAndBook(cart, book);

        // 1. Si el libro no existe devuelve NOT_FOUND
        if (existingCartBook == null) {
            log.error("None Book found with ID: " + isbn + " in cart ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("None Book found with ID: " + isbn + " in cart ");
        }

        // 1. Si el libro existe si verifica la cantidad.
        // 1.A. Si es igual a uno se elimina del carrito.
        // 1.B. Si es mayor que uno se deseminuye la cantidad.
        if (existingCartBook.getQuantity() == 1) {
            try {
                cartBookRepository.delete(existingCartBook);

            } catch (Exception e) {
                log.error("Unexpected Error occurred while deleting de cart book: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected Error occurred while deleting de cart book");
            }
        }

        if (existingCartBook.getQuantity() > 1) {
            existingCartBook.setQuantity(existingCartBook.getQuantity() - 1);
            try {
                cartBookRepository.save(existingCartBook);
            } catch (Exception e) {
                log.error("Unexpected Error occurred while saving de cart book: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected Error occurred while saving de cart book");
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCartBookQuantity(String isbn, int quantity) {
        if (quantity < 0) {
            log.error("The quantity cannot be negative.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The quantity cannot be negative.");
        }
        if (isbn.isEmpty() || isbn == null) {
            log.error("The isbn cannot be null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The isbn cannot be null.");
        }
        UserDTO user = userService.getUserDTO();
        Book book = getBookByIdIfExists(isbn);
        Cart cart = getCartOrCreateIfExists(user);

        CartBook existingCartBook = cartBookRepository.findByCartAndBook(cart, book);
        // 1. Si el libro no existe devuelve NOT_FOUND
        if (existingCartBook == null) {
            log.error("None Book found with ID: " + isbn + " in cart ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("None Book found with ID: " + isbn + " in cart ");
        }

        // 1. Si el libro existe si verifica lo siguiente:
        // 1.A. Que la cantidad que se pasa no sea mayor de la existencia
        if (book.getStock() < quantity) {
            log.error("Insufficient quantity available for book with ID: " + book.getIsbn());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient quantity available for book with ID: " + book.getIsbn());
        }
        // 1.B. Si la cantidad es cero se borra del carrito
        if (quantity == 0) {
            try {
                cartBookRepository.delete(existingCartBook);
            } catch (Exception e) {
                log.error("Unexpected Error occurred while deleting de cart book: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected Error occurred while deleting de cart book");
            }
        }

        // 1.C. Si no se comple ninguna de la condiciones anteriores si actualizar la
        // cantidad del carrito.
        existingCartBook.setQuantity(quantity);
        try {
            CartBookDTO cartBookUpdated = cartMapper.cartBookToCartBookDTO(cartBookRepository.save(existingCartBook));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(cartBookUpdated);
        } catch (Exception e) {
            log.error("Unexpected Error occurred while saving de cart book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error occurred while saving de cart book");
        }

    }

    @Override
    public ResponseEntity<?> clearCart() {
        UserDTO user = userService.getUserDTO();
        Cart cart = getCartOrCreateIfExists(user);
        try {
            cartBookRepository.deleteAll(cart.getBooks());
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            log.error("Unexpected Error occurred while deleting de cart book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error occurred while deleting de cart book");
        }
    }

    // Private methods
    private Book getBookByIdIfExists(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + bookId));
    }

    private Cart getCartOrCreateIfExists(UserDTO user) {

        return cartRepository.findByUserId(user.id())
                .orElseGet(() -> createCartForUser(user.id()));
    }

    @Override
    public Cart createCartForUser(String userId) {
        Cart newCart = Cart.builder()
                .userId(userId)
                .build();
        return cartRepository.save(newCart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such Cart found with ID: " + id));
    }
}
