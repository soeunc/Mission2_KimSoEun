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
                                "users/read-business",
                                "users/update-business/status"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/users/update-business"
                        )
                        .hasAnyRole("USER", "SELLER", "OFFER")

                        .requestMatchers(
                                "/shops/{itemId}",
                                "/shops/create",
                                "/shops/{itemId}/offers"
                        )
                        .hasAnyRole("USER", "BUSINESS","ADMIN", "OFFER")

                        .requestMatchers(
                                "/shops/{itemId}",
                                "/shops/{itemId}/update",
                                "/shops/{itemId}/delete",
                                "/shops/offer/read-seller",
                                "/shops/{itemId}/response/{offerId}"
                        )
                        .hasRole("SELLER")

                        .requestMatchers(
                                "/shops/offer/read-offer",
                                "/shops/{itemId}/status/{offerId}")
                        .hasRole("OFFER")

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