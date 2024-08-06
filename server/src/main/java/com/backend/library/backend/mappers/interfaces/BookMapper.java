package com.backend.library.backend.mappers.interfaces;

import java.util.List;
import java.util.stream.Collectors;
import org.keycloak.representations.idm.UserRepresentation;
import com.backend.library.backend.dto.books.BookDetailsDTO;
import com.backend.library.backend.dto.books.BookListDTO;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.persistence.entities.Book;

public interface BookMapper {

    BookDetailsDTO toDetailsDTO(Book book);

    static BookListDTO toListDTO(Book book){
            return BookListDTO.builder()
                    .isbn(book.getIsbn())
                    .stock(book.getStock())
                    .cover(book.getCover())
                    .title(book.getTitle())
                    .status(book.getStatus())
                    .nReviews(book.getNReviews())
                    .reviewMean(book.getReviewMean())
                    .genres(GenreMapper.toDTO(book.getGenres()))
                    .author(AuthorMapper.toList(book.getAuthor()))
                    .editorial(book.getEditorial())
                    .price(book.getPrice())
                    .build();
    }

    static List<BookListDTO> toListDTO(List<Book> books) {
        return books.stream().map(BookMapper::toListDTO).collect(Collectors.toList());
    }

    Book toBook(BookDetailsDTO bookDTO, Author author, UserRepresentation editorial);

}
