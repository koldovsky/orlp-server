package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

public class SendPointsToFriendDTO {

    private String emailFrom;

    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = EMAIL_MIN_SIZE, max = EMAIL_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @Pattern(regexp = EMAIL_PATTERN, message = "{message.validation.emailWrongFormat}", groups = Request.class)
    @NotBlank(message = "{message.validation.fieldNotEmpty}", groups = Request.class)
    private String emailTo;

    @NotNull
    private Integer points;

    public SendPointsToFriendDTO() {
    }

    public SendPointsToFriendDTO(String emailFrom, String emailTo, Integer points) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.points = points;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
