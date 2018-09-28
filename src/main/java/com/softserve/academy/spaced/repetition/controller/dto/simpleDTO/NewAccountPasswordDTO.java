package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class NewAccountPasswordDTO {

    private String email;

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
