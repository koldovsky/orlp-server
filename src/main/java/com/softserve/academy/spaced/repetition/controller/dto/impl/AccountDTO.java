package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import org.springframework.hateoas.Link;

import java.util.List;

public class AccountDTO extends DTO<Account> {

    public AccountDTO(Account entity) {
        super(entity);
    }

    public AccountDTO(Account entity, Link link) {
        super(entity, link);
    }

    public LearningRegime getLearningRegime(){
        return getEntity().getLearningRegime();
    }

    public Integer getCardsNumber(){
        return getEntity().getCardsNumber();
    }

    public List<RememberingLevel> getRememberingLevels(){
        return getEntity().getRememberingLevels();
    }




}
