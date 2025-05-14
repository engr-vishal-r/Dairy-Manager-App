package com.dairyProducts.details.controller;

import com.dairyProducts.details.utility.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final Map<String, String> USERS;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;

        // Now encoder is initialized, safe to use
        this.USERS = Map.of(
                "user", encoder.encode("pass"),
                "admin", encoder.encode("admin")
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest request) {
        String password = USERS.get(request.getUsername());
        if (password != null && encoder.matches(request.getPassword(), password)) {
            String role = request.getUsername().equals("admin") ? "ADMIN" : "USER";
            String token = jwtUtil.generateToken(request.getUsername(), role);
            return Map.of("token", token);
        }
        throw new RuntimeException("Invalid credentials");
    }
}

class AuthRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
