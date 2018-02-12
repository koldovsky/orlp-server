package com.softserve.academy.spaced.repetition.security.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.DTO.ReCaptchaResponseDto;
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

import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.security.utils.JwtTokenUtil;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUserFactory;

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
    @Mock
    UserRepository userRepository;
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
        account.setStatus(AccountStatus.ACTIVE);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(AuthorityName.ROLE_USER));
        account.setAuthorities(authorities);
        JwtUser jwtUser = JwtUserFactory.create(account);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtUser, Collections.singletonList(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.toString())));
        when(reCaptchaApiService.verify(anyString())).thenReturn(responseDto);
        when(authenticationManager.authenticate(any())).thenReturn(authenticationToken);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(jwtUser);
        when(jwtTokenUtil.generateToken(any(), any())).thenReturn("Test token");

        when(userRepository.findUserByAccountEmail(any())).thenReturn(new User(account, new Person(), new Folder()));

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
