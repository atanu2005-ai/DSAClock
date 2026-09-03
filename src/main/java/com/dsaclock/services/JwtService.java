package com.dsaclock.services;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    //secret key field
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    //method to generate jwt token
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 30 * 60 * 1000);
        return Jwts.builder()
                .subject(email) //subject of the token
                .signWith(key) //signature key
                .issuedAt(now) //creation date
                .expiration(expiry) //expiry date
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
