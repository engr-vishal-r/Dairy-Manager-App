package com.dairyProducts.details.controller;

import com.dairyProducts.details.utility.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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


        this.USERS = Map.of(
                "user", "$2a$10$QE7bpDw3GMKDzFuqmfjU1uRXRQVTaE9jff.1tZDxNU2O79uT9kZTW",
                "admin", "$2a$10$HGe4t58CD5Z7S/.gIqFYAe6hHcl3/jjX8o9JxS0nPXQW6FP6whM.K"
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
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
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

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("pass"));
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}

