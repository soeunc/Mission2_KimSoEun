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
                                "/shops/register",
                                "/shops/login",
                                "/shops/update-user",
                                "/shops/update-business",
                                "/shops/{userId}/avatar"
                        )
                        .permitAll()
//                        .requestMatchers(
//                                "/shops/home",
//                                "/shops/changeUser",
//                                "/shops/update-user"
//                        )
//                        .authenticated()

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