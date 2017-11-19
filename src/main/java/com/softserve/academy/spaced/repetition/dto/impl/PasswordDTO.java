package com.softserve.academy.spaced.repetition.dto.impl;

import com.softserve.academy.spaced.repetition.dto.Request;
import com.softserve.academy.spaced.repetition.service.validators.PasswordMatchesAnnotation.PasswordMatches;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import static com.softserve.academy.spaced.repetition.service.validators.ValidationConstants.*;
import static com.softserve.academy.spaced.repetition.service.validators.ValidationConstants.PASSWORD_LENGTH_MESSAGE;

public class PasswordDTO {

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @NotEmpty(message = EMPTY_MESSAGE, groups = Request.class)
    @PasswordMatches(groups = Request.class)
    private String currentPassword;

    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @NotEmpty(message = EMPTY_MESSAGE, groups = Request.class)
    @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_MESSAGE, groups = Request.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof PasswordDTO)){
            return false;
        }
        PasswordDTO passwordDTO = (PasswordDTO) o;
        if (currentPassword != null && newPassword != null && currentPassword.equals(passwordDTO.currentPassword)
                && newPassword.equals(passwordDTO.newPassword)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return currentPassword.hashCode() + newPassword.hashCode();
    }

}
