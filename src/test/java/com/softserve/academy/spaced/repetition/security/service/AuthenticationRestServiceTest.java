package com.softserve.academy.spaced.repetition.security.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.Rollback;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Authority;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.security.DTO.ReCaptchaResponseDto;
import com.softserve.academy.spaced.repetition.security.JwtTokenUtil;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import com.softserve.academy.spaced.repetition.security.JwtUserFactory;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(AuthenticationRestService.class)
@Rollback
public class AuthenticationRestServiceTest {

    @Mock
    private ReCaptchaApiService reCaptchaApiService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private AuthenticationRestService restService;

    @Test
    public void getAuthHeaders() throws Exception {
        String email = "test@gmail.com";
        String password = "";
        String captcha = "test";
        ReCaptchaResponseDto responseDto = new ReCaptchaResponseDto();
        responseDto.setSuccess(true);
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(AuthorityName.ROLE_USER));
        account.setAuthorities(authorities);
        JwtUser jwtUser = JwtUserFactory.create(account);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtUser, Collections.singletonList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())));
        when(reCaptchaApiService.verify(anyString())).thenReturn(responseDto);
        when(authenticationManager.authenticate(any())).thenReturn(authenticationToken);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(jwtUser);
        when(jwtTokenUtil.generateToken(any(), any())).thenReturn("Test token");
        HttpHeaders headers = restService.getAuthHeaders(email, password, captcha, null);
        assertTrue(headers.containsKey("Set-Cookie"));

    }

    @Test
    public void getFacebookHeaders() throws Exception {
    }

    @Test
    public void getGoogleHeaders() throws Exception {
    }

    @Test
    public void getHeadersForRefreshToken() throws Exception {
    }
}
