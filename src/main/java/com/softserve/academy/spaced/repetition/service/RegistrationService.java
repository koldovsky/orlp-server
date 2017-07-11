package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;


/**
 * Created by Yevhen on 06.07.2017.
 */
@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;


    public ResponseEntity <?> validateAndCreateUser(User user) {
        try {
            blankFieldsValidation(user);
        } catch (BlankFieldException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (IdentityEmailException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);

    }

    public void blankFieldsValidation(User user) {
        if (user.getAccount().getPassword() != null && user.getAccount().getEmail() != null && user.getPerson().getFirstName() != null && user.getPerson().getLastName() != null) {
            identityEmailValidation(user);
        }
        throw new BlankFieldException();
    }

    public void identityEmailValidation(User user) {
        if (userRepository.findUserByAccount_Email(user.getAccount().getEmail()) == null) {
            userService.addUser(user);
        }
        throw new IdentityEmailException();
    }

    public void registerNewUser(User user) {
        user.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
        user.setFolder(new Folder());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        userService.addUser(user);



    }
}
