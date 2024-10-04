package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.RegisterRequestDTO;
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

    public void register(RegisterRequestDTO registerRequestDTO) throws RuntimeException {
        //System.out.println(registerRequestDTO.email());
        //System.out.println(passwordEncoder.encode(registerRequestDTO.password()));

        Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.email());
        if (optionalUser.isPresent()) {
//            throw new
            System.out.println("Test");
        }

        try {
            User user = new User(registerRequestDTO.email(), passwordEncoder.encode(registerRequestDTO.password()));
            userRepository.save(user);
            System.out.println("User should be inserted");
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while registering the user: \"" + ex.getMessage());
        }

    }

}
