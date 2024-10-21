package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.exception.UserNotFoundException;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements  IUserService{

    private final UserRepository userRepository;
    private final ThrowCorrectException throwCorrectException;

    public UserService(UserRepository userRepository, ThrowCorrectException throwCorrectException) {
        this.userRepository = userRepository;
        this.throwCorrectException = throwCorrectException;
    }

    @Override
    public User getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }
}
