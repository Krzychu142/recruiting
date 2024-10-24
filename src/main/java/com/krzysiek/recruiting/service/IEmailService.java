package com.krzysiek.recruiting.service;

public interface IEmailService {
    void sendErrorEmail(Exception ex);
    void sendResetPasswordLinkEmail(String confirmedLink, String clientApplicationAddress, String to, Long hoursToExpiration);
    void sendConfirmedLinkEmail(String resetPasswordToken, String clientApplicationAddress, String to, Long hoursToExpiration);
}
