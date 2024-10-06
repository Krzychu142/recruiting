package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.EmailDTO;
import com.krzysiek.recruiting.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
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
        EmailDTO emailDTO = new EmailDTO(
                errorNotificationEmail,
                null,
                null,
                "Error in spring application API: ",
                "Error in spring application API: " + ex.getMessage()
        );
        sendEmail(emailDTO);
    }

    @Async
    public void sendConfirmedLinkEmail(String confirmedLink, String clientApplicationAddress, String to, Long hoursToExpiration){
        EmailDTO emailDTO = new EmailDTO(
                to,
                null,
                null,
                "Confirm your email.",
                String.format("Click into this link to confirm your email: %s/authentication/confirm-email?token=%s\nRemember link is valid only for %s hours.",clientApplicationAddress, confirmedLink, hoursToExpiration)
        );
        sendEmail(emailDTO);
    }

    @Async
    private void sendEmail(EmailDTO emailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(errorNotificationEmail);
            message.setSubject(emailDTO.subject());
            message.setText(emailDTO.message());
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) {
            log.error("Authentication failed while sending email. Exception: {}", e.getMessage());
        } catch (MailSendException e) {
            log.error("Error sending email. Exception: {}", e.getMessage());
        } catch (MailException e) {
            log.error("General mail error occurred. Exception: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while sending email. Exception: {}", e.getMessage());
        }
    }

}
