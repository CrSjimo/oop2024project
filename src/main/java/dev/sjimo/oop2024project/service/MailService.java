package dev.sjimo.oop2024project.service;

import org.springframework.stereotype.Service;

@Service
public class MailService {
    public void sendVerificationEmail(String email, String token) {
        // 使用邮件发送库，例如 JavaMailSender，发送包含 `token` 的邮件
    }
}