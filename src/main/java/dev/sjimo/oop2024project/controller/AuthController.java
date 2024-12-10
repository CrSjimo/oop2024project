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

    @PostMapping("/register")//子路径
    public void register(@RequestBody RegisterRequest request) {
        userService.registerUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/verify")
    public void verifyToken(@RequestBody VerificationRequest request) {
        verificationService.verifyForRegistration(request.getToken());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String jwt = userService.loginUser(request.getEmail(), request.getPassword());
        return new LoginResponse(jwt);
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgetPassword(request.getEmail());
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestHeader(value = "Authorization", required = false) String jwtToken, @RequestBody ResetPasswordRequest request) {
        if (jwtToken != null) {
            Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
            userService.resetPassword(userId, request.getNewPassword());
        } else {
            var userId = verificationService.verifyForResetPassword(request.getToken());
            userService.resetPassword(userId, request.getNewPassword());
        }
    }

    @PostMapping("/resetEmail")
    public void resetEmail(@RequestHeader(value = "Authorization", required = false) String jwtToken, @RequestBody ResetEmailRequest request) {
        if (jwtToken != null) {
            Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
            userService.resetEmail(request.getNewEmail(), userId);
        }else {
            var userId = verificationService.verifyForResetPassword(request.getToken());
            userService.resetEmail(request.getNewEmail(), userId);
        }
    }

}
