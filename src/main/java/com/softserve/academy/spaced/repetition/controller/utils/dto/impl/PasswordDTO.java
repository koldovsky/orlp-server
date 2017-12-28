package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import com.softserve.academy.spaced.repetition.utils.validators.PasswordMatches;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class PasswordDTO {

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @Size(min = 1, message = PASS_SIZE_MESSAGE, groups = Request.class)
    @PasswordMatches(groups = Request.class)
    private String currentPassword;

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @Size(min = PASS_MIN_SIZE, max = PASS_MAX_SIZE, message = PASS_SIZE_MESSAGE, groups = Request.class)
    private String newPassword;

    public PasswordDTO(String currentPassword, String newPassword){
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
