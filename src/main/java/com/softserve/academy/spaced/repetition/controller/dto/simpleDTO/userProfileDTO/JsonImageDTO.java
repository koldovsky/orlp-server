package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO;

public class JsonImageDTO {

    private String imageBase64;

    public JsonImageDTO() {
    }

    public JsonImageDTO(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
