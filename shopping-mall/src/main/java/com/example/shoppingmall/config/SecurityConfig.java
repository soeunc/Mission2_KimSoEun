package com.example.shoppingmall.config;

import com.example.shoppingmall.jwt.JwtTokenFilter;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/register",
                                "/users/login",
                                "/users/{userId}/avatar",
                                "/shops"
                        )
                        .permitAll()

                        .requestMatchers("/users/update-user")
                        .hasRole("INACTIVE")

                        .requestMatchers(
                                "/users/update-business",
                                "/shops/{userId}/create"
                        )
                        .hasRole("USER")

                        .requestMatchers(
                                "/shops/{itemId}",
                                "/shops/{userId}/{itemId}/update",
                                "/shops/{userId}/{itemId}/delete"
                        )
                        .hasAnyRole("USER", "BUSINESS_USER","ADMIN")

                        .requestMatchers(
                                "/shops/{userId}/{itemId}/update",
                                "/shops/{userId}/{itemId}/delete"
                        )
                        .hasAuthority("WRITE_AUTHORITY")

                        .anyRequest()
                        .authenticated()

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenUtils, manager),
                        AuthorizationFilter.class
                )
        ;
        return http.build();
    }
}