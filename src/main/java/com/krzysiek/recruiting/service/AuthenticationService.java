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

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void register(RegisterRequestDTO registerRequestDTO) throws RuntimeException, UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.email());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        try {
            // I need to generate there token and pass it as confirmation_token
            //
            User user = new User(registerRequestDTO.email(), passwordEncoder.encode(registerRequestDTO.password()));
            userRepository.save(user);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while registering the user: \"" + ex.getMessage());
        }
    }

    //TODO: login
    //TODO: resetPassword
    //TODO: confirmEmail

}
