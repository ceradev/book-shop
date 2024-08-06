package com.backend.library.backend.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.library.backend.persistence.entities.FavoriteBook;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long>  {
    Page<FavoriteBook> findAllByUserId(String userId,Pageable pageable);
    Optional<FavoriteBook> findAllByBookIsbn(String bookISBN);
    Optional<FavoriteBook> findByUserIdAndBookIsbn(String userId, String bookISBN);
    List<FavoriteBook> deleteAllByBookIsbn(String bookISBN);
}
