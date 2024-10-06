package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.dto.RegisterResponseDTO;
import com.krzysiek.recruiting.exception.UserAlreadyExistsException;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.UserRepository;
import io.jsonwebtoken.JwtException;
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

    public void confirmEmail(String token){
        try {
            // decode token - get email and expired
            // try to find user with this email
            // compare tokens
            // if tokens are not the same - throw error
            // tokens are same - set confirmed on true and delete token
            // send message about success - now user can login
        } catch (Exception ex) {

        }
    }

    //TODO: confirmEmail
    //TODO: login
    //TODO: resetPassword
    // TODO: create endpoint to get this token from email

}
