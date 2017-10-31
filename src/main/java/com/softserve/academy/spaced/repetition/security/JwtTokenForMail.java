package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.domain.User;
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
public class JwtTokenForMail extends JwtTokenUtil {
    private static final String USER_EMAIL = "email";
    private static final String DATE_OF_CREATION = "date";

    @Value("${app.jwt.expiration}")
    private Long expiration;

    @Value("${app.jwt.secret}")
    private String secret;

    public String generateTokenForMail(User user) {
        Map <String, Object> claims = new HashMap <>();
        claims.put(USER_EMAIL, user.getAccount().getEmail());
        claims.put(DATE_OF_CREATION, new Date());
        return generateTokenForMail(claims);
    }

    private String generateTokenForMail(Map <String, Object> claims) {
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
        final String email = getEmailFromToken(token);
        if (!isTokenExpired(token)) {
            return email;
        }
        throw new NoSuchElementException("No token found");
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
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

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
