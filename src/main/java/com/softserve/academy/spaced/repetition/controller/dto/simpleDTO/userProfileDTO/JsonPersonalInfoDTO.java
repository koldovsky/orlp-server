package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class JsonPersonalInfoDTO {

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PERSON_FIELD_MIN_SIZE, max = PERSON_FIELD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @Pattern(regexp = SPECIAL_SYMBOLS_PATTERN, message = "{message.validation.fieldCantContainReservedSymbols}", groups = Request.class)
    private String firstName;

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PERSON_FIELD_MIN_SIZE, max = PERSON_FIELD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @Pattern(regexp = SPECIAL_SYMBOLS_PATTERN, message = "{message.validation.fieldCantContainReservedSymbols}", groups = Request.class)
    private String lastName;

    public JsonPersonalInfoDTO() {
    }

    public JsonPersonalInfoDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
