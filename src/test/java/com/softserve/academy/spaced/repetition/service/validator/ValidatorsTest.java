package com.softserve.academy.spaced.repetition.service.validator;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class ValidatorsTest {

    @Autowired
    private Validator validator;

    private JwtUser jwtUser;

    private User user = new User(
            new Account("password", "user@gmail.com"),
            new Person("user", "user"),
            new Folder());

    @Before
    public void setUp() throws Exception {
        jwtUser = mock(JwtUser.class);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(jwtUser, null));
    }

    @Test
    public void testPasswordSize() throws Exception {
        user.getAccount().setPassword("user");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(PASS_SIZE_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordMatches() throws Exception {
        when(jwtUser.getUsername()).thenReturn("admin@gmail.com");
        PasswordDTO passwordDTO = new PasswordDTO("admin1", "administrator");
        Set<ConstraintViolation<PasswordDTO>> violations = validator.validate(passwordDTO, Request.class);
        assertEquals(1, violations.size());
        assertEquals(PASS_MATCHES_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailNotNull() throws Exception {
        user.getAccount().setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(NULL_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailSize() throws Exception {
        user.getAccount().setPassword("password");
        user.getAccount().setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().map(p->p.getMessage()).collect(Collectors.toList()).contains(EMAIL_SIZE_MESSAGE));
    }

    @Test
    public void testEmailPattern() throws Exception {
        user.getAccount().setEmail("userGmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_PATTERN_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailExists() throws Exception {
        user.getAccount().setEmail("admin@gmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_NOT_EXIST_MESSAGE, violations.iterator().next().getMessage());
    }
}
