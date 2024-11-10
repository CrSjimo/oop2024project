package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.model.VerificationToken;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    public String generateVerificationToken(Long userId) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        token.setUser(user);

        tokenRepository.save(token);
        return token.getToken();
    }

    public boolean verifyToken(Long userId, String token) {
        Optional<VerificationToken> verificationTokenOpt = tokenRepository.findByUser_Id(userId);
        if (verificationTokenOpt.isPresent() && verificationTokenOpt.get().getToken().equals(token)
                && verificationTokenOpt.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = verificationTokenOpt.get().getUser();
            user.setVerified(true);
            userRepository.save(user);
            tokenRepository.delete(verificationTokenOpt.get());
            return true;
        }
        return false;
    }
}
