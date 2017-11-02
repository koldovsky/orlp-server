package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.DTO.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
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

    public void validate(PasswordDTO passwordDTO) throws NotAuthorisedUserException {
        if(passwordDTO.getCurrentPassword().isEmpty()
                || passwordDTO.getNewPassword().isEmpty()
                || !passwordEncoder.matches(passwordDTO.getCurrentPassword(),
                        userService.getAuthorizedUser().getAccount().getPassword())) {
            throw new IllegalArgumentException("Current password must match and fields of password can not be empty");
        }
    }
}