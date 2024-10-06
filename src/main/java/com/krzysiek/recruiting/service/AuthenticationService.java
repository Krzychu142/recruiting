package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.dto.RegisterResponseDTO;
import com.krzysiek.recruiting.exception.UserAlreadyExistsException;
import com.krzysiek.recruiting.exception.UserNotFoundException;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaSystemException;
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

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTService jwtService, EmailService emailService, @Value("${client.application.address}") String clientApplicationAddress){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.clientApplicationAddress = clientApplicationAddress;
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws RuntimeException, UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.email());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        try {
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
        } catch (JpaSystemException ex) {
            throw new RuntimeException("JPA system error: " + ex.getMessage(), ex);
        } catch (JwtException ex) {
           throw new JwtException(ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while registering the user: \"" + ex.getMessage());
        }
    }

    public BaseResponseDTO confirmEmail(String token){
        try {
            String email = jwtService.extractEmail(token);
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException("Bad token - owner of this token not found.");
            }
            User user = optionalUser.get();
            if (!user.getConfirmationToken().equals(token)) {
                throw new ValidationException("Tokens are not equal.");
            }
            Long userId = user.getId();
            int rowsUpdated = userRepository.updateUserFields(userId, true);
            if (rowsUpdated != 1) {
                throw new RuntimeException("Something goes wrong while user updating.");
            }
            return new BaseResponseDTO("The email has been successfully confirmed.");
        } catch (ValidationException ex) {
            throw new ValidationException(ex.getMessage(), ex);
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException(ex.getMessage());
        } catch (JpaSystemException ex) {
            throw new RuntimeException("JPA system error: " + ex.getMessage(), ex);
        } catch (JwtException ex) {
            throw new JwtException(ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while confirming email: \"" + ex.getMessage());
        }
    }

    //TODO: resetPassword
    public BaseResponseDTO sendResetPasswordToken(String email){
        try {
            BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
            // try to found user by email
            // if not found throw error
            // generate new JWTtoken and set as reset password token
            //TODO: add property reset_password_token into User model
            // set this token and send via email
            // DONE
            return  baseResponseDTO;
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while reset password process: \"" + ex.getMessage());
        }
    }

    //TODO: login
    //TODO: logout

}
