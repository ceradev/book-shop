package com.backend.library.backend.mappers.interfaces;

import java.util.List;
import com.backend.library.backend.dto.addresses.AddressDTO;
import com.backend.library.backend.persistence.entities.Address;
import com.backend.library.backend.persistence.entities.UserAddress;

public interface IUserAddressMapper {

    AddressDTO toAddressDTO(Address address);
    AddressDTO toAddressDTO(UserAddress address);
    List<AddressDTO> toListAddressDTO(List<Address> addresses);
    List<AddressDTO> toListUserAddressDTO(List<UserAddress> addresses);
    UserAddress toUserAddress(String userId, AddressDTO addressDTO);
}