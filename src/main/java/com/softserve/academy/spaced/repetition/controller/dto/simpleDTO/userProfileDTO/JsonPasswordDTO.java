package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.PASSWORD_MAX_SIZE;
import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.PASSWORD_MIN_SIZE;

public class JsonPasswordDTO {

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    private String currentPassword;

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    private String newPassword;

    public JsonPasswordDTO() {
    }

    public JsonPasswordDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
