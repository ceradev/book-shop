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

import com.backend.library.backend.dto.RequestUserIdBookIsbn;
import com.backend.library.backend.dto.review.CreateReviewDTO;
import com.backend.library.backend.dto.review.ReviewBookDTO;
import com.backend.library.backend.dto.review.ReviewDTO;
import com.backend.library.backend.services.implementations.ReviewServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/v1/reviews")
@Tag(name = "Review Controller", description = "Controller for managing reviews")
public class ReviewController {
    private ReviewServiceImpl reviewService;
    

    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{bookIsbn}")
    @Operation(summary = "Obtain reviews by book ISBN", description = "Obtain reviews providing the book ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<List<ReviewDTO>> getReviewsByBook(@PathVariable String bookIsbn) {
        return reviewService.getReviewsByBook(bookIsbn);
    }

    @PostMapping
    @Operation(summary = "Create review", description = "Create review in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
            public ResponseEntity<ReviewDTO> addReview(@RequestBody CreateReviewDTO requestReview, HttpServletRequest request ) {
                return reviewService.addReview(requestReview, request);
            }


    @DeleteMapping("/{bookIsbn}")
    @Operation(summary = "Delete book from favorite", description = "Delete review by user ID and book ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
            public ResponseEntity<ReviewDTO> deleteReview(@PathVariable String bookIsbn, HttpServletRequest request) {
                return reviewService.deleteReview(bookIsbn, request);
            }



    //MODIFICACION DE LA REVIEW
    @PutMapping()
    @Operation(summary = "Modify review", description = "Modify review in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<ReviewDTO> modReview(@RequestBody CreateReviewDTO requestReview, HttpServletRequest request){

        return reviewService.modReview(requestReview, request);
    }

    @DeleteMapping("admin/{id}")
    @Operation(summary = "Delete review", description = "Delete review in the database by id, only admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<ReviewDTO> deleteReviewByID(@PathVariable Long id){
        return reviewService.deleteReviewById(id);
    }


    @GetMapping()
    @Operation(summary = "Get reviews by user", description = "Get all user reviews")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<List<ReviewBookDTO>> getReviewByUser(HttpServletRequest request) {
        return reviewService.getReviewsByUser(request);
    }

    @GetMapping("/seller")
    @Operation(summary = "Get reviews by user", description = "Get all user reviews")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    public ResponseEntity<List<ReviewBookDTO>> getReviewBySeller(HttpServletRequest request) {
        return reviewService.getReviewBySeller(request);
    }
    

}
