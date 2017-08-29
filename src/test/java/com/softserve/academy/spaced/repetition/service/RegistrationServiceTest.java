package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import com.softserve.academy.spaced.repetition.exceptions.ObjectHasNullFieldsException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegistrationServiceTest {
    private static User userNoNullFields;
    private static User userContainsNullFields;

    @BeforeClass
    public static void setUp() {
        userNoNullFields = new User();
        Person person = new Person();
        Account account = new Account();
        person.setFirstName("testName");
        person.setLastName("testLastName");
        account.setEmail("test@mail.com");
        account.setPassword("12345678");
        userNoNullFields.setPerson(new Person());
        userNoNullFields.setAccount(new Account());
        userContainsNullFields = new User();
        userContainsNullFields.setAccount(account);
        userContainsNullFields.setPerson(null);
    }

//    @Test(expected = ObjectHasNullFieldsException.class)
//    public void nullFieldsValidation_UserContainsFieldsThatIsNull_ObjectHasNullFieldsException() throws ObjectHasNullFieldsException, BlankFieldException, EmailUniquesException {
//        RegistrationService registrationService = new RegistrationService();
//        registrationService.nullFieldsValidation(userContainsNullFields);
//    }
//
//    @Test
//    public void nullFieldsValidation_UserContainsNotNullFields_NormalExecution() throws ObjectHasNullFieldsException, BlankFieldException, EmailUniquesException {
//        RegistrationService registrationService = new RegistrationService();
//        boolean actual = registrationService.nullFieldsValidation(userNoNullFields);
//        boolean expected = true;
//        Assert.assertEquals(actual, expected);
//    }

    @Test
    public void blankFieldsValidation_AllFieldsOfTheUserIsNotNull_ReturnUser() throws Exception {
    }

    @Test
    public void registerNewUser() throws Exception {
    }

    @Test
    public void sendConfirmationEmailMessage() throws Exception {
    }


}