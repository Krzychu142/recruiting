package com.krzysiek.recruiting.service;

import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.expiration.hours}")
    private  Long EXPIRATION_DATE_H;
    private static final long MILLISECONDS_IN_AN_HOUR = 1000 * 60 * 60;

    @Value("${jwt.secret.key}")
    private  String JWT_SECRET_KEY;

    @Value("${jwt.algorithm}")
    private  String ALGORITHM;

    public String encodeJWT(String email){
        byte[] decodedKey = Base64.getUrlDecoder().decode(JWT_SECRET_KEY);
        Key key = new SecretKeySpec(decodedKey, ALGORITHM);

        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE_H * MILLISECONDS_IN_AN_HOUR))
                .signWith(key)
                .compact();
    }

    public String extractEmail(@NotBlank(message = "Email cant be empty.") @Email(message = "Email should be a valid email address.") String email){
        return "Info";
    }

}
