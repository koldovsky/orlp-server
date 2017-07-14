package com.softserve.academy.spaced.repetition.security.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.security.GoogleAuthUtil;
import com.softserve.academy.spaced.repetition.security.JwtAuthenticationRequest;
import com.softserve.academy.spaced.repetition.security.JwtTokenUtil;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

@CrossOrigin
@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private GoogleAuthUtil googleAuthUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<String> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Set-Cookie", "Authentication="+token);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "${spring.social.google.path}", method = RequestMethod.POST)
    public ResponseEntity<String> createAuthenticationTokenFromSocial(@RequestBody String idToken, Device device) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken = googleAuthUtil.getGoogleIdToken(idToken);
        String email = googleAuthUtil.getEmail(googleIdToken);
        if(!googleAuthUtil.checkIfExistUser(email)){
            googleAuthUtil.saveNewGoogleUser(googleIdToken);
        }
        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, Arrays.asList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Set-Cookie", "Authentication="+token);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<String> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Set-Cookie", "Authentication="+refreshedToken);
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
