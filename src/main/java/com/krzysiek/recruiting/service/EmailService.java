package com.krzysiek.recruiting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String errorNotificationEmail;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender javaMailSender, @Value("${error.notification.email}") String errorNotificationEmail) {
        this.javaMailSender = javaMailSender;
        this.errorNotificationEmail = errorNotificationEmail;
    }

    @Async
    public void sendErrorEmail(Exception ex){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(errorNotificationEmail);
            message.setSubject("Error in spring application API:");
            message.setText("Error in spring application API: " + ex.getMessage());

            javaMailSender.send(message);
        } catch(Exception e) {
            log.error("Error while sending email. Exception: {}", e.getMessage());
        }
    }
}
