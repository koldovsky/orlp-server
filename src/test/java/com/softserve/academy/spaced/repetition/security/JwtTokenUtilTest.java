package com.softserve.academy.spaced.repetition.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.softserve.academy.spaced.repetition.security.utils.JwtTokenUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JwtTokenUtil.class)
@PowerMockIgnore({"javax.crypto.*" })
public class JwtTokenUtilTest {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final int FIVE_SECONDS = 5000;
    private static String token;
    private static Claims claims;
    private static Date created;
    private static Date expiration;

    private JwtTokenUtil jwtTokenUtil;

    @BeforeClass
    public static void setUp(){
        created = new Date(System.currentTimeMillis() + FIVE_SECONDS);
        expiration = new Date(System.currentTimeMillis() + 604800000);
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(CLAIM_KEY_USERNAME, "test@gmail.com");
        claimsMap.put(CLAIM_KEY_AUDIENCE, "mobile");
        claimsMap.put(CLAIM_KEY_CREATED, created);
        token = Jwts.builder()
                .setClaims(claimsMap)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, "mySecret")
                .compact();
        claims = Jwts.parser()
                .setSigningKey("mySecret")
                .parseClaimsJws(token)
                .getBody();
    }

    @Test
    public void getUsernameFromToken() throws Exception {
        jwtTokenUtil = PowerMockito.spy(new JwtTokenUtil());
        PowerMockito.doReturn(claims).when(jwtTokenUtil, "getClaimsFromToken", anyObject());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        PowerMockito.verifyPrivate(jwtTokenUtil).invoke("getClaimsFromToken", anyObject());
        assertEquals("test@gmail.com", username);
    }

    @Test
    public void getCreatedDateFromToken() throws Exception {
        jwtTokenUtil = PowerMockito.spy(new JwtTokenUtil());
        PowerMockito.doReturn(claims).when(jwtTokenUtil, "getClaimsFromToken", anyObject());
        Date date = jwtTokenUtil.getCreatedDateFromToken(token);
        PowerMockito.verifyPrivate(jwtTokenUtil).invoke("getClaimsFromToken", anyObject());
        assertEquals(created, date);
    }

    @Test
    public void getExpirationDateFromToken() throws Exception {
        jwtTokenUtil = PowerMockito.spy(new JwtTokenUtil());
        PowerMockito.doReturn(claims).when(jwtTokenUtil, "getClaimsFromToken", anyObject());
        Date date = jwtTokenUtil.getExpirationDateFromToken(token);
        PowerMockito.verifyPrivate(jwtTokenUtil).invoke("getClaimsFromToken", anyObject());
        assertEquals(expiration.toString(), date.toString());
    }

    @Test
    public void generateToken() throws Exception {
        jwtTokenUtil = PowerMockito.spy(new JwtTokenUtil());
        PowerMockito.doReturn("mobile").when(jwtTokenUtil, "generateAudience", anyObject());
        PowerMockito.doCallRealMethod().when(jwtTokenUtil, "generateToken", anyObject());
        PowerMockito.doCallRealMethod().when(jwtTokenUtil, "generateExpirationDate");
        Whitebox.setInternalState(jwtTokenUtil, "secret", "mySecret");
        Whitebox.setInternalState(jwtTokenUtil, "expiration", 1000L);
        String token = jwtTokenUtil.generateToken(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return "test@gmail.com";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        }, null);
        System.out.println(token);
        PowerMockito.verifyPrivate(jwtTokenUtil).invoke("generateAudience", anyObject());
        PowerMockito.verifyPrivate(jwtTokenUtil).invoke("generateExpirationDate");

    }

    @Test
    public void canTokenBeRefreshed() throws Exception {
    }

    @Test
    public void refreshToken() throws Exception {
    }

    @Test
    public void validateToken() throws Exception {
    }
}