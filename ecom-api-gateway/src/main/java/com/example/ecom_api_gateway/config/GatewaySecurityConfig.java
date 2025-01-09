package com.example.ecom_api_gateway.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class GatewaySecurityConfig {

    public SecurityFilterChain securityFilterChain (HttpSecurity http)throws Exception{
        http.csrf(csrf->csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
