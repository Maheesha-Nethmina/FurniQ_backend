package com.project.FurniQ.config;

import com.project.FurniQ.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                // FIXED: Explicitly set CORS configuration to allow cross-origin requests
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                        // Allowing Furniture/Homedeco endpoints for public access (as per your current setup)
                        .requestMatchers("/api/v1/furniture/**").permitAll()
                        .requestMatchers("/api/v1/homedeco/**").permitAll()

                        // Keeping other permitAll requests
//                        .requestMatchers("/api/v1/auth/getAllUsers").permitAll()
//                        .requestMatchers("/api/v1/auth/updateUserDetails").permitAll()
//                        .requestMatchers("/api/v1/auth/sendEmail").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("api/v1/order/**").permitAll()
                        .requestMatchers("api/v1/cart/**").permitAll()
                        .requestMatchers("api/v1/payment/**").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ADDED: Bean to configure CORS policies globally
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow your frontend origins (React defaults)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));

        // Allow all required HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Allow all headers (Content-Type, Authorization, etc.)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (for cookies, JWTs, etc.)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}