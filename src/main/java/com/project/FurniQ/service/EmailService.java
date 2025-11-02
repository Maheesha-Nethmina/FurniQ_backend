package com.project.FurniQ.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendWelcomeEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("maheeshanethmina@gmail.com");
            message.setTo(to);
            message.setSubject("Welcome to FurniQ!");
            message.setText("Hello " + username + ",\n\nThank you for registering with FurniQ. We're excited to have you!");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    @Async
    public void sendCustomEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("maheeshanethmina@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send custom email to " + to + ": " + e.getMessage());
        }
    }
}

