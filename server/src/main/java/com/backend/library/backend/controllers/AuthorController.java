package com.backend.library.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.authors.RequestAuthorDTO;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.services.interfaces.IAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/authors")
@Tag(name = "Author Controller", description = "Controller for managing authors")
public class AuthorController {

    private IAuthorService authorService;

    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "Obtain all authors", description = "Obtain all existing authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Iterable.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> getAll() {
        List<AuthorDetailsDTO> dto = authorService.getAll();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtain author by id", description = "get author by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> details(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthor(id));
    }

    @PostMapping
    @Operation(summary = "Create new author", description = "Create a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> newAuthor(@Valid @RequestBody RequestAuthorDTO author) {
        return ResponseEntity.ok(authorService.add(author));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing author", description = "Update an existing author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RequestAuthorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @Valid @RequestBody RequestAuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.update(authorDTO, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing author", description = "Deleting an existing author from app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
