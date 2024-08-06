package com.backend.library.backend.mappers.interfaces;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import com.backend.library.backend.dto.users.UserDTO;
import com.backend.library.backend.dto.users.UserDetailsDTO;
import com.backend.library.backend.utils.KeycloakProvider;

public interface UserMapper { 

    UserRepresentation toKeycloak(UserDTO user);
    static UserDTO fromKeycloak(String userId) {

       UserRepresentation user = KeycloakProvider.getUserResource().get(userId).toRepresentation();

       return UserDTO.builder()
       .id(user.getId())
       .username(user.getUsername())
       .name(user.getFirstName())
       .surname(user.getLastName())
       .email(user.getEmail())
       .build();
    }

    static UserDTO fromKeycloak(UserRepresentation user) {
        return UserDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .name(user.getFirstName())
        .surname(user.getLastName())
        .email(user.getEmail())
        .build();
    }

    UserDetailsDTO fromDetailsKeycloak(String userId);
    List<UserDetailsDTO> fromListDetailsKeycloak(List<UserRepresentation> users);
    
}
