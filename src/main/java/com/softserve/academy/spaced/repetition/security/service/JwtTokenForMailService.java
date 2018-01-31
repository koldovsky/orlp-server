package com.softserve.academy.spaced.repetition.security.service;

import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.security.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtTokenForMailService extends JwtTokenUtil {
    private static final String USER_EMAIL = "email";
    private static final String DATE_OF_CREATION = "date";
    private static final int EXPIRATION_TO_ONE_DAY = 143;

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
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

    public String decryptToken(String token) {
        final String email = getEmailFromToken(token);
        if (!isTokenExpired(token)) {
            return email;
        }
        throw new NoSuchElementException(messageSource.getMessage("message.exception.tokenNotFound",
                new Object[]{}, locale));
    }

    public String getAccountEmailFromToken(String token) {
        Date tokenExpiration;
        Date tokenDateCreation;
        String accountEmail;
        try {
            final Claims claims = getClaimsFromToken(token);
            accountEmail = String.valueOf(claims.get(USER_EMAIL));
            tokenExpiration = claims.getExpiration();
            tokenDateCreation = new Date((Long) claims.get(DATE_OF_CREATION));
        } catch (NullPointerException e) {
            return "";
        }
        if (isTokenValid(tokenDateCreation, tokenExpiration, accountEmail)) {
            return accountEmail;
        }
        return "";
    }

    private Boolean isTokenValid(Date tokenDateCreation, Date tokenExpiration, String accountEmail) {
        return (!isTokenUsed(tokenDateCreation, accountEmail) && !isTokenExpired(tokenExpiration));
    }

    private Boolean isTokenUsed(Date created, String email) {
        Date lastPasswordReset = accountRepository.getLastPasswordResetDate(email);
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    protected Boolean isTokenExpired(Date tokenExpiration) {
        return tokenExpiration.before(new Date());
    }
}
