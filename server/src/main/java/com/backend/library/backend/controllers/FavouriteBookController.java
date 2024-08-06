package com.backend.library.backend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.library.backend.converters.TempPageConverter;
import com.backend.library.backend.dto.books.BookListDTO;
import com.backend.library.backend.dto.books.TempPageRequest;
import com.backend.library.backend.services.interfaces.IFavoriteBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("api/v1/favourites")
@Tag(name = "Favorite Books Controller", description = "Controller for managing favorite books")
public class FavouriteBookController {
    
    private IFavoriteBookService favoriteBookService;

    public FavouriteBookController(IFavoriteBookService favoriteBookService) {
        this.favoriteBookService = favoriteBookService;
    }


    @GetMapping
    @Operation(summary = "Obtain favorite books by user ID", description = "Obtain favorite books of a user, uses ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<Page<BookListDTO>> getFavoriteBooks(HttpServletRequest request, TempPageRequest pages) {
        return favoriteBookService.getFavoriteBooksByUserId(request, TempPageConverter.toPageRequest(pages));
    }

    @PostMapping("/{bookIsbn}")
    @Operation(summary = "Create favorite book", description = "Obtain favorite books of a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BookListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
            public ResponseEntity<BookListDTO> addFavoriteBook(HttpServletRequest request, @PathVariable String bookIsbn) {
                return favoriteBookService.addFavoriteBook(request, bookIsbn);
            }


    @DeleteMapping("/{bookIsbn}")
    @Operation(summary = "Delete book from favorite", description = "Delete book from favorite by user ID and book ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
            public ResponseEntity<String> deleteFavoriteBook(HttpServletRequest request, @PathVariable String bookIsbn) {
                return favoriteBookService.deleteFavoriteBook(request, bookIsbn);
            }
}
