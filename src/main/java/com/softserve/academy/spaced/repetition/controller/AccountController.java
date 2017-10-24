package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.LearningRegime;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/api/private/account/learning-regime")
    public ResponseEntity<LearningRegime> getLearningRegime() throws NotAuthorisedUserException {
        LearningRegime learningRegime = accountService.getLearningRegime();
        return new ResponseEntity<>(learningRegime, HttpStatus.OK);
    }

    @PutMapping("/api/private/account/learning-regime")
    public ResponseEntity updateLearningRegime(@RequestBody String learningRegime) throws NotAuthorisedUserException {
        for (LearningRegime regime : LearningRegime.values()) {
            if (regime.regime.equals(learningRegime)) {
                accountService.updateLearningRegime(regime);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
