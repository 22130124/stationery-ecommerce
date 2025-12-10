package com.tqk.apigateway.config;

import com.tqk.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll() // thêm dòng này
                        .pathMatchers("/products/admin","/products/admin/**").hasRole("ADMIN")
                        .pathMatchers("/orders/admin","/orders/admin/**").hasRole("ADMIN")
                        .pathMatchers("/auth/admin","/auth/admin/**").hasRole("ADMIN")
                        .pathMatchers("/auth", "/auth/**").permitAll()
                        .pathMatchers("/products", "/products/**").permitAll()
                        .pathMatchers("/categories", "/categories/**").permitAll()
                        .pathMatchers("/brands", "/brands/**").permitAll()
                        .pathMatchers("/suppliers", "/suppliers/**").permitAll()
                        .pathMatchers("/ai", "/ai/**").permitAll()
                        .pathMatchers("/recommend", "/recommend/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
