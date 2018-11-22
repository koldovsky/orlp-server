package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO;

public class SendPointsToFriendDTO {
    private String emailFrom;
    private String emailTo;
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
