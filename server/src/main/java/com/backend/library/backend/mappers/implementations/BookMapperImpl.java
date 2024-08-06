package com.backend.library.backend.mappers.implementations;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import com.backend.library.backend.dto.books.BookDetailsDTO;
import com.backend.library.backend.mappers.interfaces.AuthorMapper;
import com.backend.library.backend.mappers.interfaces.BookMapper;
import com.backend.library.backend.mappers.interfaces.GenreMapper;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.persistence.entities.Book;

@Service
public class BookMapperImpl implements BookMapper {

    private GenreMapperImpl genreMapper;

    public BookMapperImpl(GenreMapperImpl genreMapper) {
        this.genreMapper = genreMapper;
    }

    public Book toBook(BookDetailsDTO bookReq, Author author, UserRepresentation editorial) {
        return Book.builder()
                .isbn(bookReq.isbn())
                .cover(bookReq.cover())
                .title(bookReq.title())
                .author(author)
                .editorial(editorial.getId())
                .synopsis(bookReq.synopsis())
                .edition(bookReq.edition())
                .price(bookReq.price())
                .genres(genreMapper.toGenre(bookReq.genres()))
                .build();
    }

    public BookDetailsDTO toDetailsDTO(Book book) {
        return BookDetailsDTO.builder()
                .isbn(book.getIsbn())
                .cover(book.getCover())
                .title(book.getTitle())
                .author(AuthorMapper.toList(book.getAuthor()))
                .status(book.getStatus())
                .stock(book.getStock())
                .editorial(book.getEditorial())
                .synopsis(book.getSynopsis())
                .edition(book.getEdition())
                .price(book.getPrice())                
                .reviewMean(book.getReviewMean())
                .publishDate(book.getPublishDate())
                .nReviews(book.getNReviews())
                .genres(GenreMapper.toDTO(book.getGenres()))
                .build();
    }
}
