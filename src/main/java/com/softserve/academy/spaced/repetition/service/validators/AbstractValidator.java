package com.softserve.academy.spaced.repetition.service.validators;

import org.springframework.stereotype.Service;

@Service
public abstract class AbstractValidator<T> {
    private AbstractValidator nextValidator;

    public void setNextValidator(AbstractValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public void doValidate(T obj) {
        validate(obj);
        if (nextValidator != null) {
            nextValidator.doValidate(obj);
        }
    }

    protected abstract void validate(T obj);
}
