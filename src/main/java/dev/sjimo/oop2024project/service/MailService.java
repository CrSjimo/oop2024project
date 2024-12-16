package dev.sjimo.oop2024project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${oop2024project.debug}")
    private boolean debug;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    public void sendVerificationEmail(String email, String token) {
        if (debug) {
            logger.info("email={} token={}", email, token);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification Email - oop2024project");
        message.setText("Your verification token is\n" + token);
        message.setFrom("no-reply@sjimo.dev");
        mailSender.send(message);
    }
}