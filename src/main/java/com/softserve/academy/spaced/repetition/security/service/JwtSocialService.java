package com.softserve.academy.spaced.repetition.security.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.softserve.academy.spaced.repetition.domain.AuthorityName;
import com.softserve.academy.spaced.repetition.security.GoogleAuthUtil;
import com.softserve.academy.spaced.repetition.security.JwtTokenUtil;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtSocialService {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final GoogleAuthUtil googleAuthUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    public JwtSocialService(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, GoogleAuthUtil googleAuthUtil, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.googleAuthUtil = googleAuthUtil;
        this.authenticationManager = authenticationManager;
    }

    public Authentication getAuthenticationTokenWithoutVerify(String email) {
        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())));
        return authentication;
    }

    public void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication getAuthenticationToken(String email, String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password, Collections.singletonList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())))
        );
        return authentication;
    }

    public String generateToken(UserDetails user, Device device){
        return jwtTokenUtil.generateToken(user, device);
    }

    public HttpHeaders addTokenToHeaderCookie(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", tokenHeader + "=" + token + "; Path=/" + "; Expires=" + jwtTokenUtil.getExpirationDateFromToken(token));
        return headers;
    }

    public GoogleIdToken getGoogleIdToken(String idToken) {
        return googleAuthUtil.getGoogleIdToken(idToken);
    }

    public String getEmail(GoogleIdToken googleIdToken) {
        return googleAuthUtil.getEmail(googleIdToken);
    }

    public void saveUserIfNotExist(String email, GoogleIdToken googleIdToken) {
        if (!googleAuthUtil.checkIfExistUser(email)) {
            googleAuthUtil.saveNewGoogleUser(googleIdToken);
        }
    }


}
