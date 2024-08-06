package com.backend.library.backend.services.implementations;

import java.util.List;
import java.util.Objects;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.backend.library.backend.dto.auth.AuthCreateUserRequest;
import com.backend.library.backend.dto.auth.AuthLoginRequest;
import com.backend.library.backend.dto.auth.AuthResetPasswordRequest;
import com.backend.library.backend.dto.users.UserUpdateRequest;
import com.backend.library.backend.mappers.interfaces.UserMapper;
import com.backend.library.backend.services.interfaces.IKeycloakService;
import com.backend.library.backend.services.interfaces.IWalletService;
import com.backend.library.backend.utils.KeycloakProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeycloakServiceImpl implements IKeycloakService {

    private UserMapper userMapper;

    private IWalletService walletService;

    public KeycloakServiceImpl(UserMapper userMapper, IWalletService walletService) {
        this.userMapper = userMapper;
        this.walletService = walletService;
    }

    /**
     * Find all users in the Keycloak realm.
     *
     * @return A list of user details.
     * @throws NotFoundException If the realm is empty or does not exist.
     */
    @Override
    public ResponseEntity<?> findAllUsers() {
        try {
            // Get the list of users from the Keycloak realm
            List<UserRepresentation> users = KeycloakProvider.getRealmResource().users().list();

            // Map the list of user representations to a list of user details
            return ResponseEntity.status(200)
                    .body(userMapper.fromListDetailsKeycloak(users));
        } catch (NotFoundException e) {
            // If the realm is empty or does not exist, return an error
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Find a user in the Keycloak realm by their user ID.
     *
     * @param userId The ID of the user to find.
     * @return A ResponseEntity containing a user details object if the user is
     *         found, or
     *         an empty response if the user is not found.
     */
    @Override
    public ResponseEntity<?> searchUserById(String userId) {
        try {
            // Map the user representation to a user details object
            return ResponseEntity.status(200)
                    .body(userMapper.fromDetailsKeycloak(userId));
        } catch (NotFoundException e) {
            // If the user is not found, return an empty response
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Find the user associated with the current request token.
     *
     * @param request The HTTP request containing the authorization token.
     * @return A ResponseEntity containing the user details object.
     */
    @Override
    public ResponseEntity<?> searchYourself(HttpServletRequest request) {
        try {
            // Get the user ID from the token
            String userId = KeycloakProvider.getUserIdFromToken(request);
            // Map the user representation to a user details object
            return ResponseEntity.status(200)
                    .body(UserMapper.fromKeycloak(userId));
        } catch (Exception e) {
            // If there is an error finding the user, return an error
            return ResponseEntity.status(500)
                    .body("Error finding user.");
        }
    }

    /**
     * Search a user in the Keycloak realm by their username.
     *
     * @param username The username to search for.
     * @return A ResponseEntity containing the user details object or an error
     *         if the user is not found.
     */
    @Override
    public ResponseEntity<?> searchUserByUsername(String username) {
        try {
            // Search for the user in the Keycloak realm by their username
            UserRepresentation user = KeycloakProvider.getRealmResource().users()
                    .search(username)
                    .getFirst();
            // Map the user representation to a user details object
            return ResponseEntity.status(200)
                    .body(UserMapper.fromKeycloak(user));
        } catch (NotFoundException e) {
            // If the user is not found, return an empty response
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Creates a new user in the Keycloak realm.
     *
     * @param userRequestDTO The user details object containing the username, email,
     *                       name and roles to be used
     *                       for the new user.
     * @return A ResponseEntity containing the access token of the newly created
     *         user or an error if the user
     *         already exists or an error occurs.
     */
    @Override
    public ResponseEntity<?> createUser(AuthCreateUserRequest userRequestDTO) {
        try {

            int status = 0;
            // Get the users resource from the Keycloak realm
            UsersResource userResource = KeycloakProvider.getUserResource();

            // Create a new user in the Keycloak realm
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userRequestDTO.username());
            user.setEmail(userRequestDTO.email());
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setFirstName(userRequestDTO.name());
            user.setLastName(userRequestDTO.surname());

            // Create the user
            Response response = userResource.create(user);
            status = response.getStatus();

            // If the user was created successfully, return the access token
            if (status == 201) {

                // Get the user ID from the response
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf('/') + 1);

                // Reset the user's password to the given password
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType("password");
                credential.setValue(userRequestDTO.password());
                credential.setTemporary(false);
                userResource.get(userId).resetPassword(credential);

                // Add the roles to the user
                RealmResource realmResource = KeycloakProvider.getRealmResource();

                List<RoleRepresentation> roleRepresentations = null;

                if (userRequestDTO.roles() == null || userRequestDTO.roles().isEmpty()) {

                    roleRepresentations = List.of(realmResource.roles().get("client").toRepresentation());
                } else {
                    roleRepresentations = realmResource.roles()
                            .list()
                            .stream()
                            .filter(role -> userRequestDTO.roles().contains(role.getName()))
                            .toList();
                }

                realmResource.users()
                        .get(userId)
                        .roles()
                        .realmLevel()
                        .add(roleRepresentations);

                // Get the access token for the newly created user
                Keycloak loggedUser = KeycloakProvider.newKeycloakBuilderWithPasswordCredentials(
                        userRequestDTO.username(),
                        userRequestDTO.password());

                boolean isSellerRolePresent = roleRepresentations.stream()
                        .anyMatch(role -> role.getName().equals("seller"));
                if (isSellerRolePresent) {
                    walletService.createWallet(userId);
                }
                return ResponseEntity.status(201)
                        .body(loggedUser.tokenManager().getAccessToken());
            } else {

                // If the user already exists, return a conflict error
                return ResponseEntity.status(409).body("User already exists");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            // If an error occurs, return a server error
            return ResponseEntity.status(500).body("Error creating user");
        }
    }

    /**
     * Logs in a user using their username and password.
     * Returns an access token for the user.
     * If the credentials are invalid, returns a 401 response with a
     * message indicating that the credentials are invalid.
     *
     * @param loginRequest the login request containing the username and password
     * @return a ResponseEntity containing the access token, or a 401 response if
     *         the
     *         credentials are invalid
     */
    @Override
    public ResponseEntity<?> loginUser(AuthLoginRequest loginRequest) {
        try (Keycloak keycloak = KeycloakProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.username(),
                loginRequest.password())) {

            // Retrieves an access token from the Keycloak server using the
            // username and password provided in the request
            AccessTokenResponse token = keycloak.tokenManager().getAccessToken();

            // Logs an info message indicating that the login was successful
            log.info("Login successfully. Returns a access token from the user");

            // Returns the access token as a JSON response
            return ResponseEntity.status(HttpStatus.OK).body(token);

        } catch (Exception e) {
            // Logs an error indicating that the credentials provided by the user
            // are invalid
            log.error("Invalid credentials provided by the user", e.getMessage());

            // Returns a 401 response with a message indicating that the credentials
            // are invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials. Please try again.");
        }
    }

    /**
     * Updates the user's information in Keycloak.
     *
     * @param userId         the ID of the user to update
     * @param userDetailsDTO the information to update for the user
     * @return a ResponseEntity containing the updated user information, or a 500
     *         response if an error occurs
     */
    @Override
    public ResponseEntity<?> updateUser(String userId, UserUpdateRequest userDetailsDTO) {
        try {

            // Create a new UserRepresentation object with the updated information
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            if (userDetailsDTO.username() != null) {
                user.setUsername(userDetailsDTO.username());
            }

            if (userDetailsDTO.name() != null) {
                user.setFirstName(userDetailsDTO.name());
            }

            if (userDetailsDTO.surname() != null) {
                user.setLastName(userDetailsDTO.surname());
            }

            // Update the user in Keycloak using the UserResource
            KeycloakProvider.getUserResource().get(userId).update(user);
            log.info("User updated successfully.", "KeycloakServiceImpl.updateUser");
            return ResponseEntity.status(200)
                    // Return the updated user information as a JSON response
                    .body(UserMapper.fromKeycloak(userId));

        } catch (Exception e) {
            // Log an error if an exception occurs when updating the user
            log.error("Error updating user", e.getMessage());
            return ResponseEntity.status(500).body("Error updating user");
        }
    }

    /**
     * Updates a user in Keycloak based on the information in the
     * UserUpdateRequest object.
     *
     * @param request the HTTP request containing the user's id in the token
     * @param userDTO the updated user information
     * @return a ResponseEntity containing the updated user information, or a 500
     *         response if an error occurs
     */
    @Override
    public ResponseEntity<?> updateYourself(HttpServletRequest request, @Valid UserUpdateRequest userDTO) {
        try {

            // Get the ID of the user from the token
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Update the user in Keycloak
            return updateUser(userId, userDTO);

        } catch (Exception e) {
            // Log an error if an exception occurs when updating the user
            log.error("Error updating user.", e.getMessage());

            // Return a 500 response with an error message
            return ResponseEntity.status(500).body("Error updating user.");
        }
    }

    /**
     * Resets a user's password in Keycloak based on the information in the
     * AuthResetPasswordRequest object.
     *
     * @param request              the HTTP request containing the user's id in the
     *                             token
     * @param resetPasswordRequest the request containing the old and new
     *                             password information
     * @return a ResponseEntity containing a JSON object indicating whether the
     *         password was reset successfully or not, or a 500 response if an error
     *         occurs
     */
    @Override
    public ResponseEntity<?> resetPassword(HttpServletRequest request,
            @Valid AuthResetPasswordRequest resetPasswordRequest) {
        try {

            // Get the ID of the user from the token
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Get the user resource from Keycloak
            UserResource userResource = KeycloakProvider.getUserResource().get(userId);

            // Check if the username in the request matches the username in
            // Keycloak
            if (!Objects.equals(resetPasswordRequest.username(),
                    userResource.toRepresentation().getUsername())) {
                return ResponseEntity.status(401).body("Invalid credentials. Please try again.");
            }

            // Create a Keycloak instance with the old password
            Keycloak keycloak = KeycloakProvider.newKeycloakBuilderWithPasswordCredentials(
                    resetPasswordRequest.username(),
                    resetPasswordRequest.oldPassword());

            // Check if the old password is invalid
            if (keycloak == null) {

                return ResponseEntity.status(401).body("Invalid credentials. Please try again.");
            }

            // Create a credential representation of the new password
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType("password");
            credential.setValue(resetPasswordRequest.newPassword());
            credential.setTemporary(false);

            // Reset the user's password in Keycloak
            userResource.resetPassword(credential);

            // Log a success message
            log.info("Password reset successfully.", "AuthenticationServiceImpl.resetPassword");

            // Return a 200 response with a JSON object indicating success
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserMapper.fromKeycloak(userId));

        } catch (Exception e) {
            // Log an error if an exception occurs
            log.error("Error updating user.", e.getMessage());

            // Return a 500 response with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user.");
        }
    }

    /**
     * Authenticates a user based on the given HTTP request and returns a
     * ResponseEntity with a boolean value indicating the authentication result.
     * If the token in the Authorization header is invalid or missing, a
     * 401 response is returned with a body of false. If an exception occurs
     * during authentication, a 500 response is returned with a body of false.
     *
     * @param request the HTTP request containing the Authorization header
     * @return a ResponseEntity with a boolean value indicating the authentication
     *         result
     */
    @Override
    public ResponseEntity<?> authenticateUser(HttpServletRequest request) {
        try {
            // Get the token from the Authorization header
            String token = request.getHeader("Authorization");
            // If the token is null or empty, return a 401 response with a
            // body of false
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(false);
            }
            // Return a 200 response with a body of true if the token is valid
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (Exception e) {
            // Log an error if an exception occurs
            log.error("Error authenticating user.", e.getMessage());
            // Return a 500 response with a body of false if an exception
            // occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }

    /**
     * Logs out a user from Keycloak based on the information in the
     * given HTTP request.
     *
     * @param request the HTTP request containing the Authorization header
     * @return a ResponseEntity containing a UserDTO object with the user's ID,
     *         or a 500 response if an error occurs
     */
    @Override
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {

            // Get the user id from the given HTTP request
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Get the user resource from Keycloak
            UserResource userResource = KeycloakProvider.getUserResource().get(userId);

            // Logout the user from Keycloak
            userResource.logout();

            // Log a success message
            log.info("User logged out successfully.", userResource);

            // Return a 200 response with a UserDTO object containing the user's ID
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserMapper.fromKeycloak(userId));

        } catch (Exception e) {
            // Log an error if an exception occurs
            log.error("Error logging out user", e.getMessage());

            // Return a 500 response with a body of an error message if an
            // exception occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error logging out user");
        }
    }

}