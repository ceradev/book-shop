package com.backend.library.backend.services.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.backend.library.backend.dto.books.BookDetailsDTO;
import com.backend.library.backend.dto.books.BookSearchCriteriaDTO;
import com.backend.library.backend.dto.books.UpdateBookRequest;
import com.backend.library.backend.persistence.entities.enums.BookStatus;

import jakarta.servlet.http.HttpServletRequest;

public interface IBookservice {
  
    public ResponseEntity<?> deleteBook(String isbn);

    public ResponseEntity<?> getAllBooks();

    public ResponseEntity<?> getBooks(BookSearchCriteriaDTO params, Pageable pageable, String status);

    public ResponseEntity<?> getBooksBySeller(BookStatus status, HttpServletRequest request, Pageable pageable);

    public ResponseEntity<?> getBooksByQuery(String query, BookStatus status, Pageable pageable);

    public ResponseEntity<?> getBookByISBN(String bookISBN);

    public ResponseEntity<String> saveBook(String bookJson, MultipartFile image, HttpServletRequest request);

    public ResponseEntity<BookDetailsDTO> updateBook(UpdateBookRequest book, String isbn);

    public ResponseEntity<BookDetailsDTO> changeBookStatus(String status, String isbn);
  
}
