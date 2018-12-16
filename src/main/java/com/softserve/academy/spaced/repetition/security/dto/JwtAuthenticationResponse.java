package com.softserve.academy.spaced.repetition.security.dto;

public class JwtAuthenticationResponse {
    private String status;

    public JwtAuthenticationResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
