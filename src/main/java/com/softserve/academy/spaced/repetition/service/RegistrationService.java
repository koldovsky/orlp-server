package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
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

    User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;


    public ResponseEntity <User> validateAndCreateUser(User userFromForm) {
        this.user = userFromForm;
        try {
            blankFieldsValidation();
        } catch (BlankFieldException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (EmailUniquesException ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity <User>(user, HttpStatus.CREATED);

    }

    public void blankFieldsValidation() throws BlankFieldException, EmailUniquesException {
        if (user.getAccount().getPassword() != null && user.getAccount().getEmail() != null && user.getPerson().getFirstName()
                != null && user.getPerson().getLastName() != null) {
            emailUniquesValidation();
        } else {
            throw new BlankFieldException();
        }
    }

    public void emailUniquesValidation() throws EmailUniquesException {
        if (userRepository.findUserByAccount_Email(user.getAccount().getEmail()) == null) {
            registerNewUser();
        } else {
            throw new EmailUniquesException();
        }
    }

    public void registerNewUser() {
        user.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
        user.setFolder(new Folder());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        userService.addUser(user);


    }
}
