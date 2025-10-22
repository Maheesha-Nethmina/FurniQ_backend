package com.project.FurniQ.controller;

import com.project.FurniQ.entity.User;
import com.project.FurniQ.service.JwtService;
import com.project.FurniQ.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // 1. Register the user (hashes password, sets role "USER")
        User savedUser = userService.register(user);

        // --- THIS IS THE NEW PART ---
        // 2. Generate a token for the new user immediately
        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole());

        // 3. Return the full login response, just like the /login endpoint
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", savedUser.getRole(),
                "email", savedUser.getEmail(),
                "username", savedUser.getUsername(),
                "message","Registration successful!"
        ));
        // --- END OF MODIFICATION ---
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        var userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty() || !userService.checkPassword(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        User user = userOpt.get();
        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole(),
                "email", user.getEmail(),
                "username", user.getUsername(),
                "message","User successfully logged in"
        ));
    }
}