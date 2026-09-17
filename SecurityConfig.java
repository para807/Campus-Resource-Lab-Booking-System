package com.campus.booking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthJwtFilter authJwtFilter;

    public SecurityConfig(AuthJwtFilter authJwtFilter) {
        this.authJwtFilter = authJwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("LAB_ADMIN")
                        .requestMatchers("/api/bookings/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(authJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}