package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.*;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.MailService;
import dev.sjimo.oop2024project.service.UserService;
import dev.sjimo.oop2024project.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resendVerificationEmail")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody ResendVerificationEmailRequest request) {
        userService.resendVerificationEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestBody VerificationRequest request) {
        verificationService.verifyForRegistration(request.getToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String jwt = userService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgetPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestHeader(value = "Authorization", required = false) String jwtToken, @RequestBody ResetPasswordRequest request) {
        if (jwtToken != null) {
            Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
            userService.resetPassword(userId, request.getNewPassword());
        } else {
            var userId = verificationService.verifyForResetPassword(request.getToken());
            userService.resetPassword(userId, request.getNewPassword());
        }
        return ResponseEntity.ok().build();
    }
}
