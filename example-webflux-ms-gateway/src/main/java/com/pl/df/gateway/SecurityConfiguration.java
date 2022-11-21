package com.pl.df.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain authorization(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin()
                .and()
                .authorizeExchange(ae -> ae
                        .pathMatchers("/public", "/login", "/logout")
                                .permitAll()
                        .pathMatchers("/product").authenticated()
                        .pathMatchers("/product/**").authenticated()
                        .pathMatchers("/product-details").authenticated()
                        .pathMatchers("/product-details/**").authenticated()
//                        .anyExchange().permitAll() // "/public" -> permitAll
                )
                .logout()
                .and()
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        var manager = new UserDetailsRepositoryReactiveAuthenticationManager(userRepository);
        manager.setPasswordEncoder(passwordEncoder);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
