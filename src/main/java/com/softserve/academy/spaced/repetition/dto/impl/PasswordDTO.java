package com.softserve.academy.spaced.repetition.dto.impl;

import com.softserve.academy.spaced.repetition.dto.Request;
import com.softserve.academy.spaced.repetition.service.validators.PasswordMatchesAnnotation.PasswordMatches;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.service.validators.ValidationConstants.*;

public class PasswordDTO {

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @Size(min = 1, message = PASSWORD_SIZE_MESSAGE, groups = Request.class)
    @PasswordMatches(groups = Request.class)
    private String currentPassword;

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_MESSAGE, groups = Request.class)
    private String newPassword;

    public PasswordDTO(){
    }

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
