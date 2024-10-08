package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.dto.RegisterResponseDTO;
import com.krzysiek.recruiting.dto.ResetPasswordRequestDTO;
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

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.email());
            if (optionalUser.isPresent()) {
                throw new UserAlreadyExistsException("User with this email already exists.");
            }
            String userEmail = registerRequestDTO.email();
            String confirmedToken = jwtService.encodeJWT(userEmail);
            User user = new User(userEmail, passwordEncoder.encode(registerRequestDTO.password()), confirmedToken);
            User savedUser = userRepository.save(user);
            if (savedUser.getId() == null) {
                throw new RuntimeException("User could not be saved. Please try again.");
            }
            emailService.sendConfirmedLinkEmail(confirmedToken, clientApplicationAddress, userEmail, jwtService.getEXPIRATION_DATE_H());

            return new RegisterResponseDTO(
                    savedUser.getId(),
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
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException("No user found with provided email.");
            }
            User user = optionalUser.get();
            String userEmail = user.getEmail();
            String resetPasswordToken = jwtService.encodeJWT(userEmail);
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
            int rowsUpdated = userRepository.updatePassword(user.getId(), passwordEncoder.encode(resetPasswordRequestDTO.password()));
            if (rowsUpdated != 1){
                throw new RuntimeException("Something goes wrong while user updating.");
            }
            return new BaseResponseDTO("Password successful changed.");
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

    //TODO: login
    //TODO: logout
    //TODO: refresh-token

}
