package com.softserve.academy.spaced.repetition.service.validator;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.PasswordDTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

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

    private final String FIELD_SIZE_MESSAGE = "Field can't be less than %d and more than %d symbols!";
    @Autowired
    private Validator validator;

    private JwtUser jwtUser;

    private User user = new User(
            new Account("password", "user@gmail.com"),
            new Person("user", "user"),
            new Folder());

    @Before
    public void setUp() {
        jwtUser = mock(JwtUser.class);
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(jwtUser, null));
    }

    @Test
    public void testPasswordSize() {
        final int PASS_MIN_SIZE = ValidationConstants.PASSWORD_MIN_SIZE;
        final int PASS_MAX_SIZE = ValidationConstants.PASSWORD_MAX_SIZE;
        final String PASS_SIZE_MESSAGE = String.format(FIELD_SIZE_MESSAGE, PASS_MIN_SIZE, PASS_MAX_SIZE);

        user.getAccount().setPassword("user");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(PASS_SIZE_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordMatches() {
        final String PASSWORDS_MATCH_MESSAGE = "Password should match with current!";

        when(jwtUser.getUsername()).thenReturn("admin@gmail.com");
        PasswordDTO passwordDTO = new PasswordDTO("incorrectPassword", "administrator");
        Set<ConstraintViolation<PasswordDTO>> violations = validator.validate(passwordDTO, Request.class);
        assertEquals(1, violations.size());
        assertEquals(PASSWORDS_MATCH_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailNotNull() {
        final String NULL_MESSAGE = "Field can not be null!";

        user.getAccount().setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(NULL_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailSize() {
        final int EMAIL_MIN_SIZE = ValidationConstants.EMAIL_MIN_SIZE;
        final int EMAIL_MAX_SIZE = ValidationConstants.EMAIL_MAX_SIZE;
        final String EMAIL_SIZE_MESSAGE = String.format(FIELD_SIZE_MESSAGE, EMAIL_MIN_SIZE, EMAIL_MAX_SIZE);

        user.getAccount().setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(2, violations.size());
        assertTrue(violations.toString().contains(EMAIL_SIZE_MESSAGE));
    }

    @Test
    public void testEmailPattern() {
        final String EMAIL_PATTERN_MESSAGE = "Incorrect format!";

        user.getAccount().setEmail("userGmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_PATTERN_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailExists() {
        final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email already used!";

        user.getAccount().setEmail("admin@gmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Request.class);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_ALREADY_EXISTS_MESSAGE, violations.iterator().next().getMessage());
    }
}
