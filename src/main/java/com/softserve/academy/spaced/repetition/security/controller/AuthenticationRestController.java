package com.softserve.academy.spaced.repetition.security.controller;

import com.softserve.academy.spaced.repetition.security.DTO.JwtAuthenticationRequest;
import com.softserve.academy.spaced.repetition.security.DTO.JwtAuthenticationResponse;
import com.softserve.academy.spaced.repetition.security.service.AuthenticationRestService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class AuthenticationRestController {

    @Autowired
    private AuthenticationRestService authenticationRestService;

    @Auditable(action = AuditingAction.SIGN_IN)
    @RequestMapping(value = "${app.jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, Device device)
            throws AuthenticationException, UserStatusException {
        HttpHeaders headers = authenticationRestService.getAuthHeaders(authenticationRequest.getUsername(),
                authenticationRequest.getPassword(), authenticationRequest.getCaptcha(), device);
        return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.SIGN_IN_VIA_GOOGLE)
    @RequestMapping(value = "${app.social.google.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationTokenFromSocial(@RequestBody String idToken,
                                                                                         Device device) throws UserStatusException {
        HttpHeaders headers = authenticationRestService.getGoogleHeaders(idToken, device);
        return new ResponseEntity<>(new JwtAuthenticationResponse("Ok"), headers, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.SIGN_IN_VIA_FACEBOOK)
    @RequestMapping(value = "${app.social.facebook.path}", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationTokenFromFacebook(@RequestBody String token,
                                                                                           Device device)
            throws GeneralSecurityException, IOException, UserStatusException {
        HttpHeaders headers = authenticationRestService.getFacebookHeaders(token, device);
        return new ResponseEntity<>(new JwtAuthenticationResponse("OK"), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "${app.jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity refreshAndGetAuthenticationToken(HttpServletRequest request) throws UserStatusException {
        HttpHeaders headers = authenticationRestService.getHeadersForRefreshToken(request);
        return new ResponseEntity<>(new JwtAuthenticationResponse("OK"), headers, HttpStatus.OK);
    }
}


