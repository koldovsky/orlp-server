package com.softserve.academy.spaced.repetition.DTO.impl;

public class PasswordDTO {
    private String currentPassword;
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

}
