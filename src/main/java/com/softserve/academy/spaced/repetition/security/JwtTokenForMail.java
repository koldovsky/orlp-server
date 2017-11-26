package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class JwtTokenForMail extends JwtTokenUtil {
    private static final String USER_EMAIL = "email";
    private static final String DATE_OF_CREATION = "date";
    private static final int EXPIRATION_TO_ONE_DAY = 143;

    @Autowired
    AccountRepository accountRepository;
    @Value("${app.jwt.expiration}")
    private Long expiration;

    @Value("${app.jwt.secret}")
    private String secret;

    public String generateTokenForMail(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_EMAIL, email);
        claims.put(DATE_OF_CREATION, new Date());
        return generateTokenForMail(claims);
    }

    private String generateTokenForMail(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateTokenExpiration())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date generateTokenExpiration() {
        return new Date(System.currentTimeMillis() + expiration * EXPIRATION_TO_ONE_DAY);
    }

    public String decryptToken(String token) {
        final String email = getEmailFromToken(token);
        if (!isTokenExpired(token)) {
            return email;
        }
        throw new NoSuchElementException("No token found");
    }

    public String getAccountEmailFromToken(String token) {
        if (!isTokenForResetPasswordExpired(token)) {
            String email = getEmailFromToken(token);
            if (accountRepository.isEmailExists(email)) {
                return email;
            }
        }
        return "";
    }

    protected Boolean isTokenForResetPasswordExpired(String token) {
        final Date tokenExpiration = getExpirationDateFromToken(token);
        if (tokenExpiration == null) {
            return true;
        }
        return tokenExpiration.before(new Date());
    }

    public String getEmailFromToken(String token) {
        String email;
        try {
            final Claims claims = getClaimsFromToken(token);
            email = String.valueOf(claims.get(USER_EMAIL));
        } catch (Exception e) {
            email = null;
        }
        return email;
    }
}
