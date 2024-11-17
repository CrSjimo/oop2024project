package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.UserData;
import dev.sjimo.oop2024project.repository.UserDataRepository;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.repository.VerificationTokenRepository;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MailService mailService;

    public void registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseException(ErrorCode.USER_ALREADY_EXIST);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // 需加密保存
        user.setVerified(false);
        userRepository.save(user);

        String token = verificationService.generateVerificationToken(user.getId());
        mailService.sendVerificationEmail(email, token);
    }

    public void resendVerificationEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        var token = verificationService.regenerateVerificationToken(user.getId());
        mailService.sendVerificationEmail(email, token);
    }

    public String loginUser(String email, String password) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        if (!user.getPassword().equals(password)) {
            throw new ResponseException(ErrorCode.AUTH_FAILED);
        }
        var userDataOpt = userDataRepository.findByUser_Id(user.getId());
        if (userDataOpt.isEmpty()) {
            var userData = new UserData();
            userData.setUsername("User " + user.getId().toString());
            userData.setGender(UserData.Gender.OTHER);
            userData.setGravatarEmail(user.getEmail());
            userData.setDescription("");
            userData.setUser(user);
            userDataRepository.save(userData);
        }
        return jwtService.generateToken(user.getId());
    }

    public void resetPassword(Long id, String password) {
        try {
            var user = userRepository.findById(id).orElseThrow();
            user.setPassword(password);
            userRepository.save(user);
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    public void forgetPassword(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }

        String token = verificationService.generateVerificationToken(user.getId());
        mailService.sendVerificationEmail(email, token);
    }
}