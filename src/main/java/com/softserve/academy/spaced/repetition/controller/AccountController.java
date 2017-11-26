package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.dto.impl.NewAccountPasswordDTO;
import com.softserve.academy.spaced.repetition.dto.impl.RememberingLevelDTO;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private AccountVerificationByEmailService verificationService;

    @GetMapping("/api/private/account/learning-regime")
    public ResponseEntity<LearningRegime> getLearningRegime() throws NotAuthorisedUserException {
        LearningRegime learningRegime = accountService.getLearningRegime();
        return new ResponseEntity<>(learningRegime, HttpStatus.OK);
    }

    @PutMapping("/api/private/account/learning-regime")
    public ResponseEntity updateLearningRegime(@RequestBody String learningRegime) throws NotAuthorisedUserException {
        for (LearningRegime regime : LearningRegime.values()) {
            if (regime.getRegime().equals(learningRegime)) {
                accountService.updateLearningRegime(regime);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
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
    public ResponseEntity updateRememberingLevel(@PathVariable Long levelId,
                                                 @RequestBody String numberOfPostponedDays) throws NotAuthorisedUserException {
        accountService.updateRememberingLevel(levelId, Integer.parseInt(numberOfPostponedDays));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "api/reset/password")
    public ResponseEntity<String> sendResetPasswordMail(@RequestBody String email) {
        LOGGER.debug("Send reset password mail to email: {}", email);
        String accountStatus = accountService.checkAccountStatusAndSendMail(email);
        return ResponseEntity.ok(accountStatus);
    }

    @PutMapping(value = "api/verification/token")
    public ResponseEntity<String> verificationToken(@RequestBody String token) {
        LOGGER.debug("Token verification");
        String emailFromToken = verificationService.getAccountEmail(token);
        return ResponseEntity.ok(emailFromToken);
    }

    @PutMapping(value = "api/create/password")
    public ResponseEntity createNewPasswordForUser(@RequestBody NewAccountPasswordDTO newAccountPasswordDTO) {
        LOGGER.debug("Created new password for: {}", newAccountPasswordDTO.getEmail());
        accountService.createNewAccountPassword(newAccountPasswordDTO.getEmail(), newAccountPasswordDTO.getPassword());
        return ResponseEntity.ok().build();
    }
}
