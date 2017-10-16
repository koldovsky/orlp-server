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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordDTO)) return false;

        PasswordDTO passwordDTO = (PasswordDTO) o;
        if (currentPassword != null && newPassword != null && currentPassword.equals(passwordDTO.currentPassword) && newPassword.equals(passwordDTO.newPassword)){ return true;}
        return false;
    }

    @Override
    public int hashCode() {
        return currentPassword.hashCode() + newPassword.hashCode();
    }

}
