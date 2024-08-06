package com.backend.library.backend.utils;

import java.util.List;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import com.auth0.jwt.JWT;
import com.backend.library.backend.handlers.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeycloakProvider {

    private KeycloakProvider() {
    }

    private static final String SERVER_URL = "http://localhost:9099";
    private static final String REALM_NAME = "library";
    private static final String REALM_MASTER = "master";
    private static final String CLIENT_ID = "backend";
    private static final String CLIENT_SECRET = "FgDWqgMLMJqgkB4sNnH497JhrarBGBjY";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USER_CONSOLE = "admin";
    private static final String USER_PASSWORD = "admin";

    public static RealmResource getRealmResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .username(USER_CONSOLE)
                .password(USER_PASSWORD)
                .clientId(ADMIN_CLI)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();

        return keycloak.realm(REALM_NAME);
    }

    public static Keycloak newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder() //
                .realm(REALM_NAME) //
                .serverUrl(SERVER_URL)//
                .clientId(CLIENT_ID) //
                .clientSecret(CLIENT_SECRET) //
                .username(username) //
                .password(password).build();
    }

    public static UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();

        return realmResource.users();
    }

    public static String getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            log.error("Invalid token provided.", "AuthenticationServiceImpl.authenticateUser");
            throw new UnauthorizedException("Invalid token");
        }

        token = token.replaceFirst("^Bearer ", "");
        return JWT.decode(token).getClaim("sub").asString();
    }

    public static List<UserRepresentation> getUsersFromRoles(String role) {
        return KeycloakProvider.getRealmResource().roles().get(role).getUserMembers();
    }
}
