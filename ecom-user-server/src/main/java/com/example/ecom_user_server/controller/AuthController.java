package com.example.ecom_user_server.controller;

import com.example.ecom_user_server.Model.User;
import com.example.ecom_user_server.config.JwtUtil;
import com.example.ecom_user_server.dto.AuthRequest;
import com.example.ecom_user_server.dto.AuthResponse;
import com.example.ecom_user_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        System.out.println("Before authentication");
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())

        );
        System.out.println("After authentication");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest registerRequest) {
        try {
            // For simplicity, let's assume a default role for new registrations
            // In a real app, you might have more complex role assignment logic
            User newUser = userService.registerNewUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    "USER" // Assign a default role, e.g., "USER"
            );
            // Optionally, generate and return a JWT token immediately after registration
            // UserDetails userDetails = customUserDetailsService.loadUserByUsername(newUser.getUsername());
            // String token = jwtUtil.generateToken(userDetails);
            // return ResponseEntity.ok(new AuthResponse(token));

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully: " + newUser.getUsername());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }
}