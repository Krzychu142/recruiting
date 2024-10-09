package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.*;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.exception.UserAlreadyExistsException;
import com.krzysiek.recruiting.exception.UserNotFoundException;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final String clientApplicationAddress;
    private final ThrowCorrectException throwCorrectException;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTService jwtService, EmailService emailService, @Value("${client.application.address}") String clientApplicationAddress, ThrowCorrectException throwCorrectException){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.clientApplicationAddress = clientApplicationAddress;
        this.throwCorrectException = throwCorrectException;
    }

    public BaseResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.email());
            if (optionalUser.isPresent()) {
                throw new UserAlreadyExistsException("User with this email already exists.");
            }
            String userEmail = registerRequestDTO.email();
            String confirmedToken = jwtService.getLongTermToken(userEmail);
            User user = new User(userEmail, passwordEncoder.encode(registerRequestDTO.password()), confirmedToken);
            User savedUser = userRepository.save(user);
            if (savedUser.getId() == null) {
                throw new RuntimeException("User could not be saved. Please try again.");
            }
            emailService.sendConfirmedLinkEmail(confirmedToken, clientApplicationAddress, userEmail, jwtService.getEXPIRATION_DATE_H());

            return new BaseResponseDTO(
                    "An activation link was sent to the provided email. Confirm to login."
            );
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public BaseResponseDTO confirmEmail(String token){
        try {
            User user = foundUserByToken(token);
            if (!user.getConfirmationToken().equals(token)) {
                throw new ValidationException("Tokens are not equal.");
            }
            int rowsUpdated = userRepository.updateUserConfirmationFields(user.getId(), true);
            if (rowsUpdated != 1) {
                throw new RuntimeException("Something goes wrong while user updating.");
            }
            return new BaseResponseDTO("The email has been successfully confirmed.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public BaseResponseDTO sendResetPasswordToken(String email){
        try {
            User user = getUserByEmail(email);
            String userEmail = user.getEmail();
            String resetPasswordToken = jwtService.getLongTermToken(userEmail);
            int rowsUpdated = userRepository.setUserResetPasswordToken(user.getId(), resetPasswordToken);
            if (rowsUpdated != 1) {
                throw new RuntimeException("Something goes wrong while user updating.");
            }
            emailService.sendResetPasswordLinkEmail(resetPasswordToken, clientApplicationAddress, userEmail, jwtService.getEXPIRATION_DATE_H());
            return new BaseResponseDTO("Email with link and instruction send to provided email.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public BaseResponseDTO resetPassword(String token, ResetPasswordRequestDTO resetPasswordRequestDTO){
        try {
            User user = foundUserByToken(token);
            if (!user.getResetPasswordToken().equals(token)) {
                throw new ValidationException("Tokens are not equal.");
            }
            if (passwordEncoder.matches(resetPasswordRequestDTO.password(), user.getPassword()))  {
                throw new ValidationException("New password should be different than old one.");
            }
            if (!user.getIsConfirmed()){
                // if he was able to reset password it's mean email is valid
                int rowsUpdated = userRepository.updateUserConfirmationFields(user.getId(), true);
                if (rowsUpdated != 1) {
                    throw new RuntimeException("Something goes wrong while user updating.");
                }
            }
            int rowsUpdated = userRepository.updatePassword(user.getId(), passwordEncoder.encode(resetPasswordRequestDTO.password()));
            if (rowsUpdated != 1){
                throw new RuntimeException("Something goes wrong while user updating.");
            }
            return new BaseResponseDTO("Password successful changed.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    //TODO: login
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        try {
            User user = getUserByEmail(loginRequestDTO.email());
            if(!user.getIsConfirmed()){
                throw new ValidationException("Email is not confirmed.");
            }
            if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
                throw new ValidationException("Uncorrected password.");
            }
            String token = jwtService.getAuthToken(user.getEmail(), user.getRole());
            return new LoginResponseDTO(token, "Successfully logged in.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    private User foundUserByToken(String token){
        try {
            if (token.isEmpty()){
                throw new ValidationException("Tokens are empty.");
            }
            String email = jwtService.extractEmail(token);
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("Bad token - owner of this token not found."));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    private User getUserByEmail(String email){
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User with provided email not found."));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    //TODO: logout
    //TODO: refresh-token

}
