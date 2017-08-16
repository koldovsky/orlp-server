package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import com.softserve.academy.spaced.repetition.logger.Logger;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MailService mailService;


    public ResponseEntity <Person> registerNewUser(User user) {
        try {
            blankFieldsValidation(user);
        } catch (BlankFieldException | EmailUniquesException ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            sendConfirmationEmailMessage(user);
        } catch (MailException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user.getPerson(), HttpStatus.CREATED);
    }

    private void blankFieldsValidation(User user) throws BlankFieldException, EmailUniquesException {
        if (user.getAccount().getPassword() != null && user.getAccount().getEmail() != null && user.getPerson().getFirstName()
                != null && user.getPerson().getLastName() != null) {
            emailUniquesValidation(user);
        } else {
            throw new BlankFieldException();
        }
    }

    private void emailUniquesValidation(User user) throws EmailUniquesException {
        if (userRepository.findUserByAccountEmail(user.getAccount().getEmail().toLowerCase()) == null) {
            createNewUser(user);
        } else {
            throw new EmailUniquesException();
        }
    }

    private void createNewUser(User user) {
        Set <Authority> listOfAuthorities = new HashSet <>();
        listOfAuthorities.add(new Authority(AuthorityName.ROLE_USER));
        user.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
        user.setFolder(new Folder());
        user.getAccount().setStatus(AccountStatus.INACTIVE);
        user.getAccount().setAuthorities(listOfAuthorities);
        user.getAccount().setEmail(user.getAccount().getEmail().toLowerCase());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        userService.addUser(user);
    }

    public void sendConfirmationEmailMessage(User user) throws MailException {
        mailService.sendMail(user);
    }
}


