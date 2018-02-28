package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.utils.validators.annotations.PasswordMatches;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class PasswordDTO {

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @PasswordMatches(groups = Request.class)
    private String currentPassword;

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
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
