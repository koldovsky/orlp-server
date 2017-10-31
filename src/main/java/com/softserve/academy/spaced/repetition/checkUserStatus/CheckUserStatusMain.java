package com.softserve.academy.spaced.repetition.checkUserStatus;
import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckUserStatusMain {

    @Autowired
    private UserService userService;

    @Before("@annotation(CheckUserStatus)")
    public void isUserStatusActive() throws UserStatusException{
       userService.getUserStatus();
    }
}
