package com.backend.library.backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.backend.library.backend.converters.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        /**
         * Converter used to process the JWT obtained from the authentication process.
         */
        @Autowired
        private JwtAuthenticationConverter jwtAuthenticationConverter;

        /**
         * Creates the security filter chain.
         *
         * @return the security filter chain
         * @throws Exception
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(requests -> {
                                        // Public endpoints
                                        requests.requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                                        "/swagger-ui.html").permitAll();
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll();

                                        // Auth endpoints
                                        requests.requestMatchers("/auth/login").permitAll();
                                        requests.requestMatchers("/auth/register").permitAll();
                                        requests.requestMatchers("/auth/reset-password").authenticated();
                                        requests.requestMatchers("/auth/me").authenticated();

                                        // images
                                        requests.requestMatchers("/images/**").permitAll();

                                        // User endpoints
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/users/me").authenticated();
                                        requests.requestMatchers(HttpMethod.PUT, "/api/v1/users/me").authenticated();

                                        // User protected endpoints
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/users")
                                                        .hasRole("admin_client_role");
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/users/{userId}")
                                                        .hasAnyRole("admin_client_role");

                                        // Address protected endpoints
                                        requests.requestMatchers("/api/v1/addresses/me").hasRole("client_client_role");
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/addresses/**")
                                                        .hasRole("admin_client_role");
                                        requests.requestMatchers(HttpMethod.DELETE, "/api/v1/addresses/**")
                                                        .hasRole("client_client_role");
                                        requests.requestMatchers(HttpMethod.POST, "/api/v1/addresses/**")
                                                        .hasRole("client_client_role");
                                        requests.requestMatchers(HttpMethod.PUT, "/api/v1/addresses/**")
                                                        .hasRole("client_client_role");

                                        // Book endpoints
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/books/**").permitAll();
                                        requests.requestMatchers(HttpMethod.POST, "/api/v1/books/search").permitAll();

                                        // Book protected endpoints
                                        requests.requestMatchers(HttpMethod.DELETE, "/api/v1/books/**")
                                                        .hasRole("admin_client_role");
                                        requests.requestMatchers(HttpMethod.POST, "/api/v1/books")
                                                        .hasAnyRole("seller_client_role");
                                        requests.requestMatchers(HttpMethod.PUT, "/api/v1/books/{isbn}")
                                                        .hasRole("seller_client_role");
                                        requests.requestMatchers("/api/v1/books/pending")
                                                        .hasAnyRole("admin_client_role");
                                        requests.requestMatchers("/api/v1/books/status/{isbn}/{status}")
                                                        .hasRole("admin_client_role");
                                        requests.requestMatchers("/api/v1/books/seller").hasRole("seller_client_role");

                                        // Review endpoints
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll();

                                        // Review protected endpoints
                                        requests.requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/{isbn}")
                                                        .hasRole("client_client_role");
                                        requests.requestMatchers(HttpMethod.POST, "/api/v1/reviews")
                                                        .hasAnyRole("client_client_role", "seller_client_role");
                                        requests.requestMatchers(HttpMethod.PUT, "/api/v1/reviews/{reviewId}")
                                                        .hasAnyRole("client_client_role", "seller_client_role");
                                        requests.requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/admin/{id}")
                                                        .hasRole("admin_client_role");

                                        // Author endpoints
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/authors/**").permitAll();

                                        // Author protected endpoints
                                        requests.requestMatchers(HttpMethod.DELETE, "/api/v1/authors/**")
                                                        .hasRole("admin_client_role");
                                        requests.requestMatchers(HttpMethod.POST, "/api/v1/authors/**")
                                                        .hasAnyRole("admin_client_role", "seller_client_role");
                                        requests.requestMatchers(HttpMethod.PUT, "/api/v1/authors/**")
                                                        .hasAnyRole("admin_client_role");

                                        // Genre endpoints
                                        requests.requestMatchers(HttpMethod.GET, "/api/v1/genres/**").permitAll();

                                        // Cart protected endpoints
                                        requests.requestMatchers("/api/v1/cart/**")
                                                        .hasRole("client_client_role");

                                        // payment protected endpoints
                                        requests.requestMatchers("/api/v1/payment/**")
                                                        .hasRole("client_client_role");

                                        // Sale protected endpoints
                                        requests.requestMatchers("/api/v1/sales/**")
                                                        .hasAnyRole("client_client_role", "seller_client_role");
                                        
                                                        
                                        // Wallet protected endpoints
                                        requests.requestMatchers("/api/v1/wallet/**")
                                                        .hasRole("seller_client_role");

                                        // All other requests require authentication
                                        requests.anyRequest().authenticated();
                                })
                                .oauth2ResourceServer(
                                                oauth -> oauth.jwt(jwt -> jwt
                                                                .jwtAuthenticationConverter(jwtAuthenticationConverter)

                                                ))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .build();
        }

        /**
         * Configuration source for CORS.
         *
         * @return the CORS configuration source
         */
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:4200"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "Content-Length",
                                "X-Requested-With",
                                "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
