package com.softserve.academy.spaced.repetition.security.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.DTO.ReCaptchaResponseDto;
import com.softserve.academy.spaced.repetition.security.utils.FacebookAuthUtil;
import com.softserve.academy.spaced.repetition.security.utils.GoogleAuthUtil;
import com.softserve.academy.spaced.repetition.security.utils.JwtTokenUtil;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Service
public class AuthenticationRestService {

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private FacebookAuthUtil facebookAuthUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private GoogleAuthUtil googleAuthUtil;
    @Autowired
    private ReCaptchaApiService reCaptchaApiService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Value("${app.jwt.header}")
    private String tokenHeader;

    public HttpHeaders getAuthHeaders(String email, String password, String captcha, Device device) throws UserStatusException {
        ReCaptchaResponseDto reCaptchaResponseDto = reCaptchaApiService.verify(captcha);
        if (!reCaptchaResponseDto.isSuccess()) {
            throw new BadCredentialsException("reCaptcha");
        }
        Authentication authentication = getAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        validateUser(userDetails);
        String token = jwtTokenUtil.generateToken(userDetails, device);
        return addTokenToHeaderCookie(token);
    }

    public HttpHeaders getFacebookHeaders(String idToken, Device device) throws UserStatusException {
        String graph = facebookAuthUtil.getFBGraph(idToken);
        Map fbProfileData = facebookAuthUtil.getGraphData(graph);
        String email = (String) fbProfileData.get("email");
        if (!facebookAuthUtil.checkIfExistUser(email)) {
            facebookAuthUtil.saveNewFacebookUser(fbProfileData);
        }
        Authentication authentication = getAuthenticationTokenWithoutVerify(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        validateUser(userDetails);
        String token = jwtTokenUtil.generateToken(userDetails, device);

        return addTokenToHeaderCookie(token);
    }

    public HttpHeaders getGoogleHeaders(String idToken, Device device) throws UserStatusException {
        GoogleIdToken googleIdToken = googleAuthUtil.getGoogleIdToken(idToken);
        String email = googleAuthUtil.getEmail(googleIdToken);
        if (!googleAuthUtil.checkIfExistUser(email)) {
            googleAuthUtil.saveNewGoogleUser(googleIdToken);
        }
        Authentication authentication = getAuthenticationTokenWithoutVerify(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        validateUser(userDetails);
        String token = jwtTokenUtil.generateToken(userDetails, device);
        return addTokenToHeaderCookie(token);
    }

    public HttpHeaders getHeadersForRefreshToken(HttpServletRequest request) throws UserStatusException {
        String token = getTokenFromRequest(request);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        validateUser(user);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return addTokenToHeaderCookie(refreshedToken);
        } else {
            throw new BadCredentialsException(messageSource.getMessage("message.exception.tokenNotValid",
                    new Object[]{}, locale));
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenHeader)) {
                    token = cookie.getValue();
                }
            }
        }
        if (token != null) {
            return token;
        } else {
            throw new BadCredentialsException(messageSource.getMessage("message.exception.tokenNotExist",
                    new Object[]{}, locale));
        }
    }

    private HttpHeaders addTokenToHeaderCookie(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", tokenHeader + "=" +
                token + "; Path=/" + "; Expires=" + jwtTokenUtil.getExpirationDateFromToken(token));
        return headers;
    }

    private Authentication getAuthenticationToken(String email, String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password,
                        Collections.singletonList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())))
        );
        return authentication;
    }

    private Authentication getAuthenticationTokenWithoutVerify(String email) {
        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null,
                        Collections.singletonList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())));
        return authentication;
    }


    private void validateUser(UserDetails userDetails) throws UserStatusException {
        User user = userRepository.findUserByAccountEmail(userDetails.getUsername());
        if (user.getAccount().getStatus().isNotActive()) {
            throw new UserStatusException(user.getAccount().getStatus());
        }
        if(!userDetails.isAccountNonLocked()){
            throw new LockedException(messageSource.getMessage("message.exception.accountDeactivated",
                    new Object[]{}, locale));
        }

    }
}
