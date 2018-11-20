package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO;

public class SendPointsToFriendDTO {
    private String emailFrom;
    private String emailTo;
    private int points;

    public SendPointsToFriendDTO() {
    }

    public SendPointsToFriendDTO(String emailFrom, String emailTo, int points) {
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
