package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public void getCard(@RequestBody User userFromClient) {
        User user = new User();
        Account account = new Account();
        Person person = new Person();
        account.setEmail(userFromClient.getAccount().getEmail());
        account.setPassword("123456");
        account.setLastPasswordResetDate(Calendar.getInstance().getTime());
        person.setFirstName(userFromClient.getPerson().getFirstName());
        person.setLastName(userFromClient.getPerson().getLastName());
//        Folder folder = new Folder();
//        folder.setDecks(new ArrayList <>());
//        user.setFolder(folder);
        user.setAccount(account);
        user.setPerson(person);
        userService.addUser(user);
        System.out.println(user);
    }
}