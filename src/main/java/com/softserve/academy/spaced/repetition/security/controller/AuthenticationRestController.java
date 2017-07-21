package com.softserve.academy.spaced.repetition.security.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.softserve.academy.spaced.repetition.security.*;
import com.softserve.academy.spaced.repetition.security.DTO.JwtAuthenticationRequest;
import com.softserve.academy.spaced.repetition.security.DTO.JwtAuthenticationResponse;
import com.softserve.academy.spaced.repetition.security.DTO.ReCaptchaResponseDto;
import com.softserve.academy.spaced.repetition.security.service.JwtSocialService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
public class AuthenticationRestController {
    private final JwtSocialService jwtSocialService;
    private final JwtTokenUtil jwtTokenUtil;
    private final FacebookAuthUtil facebookAuthUtil;
    private final UserDetailsService userDetailsService;
    private final ReCaptchaApiService reCaptchaApiService;

    @Autowired
    public AuthenticationRestController(JwtSocialService jwtSocialService, JwtTokenUtil jwtTokenUtil, FacebookAuthUtil facebookAuthUtil, UserDetailsService userDetailsService, ReCaptchaApiService reCaptchaApiService) {
        this.jwtSocialService = jwtSocialService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.facebookAuthUtil = facebookAuthUtil;
        this.userDetailsService = userDetailsService;
        this.reCaptchaApiService = reCaptchaApiService;
    }

    @Value("${jwt.header}")
    private String tokenHeader;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
        ReCaptchaResponseDto reCaptchaResponseDto = reCaptchaApiService.verify(authenticationRequest.getCaptcha());
        if (!reCaptchaResponseDto.isSuccess()) {
            throw new BadCredentialsException("reCaptcha");
        }
        Authentication authentication = jwtSocialService.getAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        jwtSocialService.setAuthentication(authentication);
        String token = jwtSocialService.generateToken((JwtUser) authentication.getPrincipal(), device);
        HttpHeaders headers = jwtSocialService.addTokenToHeaderCookie(token);
        System.out.println("sss");
        return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "${spring.social.google.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationTokenFromSocial(@RequestBody String idToken, Device device) {
        GoogleIdToken googleIdToken = jwtSocialService.getGoogleIdToken(idToken);
        String email = jwtSocialService.getEmail(googleIdToken);
        jwtSocialService.saveUserIfNotExist(email, googleIdToken);
        Authentication authentication = jwtSocialService.getAuthenticationTokenWithoutVerify(email);
        jwtSocialService.setAuthentication(authentication);
        String token = jwtSocialService.generateToken((JwtUser) authentication.getPrincipal(), device);
        HttpHeaders headers = jwtSocialService.addTokenToHeaderCookie(token);
        return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "${spring.social.facebook.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationTokenFromFacebook(@RequestBody String token, Device device) throws GeneralSecurityException, IOException {

        String graph = facebookAuthUtil.getFBGraph(token);
        Map fbProfileData = facebookAuthUtil.getGraphData(graph);
        String email = (String) fbProfileData.get("email");
        if (!facebookAuthUtil.checkIfExistUser(email)) {
            facebookAuthUtil.saveNewFacebookUser(fbProfileData);
        }
        Authentication authentication = jwtSocialService.getAuthenticationTokenWithoutVerify(email);
        jwtSocialService.setAuthentication(authentication);
        String returnedToken = jwtSocialService.generateToken((JwtUser) authentication.getPrincipal(), device);
        HttpHeaders headers = jwtSocialService.addTokenToHeaderCookie(returnedToken);
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


