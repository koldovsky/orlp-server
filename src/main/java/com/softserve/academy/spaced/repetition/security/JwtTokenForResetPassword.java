package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.domain.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class JwtTokenForResetPassword extends JwtTokenUtil {
    private static final String ACCOUNT_IDENTIFIER = "identifier";
    private static final String DATE_OF_CREATION = "date";

    @Value("${app.jwt.expiration}")
    private Long expiration;

    @Value("${app.jwt.secret}")
    private String secret;

    public String generateTokenForRestorePassword(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ACCOUNT_IDENTIFIER, account.getIdentifier());
        claims.put(DATE_OF_CREATION, new Date());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    public String decryptToken(String token) {
        final String identifier = getAccountIdentifierFromToken(token);
        if (!isTokenExpired(token)) {
            return identifier;
        }
        throw new NoSuchElementException("Token is invalid");
    }


    public String getAccountIdentifierFromToken(String token) {
        String identifier;
        try {
            final Claims claims = getClaimsFromToken(token);
            identifier = String.valueOf(claims.get(ACCOUNT_IDENTIFIER));
        } catch (Exception e) {
            identifier = null;
        }
        return identifier;
    }

}
