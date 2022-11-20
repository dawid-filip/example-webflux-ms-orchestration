package com.pl.df.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain authorization(ServerHttpSecurity http) {
        return http
                .httpBasic(c -> Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ae -> ae
                        .pathMatchers("/product").authenticated()
                        .pathMatchers("/product/**").authenticated()
                        .pathMatchers("/product-details").authenticated()
                        .pathMatchers("/product-details/**").authenticated()
                        .anyExchange().permitAll() // "/public" -> permitAll
                )
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService authentication() {
        UserDetails user = User.builder().username("user")
                .password("$2a$12$kSCGkhEDG5HWliog/0fEm.jWemtJ5UnLXgc.X0VzITOjuhrFNVYPa")   // password
                .roles("USER").build();
        UserDetails admin = User.builder().username("admin")
                .password("$2a$12$B4hLuKH6lNN/Io5cD8CAlOeqT8gpn6BxWcgzR/WgGGMtqrmRt1Ai6")   // pwd
                .roles("USER", "ADMIN").build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
