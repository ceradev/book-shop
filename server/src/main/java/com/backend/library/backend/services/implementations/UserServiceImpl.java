package com.backend.library.backend.services.implementations;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.users.UserDTO;
import com.backend.library.backend.handlers.exceptions.UnexpectedException;
import com.backend.library.backend.mappers.interfaces.UserMapper;
import com.backend.library.backend.utils.KeycloakProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl {

    public UserDTO getUserDTO() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserDTO user = UserMapper
                    .fromKeycloak(KeycloakProvider.getRealmResource().users().search(username).getFirst());
            if (user != null) {
                return user;
            }
            log.error("No user found for username " + username + " in keycloak");
            throw new UnexpectedException("Error retrieving the user");
        } catch (Exception e) {
            log.error("Error retrieving the user from keycloak", e.getMessage());
            return null;
        }
    }
}
