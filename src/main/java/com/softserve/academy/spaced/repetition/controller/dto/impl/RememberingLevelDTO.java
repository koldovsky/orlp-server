package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;

public class RememberingLevelDTO extends DTO<RememberingLevel> {

    public RememberingLevelDTO(RememberingLevel rememberingLevel) {
        super(rememberingLevel);
    }

    public Long getLevelId() {
        return getEntity().getId();
    }

    public Integer getOrderNumber() {
        return getEntity().getOrderNumber();
    }

    public String getName() {
        return getEntity().getName();
    }

    public Integer getNumberOfPostponedDays() {
        return getEntity().getNumberOfPostponedDays();
    }
}
