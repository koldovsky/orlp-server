package com.softserve.academy.spaced.repetition.controller.dto.impl;

public class SetPointsByAdminDTO {

    private String email;

    private Integer points;

    public SetPointsByAdminDTO() {}

    public SetPointsByAdminDTO(String email, Integer points) {
        this.email = email;
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
