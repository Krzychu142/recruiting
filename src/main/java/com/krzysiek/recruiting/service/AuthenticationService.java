package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.*;
import com.krzysiek.recruiting.enums.TokensType;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.exception.UserAlreadyExistsException;
import com.krzysiek.recruiting.exception.UserNotFoundException;
import com.krzysiek.recruiting.mapper.UserMapper;
import com.krzysiek.recruiting.model.RefreshToken;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.RefreshTokenRepository;
import com.krzysiek.recruiting.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
//TODO: encrypt all stored tokens
@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final String clientApplicationAddress;
    private final ThrowCorrectException throwCorrectException;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTService jwtService, EmailService emailService, @Value("${client.application.address}") String clientApplicationAddress, ThrowCorrectException throwCorrectException, RefreshTokenRepository refreshTokenRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.clientApplicationAddress = clientApplicationAddress;
        this.throwCorrectException = throwCorrectException;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userMapper = userMapper;
    }

    //TODO: maybe if email already exists but is not confirmed and still has confirmationToken
    //      generate new token, rewrite token&password and send it one more time?
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
            User user = foundUserByLongTermToken(token);
            if (user.getIsConfirmed()){
                throw new ValidationException("Email already confirmed.");
            }
            if (user.getConfirmationToken().isBlank()){
                throw new ValidationException("No token set for confirmation token.");
            }
            if (!user.getConfirmationToken().equals(token)) {
                throw new ValidationException("Tokens are not equal.");
            }
            int updatedRows = userRepository.updateUserConfirmationFields(user.getId(), true);
            if (updatedRows != 1) {
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
            int updatedRows = userRepository.setUserResetPasswordToken(user.getId(), resetPasswordToken);
            if (updatedRows != 1) {
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
            User user = foundUserByLongTermToken(token);
            if (!user.getResetPasswordToken().equals(token)) {
                throw new ValidationException("Tokens are not equal.");
            }
            if (passwordEncoder.matches(resetPasswordRequestDTO.password(), user.getPassword()))  {
                throw new ValidationException("New password should be different than old one.");
            }
            if (!user.getIsConfirmed()){
                // if he was able to reset password it's mean email is valid
                int updatedRows = userRepository.updateUserConfirmationFields(user.getId(), true);
                if (updatedRows != 1) {
                    throw new RuntimeException("Something goes wrong while user updating.");
                }
            }
            int updatedRows = userRepository.updatePassword(user.getId(), passwordEncoder.encode(resetPasswordRequestDTO.password()));
            if (updatedRows != 1){
                throw new RuntimeException("Something goes wrong while user updating.");
            }
            return new BaseResponseDTO("Password successful changed.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            User user = getUserByEmail(loginRequestDTO.email());
            if (!user.getIsConfirmed()) {
                throw new ValidationException();
            }
            if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
                throw new ValidationException();
            }
            return generateTokensForUser(user, "Successfully logged in.");
        } catch (UserNotFoundException | ValidationException ex) {
            throw new ValidationException("Invalid email or password.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Transactional
    public LoginResponseDTO refreshToken(String refreshTokenValue) {
        try {
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(refreshTokenValue);
            if (optionalRefreshToken.isEmpty()) {
                throw new JwtException("Refresh token could not be found.");
            }
            RefreshToken oldRefreshToken = optionalRefreshToken.get();
            if (oldRefreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new JwtException("Your token has been expired. Please log in again.");
            }
            User user = oldRefreshToken.getUser();
            int deletedResult = refreshTokenRepository.deleteByToken(refreshTokenValue);
            if (deletedResult != 1) {
                throw new RuntimeException("Something goes wrong while refreshing token.");
            }
            return generateTokensForUser(user, "Token refreshed.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    private LoginResponseDTO generateTokensForUser(User user, String message) {
        String newAccessToken = jwtService.getAccessToken(user.getEmail(), user.getRole());
        String newRefreshTokenValue = jwtService.getRefreshToken(user.getEmail());
        RefreshToken newRefreshToken  = new RefreshToken(newRefreshTokenValue, LocalDateTime.now().plusDays(jwtService.getEXPIRATION_DATE_DAYS()), user);
        RefreshToken resultOfSave = refreshTokenRepository.save(newRefreshToken);
        if (resultOfSave.getId() == null) {
            throw new RuntimeException("Refresh token could not be saved.");
        }
        return new LoginResponseDTO(newAccessToken, newRefreshTokenValue, message);
    }

    private User foundUserByLongTermToken(String token){
        try {
            if (token.isBlank()){
                throw new ValidationException("Tokens are empty.");
            }
            TokensType tokenType = jwtService.extractType(token);
            if (tokenType != TokensType.LONG_TERM) {
                throw new ValidationException("Token is not a valid token.");
            }
            String email = jwtService.extractEmail(token);
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("Bad token - owner of this token not found."));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public UserDTO getUserDTOFromSecurityContext() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getPrincipal().toString();
                User user = getUserByEmail(email);
                return userMapper.toDTO(user);
            }

            throw new IllegalStateException("User is not authenticated");
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

    public void logout(String refreshTokenValue) {
        try {
            int deletedTokens = refreshTokenRepository.deleteByToken(refreshTokenValue);
            if (deletedTokens != 1) {
                throw new RuntimeException("Logout failed. Refresh token not found.");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void logoutAllDevices(String accessToken) {
        try {
            String token = accessToken.replace("Bearer ", "");
            String email = jwtService.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            int deletedTokens = refreshTokenRepository.deleteAllByUser(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
