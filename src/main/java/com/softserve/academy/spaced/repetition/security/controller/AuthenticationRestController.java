package com.softserve.academy.spaced.repetition.security.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.softserve.academy.spaced.repetition.security.*;
import com.softserve.academy.spaced.repetition.security.DTO.JwtAuthenticationRequest;
import com.softserve.academy.spaced.repetition.security.DTO.JwtAuthenticationResponse;
import com.softserve.academy.spaced.repetition.security.DTO.ReCaptchaResponseDto;
import com.softserve.academy.spaced.repetition.security.service.AuthenticationRestService;
import com.softserve.academy.spaced.repetition.security.service.JwtService;
import com.softserve.academy.spaced.repetition.security.service.JwtUserDetailsService;
import com.softserve.academy.spaced.repetition.security.service.ReCaptchaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class AuthenticationRestController {

    @Autowired
    private ReCaptchaApiService reCaptchaApiService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationRestService authenticationRestService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
        ReCaptchaResponseDto reCaptchaResponseDto = reCaptchaApiService.verify(authenticationRequest.getCaptcha());
        if (!reCaptchaResponseDto.isSuccess()) {
            throw new BadCredentialsException("reCaptcha");
        }
        Authentication authentication = jwtService.getAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        jwtService.setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtService.generateToken(userDetails, device);
        HttpHeaders headers = jwtService.addTokenToHeaderCookie(token);
        return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "${spring.social.google.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationTokenFromSocial(@RequestBody String idToken, Device device) {
        GoogleIdToken googleIdToken = jwtService.getGoogleIdToken(idToken);
        String email = jwtService.getEmail(googleIdToken);
        jwtService.saveUserIfNotExist(email, googleIdToken);
        Authentication authentication = jwtService.getAuthenticationTokenWithoutVerify(email);
        jwtService.setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(userDetails, device);
        HttpHeaders headers = jwtService.addTokenToHeaderCookie(token);
        return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "${spring.social.facebook.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationTokenFromFacebook(@RequestBody String token, Device device) throws GeneralSecurityException, IOException {
        HttpHeaders headers = authenticationRestService.getFacebookHeaders(token, device);
        return new ResponseEntity<>(new JwtAuthenticationResponse("OK"), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", tokenHeader + "=" + refreshedToken + "; Path=/" + "; Expires=" + jwtTokenUtil.getExpirationDateFromToken(refreshedToken));
            return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(new JwtAuthenticationResponse("error"));
        }
    }
}


