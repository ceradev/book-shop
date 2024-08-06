package com.backend.library.backend.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.backend.library.backend.dto.books.BookListDTO;
import jakarta.servlet.http.HttpServletRequest;
public interface IFavoriteBookService {
    public ResponseEntity<Page<BookListDTO>> getFavoriteBooksByUserId(HttpServletRequest request,Pageable pageable);

    public ResponseEntity<BookListDTO> addFavoriteBook(HttpServletRequest request, String bookIsbn);

    public ResponseEntity<String> deleteFavoriteBook(HttpServletRequest request, String bookIsbn);
    
}
