package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class NewAccountPasswordDTO {

    private String email;

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(message = "{message.validation.fieldSizeLimits}", min = PASS_MIN_SIZE, max = PASS_MAX_SIZE, groups = Request.class)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
