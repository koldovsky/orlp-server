package com.softserve.academy.spaced.repetition.security.service;


import com.softserve.academy.spaced.repetition.security.FacebookAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationRestService {

    private final JwtSocialService jwtSocialService;
    private final FacebookAuthUtil facebookAuthUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationRestService(JwtSocialService jwtSocialService, FacebookAuthUtil facebookAuthUtil, UserDetailsService userDetailsService) {
        this.jwtSocialService = jwtSocialService;
        this.facebookAuthUtil = facebookAuthUtil;
        this.userDetailsService = userDetailsService;
    }

    public HttpHeaders getFacebookHeaders(String token, Device device) {
        String graph = facebookAuthUtil.getFBGraph(token);
        Map fbProfileData = facebookAuthUtil.getGraphData(graph);
        String email = (String) fbProfileData.get("email");
        if (!facebookAuthUtil.checkIfExistUser(email)) {
            facebookAuthUtil.saveNewFacebookUser(fbProfileData);
        }
        Authentication authentication = jwtSocialService.getAuthenticationTokenWithoutVerify(email);
        jwtSocialService.setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String returnedToken = jwtSocialService.generateToken(userDetails, device);

        return jwtSocialService.addTokenToHeaderCookie(returnedToken);
    }
}
