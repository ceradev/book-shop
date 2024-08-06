package com.backend.library.backend.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.library.backend.persistence.entities.OrderEntity;

@Repository
public interface OrderEntityRepository extends CrudRepository<OrderEntity, Long> {

    OrderEntity findByPaypalOrderId(String paypalOrderId);

    OrderEntity findByCartId(Long cartId);
}
