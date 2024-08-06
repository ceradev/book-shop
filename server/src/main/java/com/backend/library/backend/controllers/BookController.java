package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.library.backend.converters.TempPageConverter;
import com.backend.library.backend.dto.books.BookDetailsDTO;
import com.backend.library.backend.dto.books.BookListDTO;
import com.backend.library.backend.dto.books.BookSearchCriteriaDTO;
import com.backend.library.backend.dto.books.TempPageRequest;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.enums.BookStatus;
import com.backend.library.backend.dto.books.UpdateBookRequest;
import com.backend.library.backend.services.interfaces.IBookservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/books")
@Tag(name = "Book Controller", description = "Controller for managing books")
public class BookController {

    private IBookservice bookService;

    public BookController(IBookservice bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Obtain all books", description = "Obtain all books per page and how many do you want in the page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> getBooksPerPage(TempPageRequest pages) {
        return bookService.getBooks(null, TempPageConverter.toPageRequest(pages), "PUBLISHED");
    }

    @GetMapping("/pending")
    @Operation(summary = "Obtain all pending books for admins", description = "Obtain all books with status 'PENDING' with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> getBooksPending(TempPageRequest pages) {
        return bookService.getBooks(null, TempPageConverter.toPageRequest(pages), "PENDING");
    }

    @PostMapping("/search")
    @Operation(summary = "Search books", description = "Search books in the database by filter criteria and you can filter by title, genre and author or all of them")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> searchBooks(@RequestBody BookSearchCriteriaDTO criteriaDTO) {
        return bookService.getBooks(criteriaDTO,
                TempPageConverter.toPageRequest(criteriaDTO.getPageable()), "PUBLISHED");
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books in the  database by query string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Iterable.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> searchBooksByQueryString(@RequestParam String q, BookStatus status,
            TempPageRequest pages) {
        return bookService.getBooksByQuery(q, status, TempPageConverter.toPageRequest(pages));
    }

    @GetMapping("/seller")
    @Operation(summary = "Obtain books by seller", description = "Obtain books by seller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> getBooksBySeller(BookStatus status, TempPageRequest pages, HttpServletRequest request) {
        return bookService.getBooksBySeller(status, request, TempPageConverter.toPageRequest(pages));
    }

    @GetMapping("/{isbn}")
    @Operation(summary = "Obtain book by ISBN", description = "Obtain book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByISBN(isbn);
    }

    @PostMapping
    @Operation(summary = "Create book", description = "Create book in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })

    public ResponseEntity<?> createBook(@Valid @RequestParam("book") String bookJson,
            @RequestParam("image") MultipartFile image, HttpServletRequest request) {
        return bookService.saveBook(bookJson, image, request);
    }

    @PutMapping("/{isbn}")
    @Operation(summary = "Update book", description = "A vendor can update a rejected or pending book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookDetailsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<BookDetailsDTO> updateBook(@Valid @RequestBody UpdateBookRequest newData,
            @PathVariable String isbn) {
        return bookService.updateBook(newData, isbn);
    }

    @PutMapping("status/{isbn}/{status}")
    @Operation(summary = "Change book status", description = "An admin can change the book status to rejected or published")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookDetailsDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<BookDetailsDTO> changeStatus(@PathVariable String isbn, @PathVariable String status) {
        return bookService.changeBookStatus(status, isbn);
    }

    @DeleteMapping("/{isbn}")
    @Operation(summary = "Delete book", description = "Delete book from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        return bookService.deleteBook(isbn);
    }
}
