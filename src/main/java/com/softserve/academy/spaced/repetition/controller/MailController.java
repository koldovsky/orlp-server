package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.MailDTO;
import com.softserve.academy.spaced.repetition.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/contactus")
public class MailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity fromContactUsFromToEmailSender(@RequestBody MailDTO mailDTO){
       LOGGER.debug("Sending request from Contact us form to email");
            mailService.sendRequestFromContactUsFormToEmail(mailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
