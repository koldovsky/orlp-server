package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class NewAccountPasswordDTO {

    private String email;

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @Size(min = PASS_MIN_SIZE, max = PASS_MAX_SIZE, message = PASS_SIZE_MESSAGE, groups = Request.class)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
