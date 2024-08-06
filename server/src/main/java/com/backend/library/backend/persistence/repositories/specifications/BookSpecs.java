package com.backend.library.backend.persistence.repositories.specifications;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Genre;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BookSpecs {

    public static Specification<Book> hasGenres(List<String> genres) {
        // return (root, query, criteriaBuilder) -> {
        //     if (genres == null || genres.isEmpty()) {
        //         return null;
        //     }
        //     List<Predicate> genrePredicates = new ArrayList<>();
        //     for (String genre : genres) {
        //         genrePredicates.add(criteriaBuilder.isMember(genre, root.get("genres").get("name")));
                
        //     }
        //     return criteriaBuilder.or(genrePredicates.toArray(new Predicate[0]));
        // };
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (genres == null || genres.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // enlace con los géneros
            Join<Book, Genre> genreJoin = root.join("genres", JoinType.INNER);

            // crear la query con or
            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(criteriaBuilder.lower(genreJoin.get("name")));
            for (String genre : genres) {
                inClause.value(genre.toLowerCase());
            }

            return criteriaBuilder.and(inClause);
        };
    }

    public static Specification<Book> hasAuthorFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return null;
            }
            Join<Book, Author> authorJoin = root.join("author");
            return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("firstName")), "%"+firstName.toLowerCase()+"%");
        };
    }

    public static Specification<Book> hasAuthorLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return null;
            }
            Join<Book, Author> authorJoin = root.join("author");
            return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("lastName")),"%"+ lastName.toLowerCase()+"%");
        };
    }

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, criteriaBuilder) -> {
            if (isbn == null || isbn.isEmpty()) {
                return criteriaBuilder.conjunction();
                //return null;
            }
            //return criteriaBuilder.equal(root.get("isbn"), isbn);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("isbn")), "%" + isbn.toLowerCase() + "%");
        };
    }

    public static Specification<Book> hasEditorial(String editorial) {
        return (root, query, criteriaBuilder) -> {
            if (editorial == null || editorial.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("editorial")), "%"+editorial.toLowerCase()+"%");
        };
    }

    public static Specification<Book> hasSynopsis(String synopsis) {
        return (root, query, criteriaBuilder) -> {
            if (synopsis == null || synopsis.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("synopsis")),
                    "%" + synopsis.toLowerCase() + "%");
        };
    }

    public static Specification<Book> hasEdition(String edition) {
        return (root, query, criteriaBuilder) -> {
            if (edition == null || edition.isEmpty()) {
                return null;
            }
            
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("edition")), "%" + edition.toLowerCase() + "%");
        };
    }

    // public static Specification<Book> hasMinPrice(Double minPrice) {
    // return (root, query, criteriaBuilder) -> {
    // if (minPrice == null) {
    // return null;
    // }
    // return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    // };
    // }

    // public static Specification<Book> hasMaxPrice(Double maxPrice) {
    // return (root, query, criteriaBuilder) -> {
    // if (maxPrice == null) {
    // return null;
    // }
    // return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    // };
    // }

    public static Specification<Book> hasPublishedDate(LocalDateTime publishedDate) {
        return (root, query, criteriaBuilder) -> {
            if (publishedDate == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("publishedDate"), publishedDate);
        };
    }

    public static Specification<Book> hasPriceInRange(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }
            Predicate pricePredicate = null;
            if (minPrice != null) {
                pricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if (maxPrice != null) {
                Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
                pricePredicate = (pricePredicate != null) ? criteriaBuilder.and(pricePredicate, maxPricePredicate)
                        : maxPricePredicate;
            }
            return pricePredicate;
        };
    }

    public static String normalizeString(String input) {
        return input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD);
    }

}
