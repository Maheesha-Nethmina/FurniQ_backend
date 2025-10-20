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
        User savedUser = userService.register(user);
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "email", savedUser.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        var userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty() || !userService.checkPassword(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        String token = jwtService.generateToken(email, userOpt.get().getRole());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", userOpt.get().getRole(),
                "email", userOpt.get().getEmail(),
                "message","User successfully logged in"
        ));
    }
}
