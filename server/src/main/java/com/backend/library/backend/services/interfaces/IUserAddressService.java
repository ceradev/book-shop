package com.backend.library.backend.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.backend.library.backend.dto.addresses.AddressDTO;
import com.backend.library.backend.dto.addresses.CreateRequestAddress;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserAddressService {

    public ResponseEntity<List<AddressDTO>> getAllAddressByUserId(String userId);

    public ResponseEntity<List<AddressDTO>> getMyAddresses(HttpServletRequest request);

    public ResponseEntity<AddressDTO> createAddress(HttpServletRequest request, CreateRequestAddress addressDTO);

    public ResponseEntity<AddressDTO> updateAddress(HttpServletRequest request, AddressDTO addressDTO);

    public ResponseEntity<AddressDTO> deleteAddress(HttpServletRequest request, Long addressId);

}
