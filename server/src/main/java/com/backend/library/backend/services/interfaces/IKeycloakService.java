package com.backend.library.backend.services.interfaces;

import org.springframework.http.ResponseEntity;
import com.backend.library.backend.dto.auth.AuthCreateUserRequest;
import com.backend.library.backend.dto.auth.AuthLoginRequest;
import com.backend.library.backend.dto.auth.AuthResetPasswordRequest;
import com.backend.library.backend.dto.users.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface IKeycloakService {
    
    ResponseEntity<?> findAllUsers();
    ResponseEntity<?> searchUserById(String userId);
    ResponseEntity<?> searchYourself(HttpServletRequest request);
    ResponseEntity<?> searchUserByUsername(String username);
    ResponseEntity<?> createUser(@Valid AuthCreateUserRequest userDTO);
    ResponseEntity<?> loginUser(AuthLoginRequest loginRequest);
    ResponseEntity<?> updateUser(String userId , @Valid UserUpdateRequest userDetailsDTO);
    ResponseEntity<?> updateYourself(HttpServletRequest request, @Valid UserUpdateRequest userDTO);
    ResponseEntity<?> authenticateUser(HttpServletRequest request);
    ResponseEntity<?> resetPassword(HttpServletRequest request, AuthResetPasswordRequest resetPasswordRequest);
    ResponseEntity<?> logout(HttpServletRequest request);
}
