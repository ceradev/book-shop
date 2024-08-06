package com.backend.library.backend.persistence.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.backend.library.backend.persistence.entities.UserAddress;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findAllByUserId(String userId);

    Optional<UserAddress> findByAddressIdAndUserId(Long addressId, String userId);
    Optional<UserAddress> findByIdAndUserId(Long id, String userId);


    @Query("SELECT ua FROM UserAddress ua WHERE ua.id = :id")
    Optional<UserAddress> findUserAddressByAddressId(@Param("id") Long id);
}
