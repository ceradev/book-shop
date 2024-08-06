package com.backend.library.backend.services.implementations;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.server.ResponseStatusException;
import com.backend.library.backend.dto.books.BookListDTO;
import com.backend.library.backend.handlers.exceptions.ConflictException;
import com.backend.library.backend.mappers.implementations.BookMapperImpl;
import com.backend.library.backend.mappers.interfaces.BookMapper;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.FavoriteBook;
import com.backend.library.backend.persistence.repositories.BookRepository;
import com.backend.library.backend.persistence.repositories.FavoriteBookRepository;
import com.backend.library.backend.services.interfaces.IFavoriteBookService;
import com.backend.library.backend.utils.KeycloakProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FavoriteBookServiceImpl implements IFavoriteBookService {

    private FavoriteBookRepository favoriteBookRepository;
    private BookRepository bookRepository;

    public FavoriteBookServiceImpl(FavoriteBookRepository favoriteBookRepository, BookMapperImpl bookMapper,
            BookRepository bookRepository) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves a list of favorite books by user id.
     *
     * @param request the HTTP request object
     * @param pageable the page request criteria
     * @return the list of favorite books as a page of book DTOs
     * @throws InternalServerError if an error occurs while retrieving the data
     */
    @Override
    public ResponseEntity<Page<BookListDTO>> getFavoriteBooksByUserId(HttpServletRequest request, Pageable pageable) {
        try {
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Retrieve the list of favorite books for the given user id and page request criteria
            Page<FavoriteBook> favoriteBooks = favoriteBookRepository.findAllByUserId(userId, pageable);

            // Convert the list of favorite books to a list of book DTOs
            Page<BookListDTO> bookListDTOs = favoriteBooks.map(this::toBooksListDTOs);

            // Return the list of book DTOs as a page response
            return ResponseEntity.status(HttpStatus.OK).body(bookListDTOs);

        } catch (InternalServerError e) {
            log.error("Error with the following message: {}", e.getMessage());

            // Return an internal server error response if an error occurs while retrieving the data
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Adds a favorite book to a user's list of favorite books
     *
     * @param request the HTTP request object
     * @param bookIsbn the ISBN of the book to add as a favorite
     * @return a ResponseEntity containing a BookListDTO object, or a ConflictException
     * if the book is already a favorite
     */
    @Override
    public ResponseEntity<BookListDTO> addFavoriteBook(HttpServletRequest request, String bookIsbn) {
        try {

            // Get the user id from the given HTTP request
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Retrieve the list of favorite books for the given user id
            Optional<FavoriteBook> schrodingerFavoriteBook = favoriteBookRepository
                    .findByUserIdAndBookIsbn(userId, bookIsbn);

            // Throw a ConflictException if the book is already a favorite
            if (schrodingerFavoriteBook.isPresent()) {
                throw new ConflictException("Favorite book already exists");
            }

            // Retrieve the book with the given ISBN from the database
            Book book = bookRepository.findById(bookIsbn)
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Not found any book with ISBN provided "));

            // Save the favorite book to the database
            favoriteBookRepository.save(
                    FavoriteBook.builder()
                            .book(book)
                            .userId(userId)
                            .build());

            // Return the book as a BookListDTO object with a 201 status
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(BookMapper.toListDTO(book));

        } catch (InternalServerError e) {
            // Log the error and return an internal server error response if an error occurs
            log.error("Error with the following message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    /**
     * Deletes a favorite book from the database
     * 
     * @param request the HTTP request containing the user id and book isbn
     * @param bookIsbn the ISBN of the book to be deleted from the user's favorites
     * @return a ResponseEntity containing a JSON object with a message indicating whether the book was deleted successfully or not
     * @throws ResponseStatusException if no favorite book is found with the given user id and book isbn
     */
    @Override
    public ResponseEntity<String> deleteFavoriteBook(HttpServletRequest request, String bookIsbn) {

        try {

            // Get the user id from the given HTTP request
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Retrieve the favorite book to be deleted from the database
            // If the favorite book is not found, throw a ResponseStatusException
            FavoriteBook favoriteBook = favoriteBookRepository
                    .findByUserIdAndBookIsbn(userId, bookIsbn)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Not found any favorite book with information provided "));

            // Delete the favorite book from the database
            favoriteBookRepository.delete(favoriteBook);
            // Return a ResponseEntity with a JSON object indicating that the book was deleted successfully
            return ResponseEntity.status(HttpStatus.OK)
                    .body("{\"message\": \"Book deleted successfully\"}");

        } catch (InternalServerError e) {
            // Log the error and return an internal server error response if an error occurs
            log.error("Error with the following message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


     /**
     * Converts a favorite book entity to a book list DTO.
     * 
     * @param favoriteBook the favorite book entity to be converted
     * @return the book list DTO converted from the given favorite book entity
     */
    private BookListDTO toBooksListDTOs(FavoriteBook favoriteBook) {
        return BookMapper.toListDTO(favoriteBook.getBook());
    }

}
