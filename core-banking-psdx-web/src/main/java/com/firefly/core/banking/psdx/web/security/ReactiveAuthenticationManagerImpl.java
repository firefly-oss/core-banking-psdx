package com.firefly.core.banking.psdx.web.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Implementation of ReactiveAuthenticationManager for user authentication.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReactiveAuthenticationManagerImpl implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticate a user.
     *
     * @param authentication The authentication object
     * @return A Mono of Authentication
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        log.debug("Authenticating user: {}", username);
        
        return userDetailsService.findByUsername(username)
                .filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
                .map(this::createAuthentication)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password")));
    }

    /**
     * Create an authentication object from user details.
     *
     * @param userDetails The user details
     * @return The authentication object
     */
    private Authentication createAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
