package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;


    public User registerNewUser(User user) throws BlankFieldException, EmailUniquesException {
        return blankFieldsValidation(user);
    }

    private User blankFieldsValidation(User user) throws BlankFieldException, EmailUniquesException {
        if (user.getAccount().getPassword() != null && user.getAccount().getEmail() != null && user.getPerson().getFirstName()
                != null && user.getPerson().getLastName() != null) {
            return emailUniquesValidation(user);
        } else {
            throw new BlankFieldException();
        }
    }

    private User emailUniquesValidation(User user) throws EmailUniquesException {
        if (userRepository.findUserByAccountEmail(user.getAccount().getEmail().toLowerCase()) == null) {
            return createNewUser(user);
        } else {
            throw new EmailUniquesException();
        }
    }

    private User createNewUser(User user) {
        Set <Authority> listOfAuthorities = new HashSet <>();
        listOfAuthorities.add(new Authority(AuthorityName.ROLE_USER));
        user.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
        user.setFolder(new Folder());
        user.getAccount().setStatus(AccountStatus.INACTIVE);
        user.getAccount().setAuthorities(listOfAuthorities);
        user.getAccount().setEmail(user.getAccount().getEmail().toLowerCase());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        userService.addUser(user);
        return user;
    }

    public void sendConfirmationEmailMessage(User user) throws MailException {
        mailService.sendConfirmationMail(user);
    }
}


