package com.backend.library.backend.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.library.backend.persistence.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
