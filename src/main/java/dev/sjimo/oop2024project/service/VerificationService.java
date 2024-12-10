package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.model.VerificationToken;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.repository.VerificationTokenRepository;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        var token = tokenRepository.findByUser_Id(userId).orElse(new VerificationToken());
        if (token.getIssuedDate() != null && Duration.between(LocalDateTime.now(), token.getIssuedDate()).toMinutes() < 60) {
            throw new ResponseException(ErrorCode.VERIFICATION_TOO_FREQUENT);
        }

        token.setToken(UUID.randomUUID().toString());
        token.setIssuedDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        if (token.getUser() == null) {
            User user = userRepository.findById(userId).orElseThrow();
            token.setUser(user);
        }

        tokenRepository.save(token);
        return token.getToken();
    }


    public User verifyToken(String token) {
        Optional<VerificationToken> verificationTokenOpt = tokenRepository.findByToken(token);
        if (!(verificationTokenOpt.isPresent() && verificationTokenOpt.get().getToken().equals(token)
                && verificationTokenOpt.get().getExpiryDate().isAfter(LocalDateTime.now()))) {
            throw new ResponseException(ErrorCode.INVALID_VERIFICATION_TOKEN);
        }
        User user = verificationTokenOpt.get().getUser();
        tokenRepository.delete(verificationTokenOpt.get());
        return user;
    }

    public void verifyForRegistration(String token) {
        User user = verifyToken(token);
        if (user.isVerified())
            throw new ResponseException(ErrorCode.USER_ALREADY_VERIFIED);
        user.setVerified(true);
        userRepository.save(user);
    }

    public Long verifyForResetPassword(String token) {
        User user = verifyToken(token);
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        return user.getId();
    }
}
