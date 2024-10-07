package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.exception.ThrowCorrectException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
    private  Long EXPIRATION_DATE_H;
    private static final long MILLISECONDS_IN_AN_HOUR = 1000 * 60 * 60;

    @Value("${jwt.secret.key}")
    private  String JWT_SECRET_KEY;

    @Value("${jwt.algorithm}")
    private  String ALGORITHM;


    public JWTService(ThrowCorrectException throwCorrectException){
        this.throwCorrectException = throwCorrectException;
    }

    public String encodeJWT(String email){
        try {
            byte[] decodedKey = Base64.getUrlDecoder().decode(JWT_SECRET_KEY);
            Key key = new SecretKeySpec(decodedKey, ALGORITHM);

            return Jwts.builder()
                    .subject(email)
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE_H * MILLISECONDS_IN_AN_HOUR))
                    .signWith(key)
                    .compact();
        } catch (Exception ex) {
            throwCorrectException.handleException(ex);
            return null;
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
            throwCorrectException.handleException(ex);
            return null;
        }
    }
}
