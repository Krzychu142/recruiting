package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.exception.UserAlreadyExistsException;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTService jwtService){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequestDTO registerRequestDTO) throws RuntimeException, UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.email());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        try {
            // TODO: generate confirmed token, and save it (it should be equal only 2h for example - that's also good point to create CRON job or trigger to handled not confirmed accounts)
            // TODO: send link via email
            // TODO: create endpoint to get this token from email
            // TODO: make validation of token
            String userEmail = registerRequestDTO.email();
            String confirmedToken = jwtService.encodeJWT(userEmail);
//            System.out.println(jwtService.encodeJWT(registerRequestDTO.email()));
            User user = new User(userEmail, passwordEncoder.encode(registerRequestDTO.password()), confirmedToken);
            userRepository.save(user);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while registering the user: \"" + ex.getMessage());
        }
    }

    //TODO: login
    //TODO: resetPassword
    //TODO: confirmEmail

}
