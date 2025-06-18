package com.example.ecom_api_gateway.filter;

import com.example.ecom_api_gateway.config.JwtUtil;

import org.springframework.web.server.WebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil=jwtUtil;
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 1. Get the request URI path
        String path = exchange.getRequest().getURI().getPath();

        // 2. Skip JWT check for authentication endpoints (e.g., /auth/**)
        if (path.startsWith("/auth")) {
            return chain.filter(exchange); // Continue the filter chain without JWT validation
        }

        // 3. Extract JWT token from the Authorization header
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        String jwt = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("Error extracting username from token: {}", e.getMessage(), e);
                // Optionally, immediately return unauthorized response here
                // exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                // return exchange.getResponse().setComplete(); // Complete the response and stop chain
                // For now, we continue the chain, letting subsequent security filters handle unauthenticated
            }
        }

        final String finalJwt = jwt; // Need final variables for use in lambda expressions
        final String finalUsername = username;

        // 4. Validate token and set authentication in the Reactive Security Context
        return ReactiveSecurityContextHolder.getContext() // Get the current reactive security context
                .defaultIfEmpty(null) // If no context exists, provide a null mono
                .flatMap(securityContext -> {
                    // Check if username was extracted AND there's no existing authentication in the context
                    if (finalUsername != null && (securityContext == null || securityContext.getAuthentication() == null)) {
                        if (jwtUtil.validateToken(finalJwt)) {
                            // Token is valid: Create authentication token
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    finalUsername, null, Collections.singletonList(new SimpleGrantedAuthority("USER")));

                            // Set the authentication in the reactive security context for downstream use
                            // This uses contextWrite to propagate the authentication down the chain
                            logger.info("Successfully authenticated user: {}", finalUsername);
                            return chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                        } else {
                            logger.warn("Invalid JWT token for user: {}", finalUsername);
                        }
                    }
                    // If no valid JWT, or already authenticated, or validation failed, just continue the filter chain
                    return chain.filter(exchange);
                });
    }
}