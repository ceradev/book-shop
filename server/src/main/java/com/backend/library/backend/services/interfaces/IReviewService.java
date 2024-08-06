package com.backend.library.backend.services.interfaces;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.backend.library.backend.dto.RequestUserIdBookIsbn;
import com.backend.library.backend.dto.review.CreateReviewDTO;
import com.backend.library.backend.dto.review.ReviewBookDTO;
import com.backend.library.backend.dto.review.ReviewDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface IReviewService {
    public ResponseEntity<List<ReviewDTO>> getReviewsByBook(String bookIsbn);

    public ResponseEntity<ReviewDTO> deleteReview(String isbn, HttpServletRequest request);

    public ResponseEntity<ReviewDTO> addReview(CreateReviewDTO createReviewDTO, HttpServletRequest request);

    public ResponseEntity<ReviewDTO> modReview(CreateReviewDTO createReviewDTO, HttpServletRequest request);

    public ResponseEntity<ReviewDTO> deleteReviewById(Long id);

    public ResponseEntity<List<ReviewBookDTO>> getReviewsByUser(HttpServletRequest request);

    public ResponseEntity<List<ReviewBookDTO>> getReviewBySeller(HttpServletRequest request);

}
