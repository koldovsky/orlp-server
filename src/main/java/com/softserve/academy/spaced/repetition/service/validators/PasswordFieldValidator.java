package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.DTO.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.PasswordFieldException;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class PasswordFieldValidator {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;


    public PasswordFieldValidator() {
    }

    public void validate(PasswordDTO passwordDTO) throws PasswordFieldException, NotAuthorisedUserException {
        if(passwordDTO.getCurrentPassword().isEmpty()
                || passwordDTO.getNewPassword().isEmpty()
                || !passwordEncoder.matches(passwordDTO.getCurrentPassword(),
                        userService.getAuthorizedUser().getAccount().getPassword())) {
            throw new PasswordFieldException();
        }
    }
}