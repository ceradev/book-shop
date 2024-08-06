package com.backend.library.backend.mappers.implementations;

import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import com.backend.library.backend.dto.users.UserDTO;
import com.backend.library.backend.dto.users.UserDetailsDTO;
import com.backend.library.backend.mappers.interfaces.UserMapper;
import com.backend.library.backend.utils.KeycloakProvider;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserRepresentation toKeycloak(UserDTO user) {

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(user.username());
        userRepresentation.setFirstName(user.name());
        userRepresentation.setLastName(user.surname());
        userRepresentation.setEmail(user.email());
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }

    @Override
    public UserDetailsDTO fromDetailsKeycloak(String userId) {
        UserRepresentation user = KeycloakProvider.getUserResource().get(userId).toRepresentation();
        List<UserRepresentation> admins = KeycloakProvider.getUsersFromRoles("admin");
        List<UserRepresentation> sellers = KeycloakProvider.getUsersFromRoles("seller");
        List<UserRepresentation> clients = KeycloakProvider.getUsersFromRoles("client");

        UserDetailsDTO detailsDTO = null;
        if (admins.stream().anyMatch(u -> u.getId().equals(userId))) {
            detailsDTO = UserDetailsDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getFirstName())
                    .surname(user.getLastName())
                    .email(user.getEmail())
                    .role("ADMIN")
                    .build();
        } else if (sellers.stream().anyMatch(u -> u.getId().equals(userId))) {
            detailsDTO = UserDetailsDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getFirstName())
                    .surname(user.getLastName())
                    .email(user.getEmail())
                    .role("SELLER")
                    .build();
        } else if (clients.stream().anyMatch(u -> u.getId().equals(userId))) {
            detailsDTO = UserDetailsDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getFirstName())
                    .surname(user.getLastName())
                    .email(user.getEmail())
                    .role("CLIENT")
                    .build();
        }

        return detailsDTO;
    }

    @Override
    public List<UserDetailsDTO> fromListDetailsKeycloak(List<UserRepresentation> users) {
        return users.stream().map(user -> fromDetailsKeycloak(user.getId())).toList();
    }
}
