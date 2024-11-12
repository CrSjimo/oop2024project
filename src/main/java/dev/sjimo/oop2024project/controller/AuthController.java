package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.request.LoginRequest;
import dev.sjimo.oop2024project.request.RegisterRequest;
import dev.sjimo.oop2024project.request.VerificationRequest;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.UserService;
import dev.sjimo.oop2024project.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestBody VerificationRequest request) {
        boolean isVerified = verificationService.verifyToken(request.getToken());

        if (isVerified) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String jwt = userService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(jwt);
    }
}
