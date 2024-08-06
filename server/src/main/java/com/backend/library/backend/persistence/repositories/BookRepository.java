package com.backend.library.backend.persistence.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.enums.BookStatus;

@Repository
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b FROM Book b JOIN b.author a WHERE " +
            "(LOWER(b.edition) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(b.synopsis) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(b.editorial) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT('%', :q, '%')))" +
            "AND b.status = :status")
    Page<Book> findAllByQueryString(@Param("q") String q, @Param("status") BookStatus status, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.sellerId = :sellerId AND b.status = :status")
    Page<Book> findAllBySellerIdAndStatus(@Param("status") BookStatus status, @Param("sellerId") String sellerId,
            Pageable pageable);

    Book findByIsbn(String isbn);
}
