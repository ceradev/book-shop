package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.library.backend.dto.auth.AuthCreateUserRequest;
import com.backend.library.backend.dto.auth.AuthLoginRequest;
import com.backend.library.backend.dto.auth.AuthResetPasswordRequest;
import com.backend.library.backend.services.interfaces.IKeycloakService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Controller for authentication and authorization operations")
public class AuthController {

        private IKeycloakService keycloakService;

        public AuthController(IKeycloakService keycloakService) {
                this.keycloakService = keycloakService;
        }

        @Operation(summary = "Login user into the system", description = "It receives the username and password of a user and returns an access token if the user exists and the password is correct", responses = {
                        @ApiResponse(responseCode = "200", description = "Access token returned"),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody AuthLoginRequest loginRequest) {
                return keycloakService.loginUser(loginRequest);
        }

        @Operation(summary = "Register a new user", description = "It receives the username, password, email and role of a new user and creates it in the system", responses = {
                        @ApiResponse(responseCode = "201", description = "User created"),
                        @ApiResponse(responseCode = "400", description = "Bad request (missing or incorrect data)")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody AuthCreateUserRequest createUserRequest) {
                return keycloakService.createUser(createUserRequest);
        }

        @Operation(summary = "Reset password", description = "It receives the username and new password of a user and updates it in the system", responses = {
                        @ApiResponse(responseCode = "200", description = "Password updated"),
                        @ApiResponse(responseCode = "400", description = "Bad request (missing or incorrect data)")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @PostMapping("/reset-password")
        public ResponseEntity<?> resetPassword(HttpServletRequest request,
                        @RequestBody AuthResetPasswordRequest resetPasswordRequest) {
                return keycloakService.resetPassword(request, resetPasswordRequest);
        }

        @Operation(summary = "Logout user", description = "It logs out the user from the system", responses = {
                        @ApiResponse(responseCode = "200", description = "User logged out"),
                        @ApiResponse(responseCode = "400", description = "Bad request (missing or incorrect data)")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request) {
                return keycloakService.logout(request);
        }

        @Operation(summary = "Authenticate user", description = "It authenticates the user and returns an access token if the user exists and the password is correct", responses = {
                        @ApiResponse(responseCode = "200", description = "Access token returned"),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials")
        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "500", description = "Unknown error from server")
        })
        @GetMapping("/me")
        public ResponseEntity<?> authenticateUser(HttpServletRequest request) {
                return keycloakService.authenticateUser(request);
        }
}
