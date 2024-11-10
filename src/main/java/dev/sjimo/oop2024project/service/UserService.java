package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.repository.VerificationTokenRepository;
import dev.sjimo.oop2024project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MailService mailService;

    public String registerUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // 需加密保存
        user.setVerified(false);
        userRepository.save(user);

        String token = verificationService.generateVerificationToken(user.getId());
        mailService.sendVerificationEmail(email, token);

        return jwtService.generateToken(user.getId());
    }

    public boolean verifyToken(Long id, String token) {
        return verificationService.verifyToken(id, token);
    }

    public String loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().isVerified() && userOpt.get().getPassword().equals(password)) {
            return jwtService.generateToken(userOpt.get().getId());
        }
        throw new RuntimeException("Invalid credentials or user not verified");
    }
}