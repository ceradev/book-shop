package com.backend.library.backend.persistence.repositories;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.backend.library.backend.persistence.entities.Sale;

@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findAllByClientId(String clientId, Pageable pageable);

    Optional<Sale> findByClientIdAndBookIsbn(String clientId, String bookISBN);

    long countByBookIsbn(String bookISBN);
    Optional <Sale> findAllByBookIsbn(String bookISBN);
    Optional <Sale> deleteAllByBookIsbn(String bookISBN);

    @Query("SELECT s FROM Sale s WHERE s.salerId = :id OR s.clientId = :id")
    Set<Sale> findAllBySellerIdOrClientId(@Param("id") String id);

}
