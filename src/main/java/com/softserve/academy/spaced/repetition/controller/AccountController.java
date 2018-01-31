package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.NewAccountPasswordDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.RememberingLevelDTO;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountVerificationByEmailService verificationService;

    @GetMapping("/api/private/account/learning-regime")
    public ResponseEntity<LearningRegime> getLearningRegime() throws NotAuthorisedUserException {
        LearningRegime learningRegime = accountService.getLearningRegime();
        return new ResponseEntity<>(learningRegime, HttpStatus.OK);
    }

    @PutMapping("/api/private/account/learning-regime")
    public ResponseEntity updateLearningRegime(@RequestBody String learningRegime)
            throws NotAuthorisedUserException, IllegalArgumentException {
        accountService.updateLearningRegime(learningRegime);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/private/account/cards-number")
    public ResponseEntity<Integer> getCardsNumber() throws NotAuthorisedUserException {
        return ResponseEntity.ok(accountService.getCardsNumber());
    }

    @PutMapping("/api/private/account/cards-number")
    public ResponseEntity updateCardsNumber(@RequestBody String cardsNumber) throws NotAuthorisedUserException {
        accountService.updateCardsNumber(Integer.parseInt(cardsNumber));
        return ResponseEntity.ok().build();
    }

    @GetMapping("api/private/account/remembering-levels")
    public ResponseEntity<List<RememberingLevelDTO>> getRememberingLevels() throws NotAuthorisedUserException {
        List<RememberingLevel> rememberingLevels = accountService.getRememberingLevels();
        return ResponseEntity.ok(DTOBuilder.buildDtoListForCollection(rememberingLevels, RememberingLevelDTO.class));
    }

    @PutMapping("api/private/account/remembering-levels/{levelId}")
    public ResponseEntity updateRememberingLevel(@PathVariable Long levelId, @RequestBody String numberOfPostponedDays)
            throws NotAuthorisedUserException {
        accountService.updateRememberingLevel(levelId, Integer.parseInt(numberOfPostponedDays));
        return ResponseEntity.ok().build();
    }
}
