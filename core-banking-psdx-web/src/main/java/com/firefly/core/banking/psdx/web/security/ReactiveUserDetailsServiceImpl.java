/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.banking.psdx.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of ReactiveUserDetailsService for user authentication.
 * This is a simplified implementation for demonstration purposes.
 * In a real application, this would be connected to a user repository.
 */
@Service

@Slf4j
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Map<String, UserDetails> users = new HashMap<>();

    /**
     * Constructor to initialize some test users.
     */
    public ReactiveUserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        // Add some test users
        users.put("admin", User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build());

        users.put("user", User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build());
    }

    /**
     * Find a user by username.
     *
     * @param username The username
     * @return A Mono of UserDetails
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return Mono.justOrEmpty(users.get(username));
    }
}
