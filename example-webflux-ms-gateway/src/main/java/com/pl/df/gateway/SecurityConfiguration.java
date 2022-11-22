package com.pl.df.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain authorization(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin()
                .and()
                .authorizeExchange(ae -> ae
                        .pathMatchers("/public", "/login", "/logout", "/", "/index.html")
                        .permitAll()
                )
                .authorizeExchange(ae -> ae
                        .pathMatchers("/product", "/product/**")
                        .hasAnyRole("USER", "ADMIN")
                )
                .authorizeExchange(ae -> ae
                        .pathMatchers("/product-details", "/product-details/**")
                        .hasAnyRole("ADMIN")
                        .anyExchange()
                        .authenticated()
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
