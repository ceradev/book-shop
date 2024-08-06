package com.backend.library.backend.mappers.implementations;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.backend.library.backend.dto.addresses.AddressDTO;
import com.backend.library.backend.mappers.interfaces.IUserAddressMapper;
import com.backend.library.backend.persistence.entities.Address;
import com.backend.library.backend.persistence.entities.UserAddress;

@Service
public class UserAddressMapperImpl implements IUserAddressMapper {

    @Override
    public AddressDTO toAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .road(address.getRoad())
                .num(address.getNum())
                .build();
    }

    @Override
    public AddressDTO toAddressDTO(UserAddress address) {
        return AddressDTO.builder()
                .id(address.getAddress().getId())
                .country(address.getAddress().getCountry())
                .city(address.getAddress().getCity())
                .postalCode(address.getAddress().getPostalCode())
                .road(address.getAddress().getRoad())
                .num(address.getAddress().getNum())
                .build();
    }



    @Override
    public UserAddress toUserAddress(String userId, AddressDTO addressDTO) {
        return UserAddress.builder()
                .userId(userId)
                .address(Address.builder()
                        .id(addressDTO.id())
                        .country(addressDTO.country())
                        .city(addressDTO.city())
                        .postalCode(addressDTO.postalCode())
                        .road(addressDTO.road())
                        .num(addressDTO.num())
                        .build())
                .build();
    }

    @Override
    public List<AddressDTO> toListAddressDTO(List<Address> addresses) {
        return addresses.stream()
                .map(this::toAddressDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> toListUserAddressDTO(List<UserAddress> addresses) {
        return addresses.stream()
                .map(this::toAddressDTO).collect(Collectors.toList());
    }
}
