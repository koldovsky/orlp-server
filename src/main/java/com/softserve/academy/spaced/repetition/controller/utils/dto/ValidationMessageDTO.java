package com.softserve.academy.spaced.repetition.controller.utils.dto;

import java.util.List;

public class ValidationMessageDTO {

    private List<FieldErrorDTO> errors;

    public ValidationMessageDTO(List<FieldErrorDTO> errors) {
        this.errors = errors;
    }

    public void addFieldErrorDTO(FieldErrorDTO fieldErrorDTO) {
        errors.add(fieldErrorDTO);
    }

    public List<FieldErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrorDTO> errors) {
        this.errors = errors;
    }
}
