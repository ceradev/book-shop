package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.library.backend.dto.users.UserUpdateRequest;
import com.backend.library.backend.services.interfaces.IKeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Keycloak Controller", description = "Controller for managing users in Keycloak")
public class KeycloakController {

        private IKeycloakService keycloakService;

        public KeycloakController(IKeycloakService keycloakService) {
                this.keycloakService = keycloakService;
        }

        @Operation(summary = "Returns all users", responses = {
                        @ApiResponse(responseCode = "200", description = "Users returned")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Not authorized to perform this action"),
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @GetMapping
        @PreAuthorize("hasRole('admin_client_role')")
        public ResponseEntity<?> findAllUsers() {
                return keycloakService.findAllUsers();
        }

        @Operation(summary = "Returns a user by ID", responses = {
                        @ApiResponse(responseCode = "200", description = "User returned")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Not authorized to perform this action"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @GetMapping("/{userId}")
        public ResponseEntity<?> findUserById(@PathVariable String userId) {
                return keycloakService.searchUserById(userId);
        }

        @Operation(summary = "Returns yourself", responses = {
                        @ApiResponse(responseCode = "200", description = "User returned")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Not authorized to perform this action"),
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @GetMapping("/me")
        public ResponseEntity<?> searchYourself(HttpServletRequest request) {
                return keycloakService.searchYourself(request);
        }

        @Operation(summary = "Returns a user by username", responses = {
                        @ApiResponse(responseCode = "200", description = "User returned")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Not authorized to perform this action"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @GetMapping("/username/{username}")
        public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
                return keycloakService.searchUserByUsername(username);
        }

        @Operation(summary = "Updates a user", responses = {
                        @ApiResponse(responseCode = "200", description = "User updated")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Not authorized to perform this action"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @PutMapping("/{userId}")
        public ResponseEntity<?> updateUser(@PathVariable String userId,
                        @Valid @RequestBody UserUpdateRequest userDTO) {
                return keycloakService.updateUser(userId, userDTO);
        }

        @Operation(summary = "Updates yourself", responses = {
                        @ApiResponse(responseCode = "200", description = "User updated")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Not authorized to perform this action"),
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @PutMapping("/me")
        public ResponseEntity<?> updateYourself(HttpServletRequest request, @Valid @RequestBody UserUpdateRequest userDTO) {
                return keycloakService.updateYourself(request, userDTO);
        }
}
