package com.backend.library.backend.mappers.interfaces;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.review.ReviewBookDTO;
import com.backend.library.backend.dto.review.ReviewDTO;
import com.backend.library.backend.persistence.entities.Review;

public interface ReviewMapper {
    static ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .user(UserMapper.fromKeycloak(review.getUserId()))
                .rating(review.getRating())
                .comment(review.getComment())
                .isPurchased(review.getIsPurchased())
                .build();
    }

    static ReviewBookDTO toDTOBook(Review review) {
        return ReviewBookDTO.builder()
                .id(review.getId())
                .user(UserMapper.fromKeycloak(review.getUserId()))
                .rating(review.getRating())
                .comment(review.getComment())
                .isPurchased(review.getIsPurchased())
                .isbn(review.getBook().getIsbn())
                .title(review.getBook().getTitle())
                .author(new AuthorDetailsDTO(review.getBook().getAuthor().getId(),
                        review.getBook().getAuthor().getFirstName(), review.getBook().getAuthor().getLastName(),
                        review.getBook().getAuthor().getBiography(), review.getBook().getAuthor().getPortrait()))
                .build();
    }

    static List<ReviewBookDTO> toDTOBook(Set<Review> reviews) {
        return reviews.stream().map(ReviewMapper::toDTOBook).collect(Collectors.toList());
    }

    static List<ReviewDTO> toDTO(Set<Review> reviews) {
        return reviews.stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
    }
}
