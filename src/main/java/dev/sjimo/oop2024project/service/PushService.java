package dev.sjimo.oop2024project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PushService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void pushTo(Long userId, Object content) {
        //TODO 暂时先发邮件，之后用socket实现
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Push Notification");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            message.setText(objectMapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        message.setFrom("no-reply@sjimo.dev");
        mailSender.send(message);
    }
}
