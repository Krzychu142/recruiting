package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.enums.Role;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

    private final ThrowCorrectException throwCorrectException;

    @Getter
    @Value("${jwt.expiration.hours}")
    private Long EXPIRATION_DATE_H;

    @Getter
    @Value("${jwt.expiration.minutes}")
    private Long EXPIRATION_DATE_MINUTES;

    @Getter
    @Value("${jwt.expiration.days}")
    private Long EXPIRATION_DATE_DAYS;

    private static final long MILLISECONDS_IN_A_MINUTE = 1000 * 60;
    private static final long MILLISECONDS_IN_AN_HOUR = MILLISECONDS_IN_A_MINUTE * 60;
    private static final long MILLISECONDS_IN_A_DAY = MILLISECONDS_IN_AN_HOUR * 24;

    @Value("${jwt.secret.key}")
    private  String JWT_SECRET_KEY;

    @Value("${jwt.algorithm}")
    private  String ALGORITHM;


    public JWTService(ThrowCorrectException throwCorrectException){
        this.throwCorrectException = throwCorrectException;
    }

    public String getLongTermToken(String email){
        return encodeToken(email, null, EXPIRATION_DATE_H, MILLISECONDS_IN_AN_HOUR);
    }

    public String getAccessToken(String email, Role role){
        return encodeToken(email, role, EXPIRATION_DATE_MINUTES, MILLISECONDS_IN_A_MINUTE);
    }

    public String getRefreshToken(String email){
        return encodeToken(email, null, EXPIRATION_DATE_DAYS, MILLISECONDS_IN_A_DAY);
    }

    private String encodeToken(String email, Role role, Long expirationDateTime, Long millisecondsMultiplier) {
        try {
            byte[] decodedKey = Base64.getUrlDecoder().decode(JWT_SECRET_KEY);
            Key key = new SecretKeySpec(decodedKey, ALGORITHM);

            var builder = Jwts.builder()
                    .subject(email)
                    .expiration(new Date(System.currentTimeMillis() + expirationDateTime * millisecondsMultiplier))
                    .signWith(key);


            if (role != null) {
                builder.claim("role", role.name());
            }

            return builder.compact();
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public String extractEmail(String JWTToken) {
        try {
            byte[] decodedKey = Base64.getUrlDecoder().decode(JWT_SECRET_KEY);
            SecretKey secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(JWTToken)
                    .getPayload();
            return claims.getSubject();

        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }
}
