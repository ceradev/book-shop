package com.backend.library.backend.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.backend.library.backend.persistence.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByBookIsbn(String bookISBN);
    Optional<Review> findByBookIsbnAndUserId(String bookISBN, String userId);
    Optional<List<Review>> findByUserId(String userId);

    @Query("SELECT r FROM Review r INNER JOIN Book b ON r.book.isbn = b.isbn WHERE b.sellerId = :sellerId")
    List<Review>findReviewsSellerBooks(@Param("sellerId") String sellerId);

    List<Review>findAllByBookSellerId(String sellerId);

    Optional<Review>findAllByBookIsbn(String bookISBN);

    List<Review>deleteAllByBookIsbn(String bookISBN);

}
