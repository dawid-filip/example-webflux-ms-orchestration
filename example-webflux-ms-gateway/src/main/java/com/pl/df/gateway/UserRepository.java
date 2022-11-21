package com.pl.df.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Log4j2
public class UserRepository implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private static Set<UserDetails> users = new HashSet<>();

    static {
        users.add(new User("guest", "$2a$12$kSCGkhEDG5HWliog/0fEm.jWemtJ5UnLXgc.X0VzITOjuhrFNVYPa", true, List.of()));  // pssword
        users.add(new User("user", "$2a$12$kSCGkhEDG5HWliog/0fEm.jWemtJ5UnLXgc.X0VzITOjuhrFNVYPa", true, List.of("ROLE_USER")));   // pssword
        users.add(new User("admin", "$2a$12$kSCGkhEDG5HWliog/0fEm.jWemtJ5UnLXgc.X0VzITOjuhrFNVYPa", true, List.of("ROLE_USER", "ROLE_ADMIN")));   // pssword
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.just(username)
                .map(name -> users.stream()
                        .filter(user -> user.getUsername().equalsIgnoreCase(name))
                        .findFirst()
                        .orElseGet(() -> null)
                )
                .doOnNext(log::info);
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return Mono.error(new IllegalStateException("Password update is prohibited!"));
    }

}
