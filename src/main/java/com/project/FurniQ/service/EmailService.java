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

    /**
     * Sends a welcome email to a new user.
     * This method is asynchronous (@Async) and will run in a separate thread.
     *
     * @param to       The recipient's email address.
     * @param username The user's name.
     */
    @Async
    public void sendWelcomeEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@furniq.com"); // Set your 'from' address
            message.setTo(to);
            message.setSubject("Welcome to FurniQ!");
            message.setText("Hello " + username + ",\n\nThank you for registering with FurniQ. We're excited to have you!");

            mailSender.send(message);
        } catch (Exception e) {
            // It's good practice to log errors, especially in async methods
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}