package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.ExpiredTokenForVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenForMail extends JwtTokenUtil {
    private static final String USER_EMAIL = "email";
    private static final String DATE_OF_CREATION = "date";

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
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
        return new Date(System.currentTimeMillis() + expiration * 1*60);
    }

    public String decryptToken(String token) throws ExpiredTokenForVerificationException {
        final String email = getEmailFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        if (!isTokenExpired(token)) {
            return email;
        }
        throw new ExpiredTokenForVerificationException();
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

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(DATE_OF_CREATION));
        } catch (Exception e) {
            created = null;
        }
        return created;
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
