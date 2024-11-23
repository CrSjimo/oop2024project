package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final String SECRET_KEY = "6AD589E84ADD4A2590F365B6A8AE7C500453D25CF88A4A3C81159FD1B42CBE14";

    @Autowired
    UserRepository userRepository;

    public String generateToken(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("pwd", user.getPassword())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 100))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long extractUserId(String token) {
        try {
            var jwtPayload = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            var userId = Long.parseLong(jwtPayload.getSubject());
            var user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
            if (!user.getPassword().equals(jwtPayload.get("pwd").toString())) {
                throw new ResponseException(ErrorCode.PERMISSION_DENIED);
            }
            return userId;

        } catch (JwtException | NumberFormatException e) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
    }
}
