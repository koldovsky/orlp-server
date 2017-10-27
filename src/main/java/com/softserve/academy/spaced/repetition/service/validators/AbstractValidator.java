package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractValidator<T> {
    private AbstractValidator nextValidator;

    public void setNextValidator(AbstractValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public void doValidate(T obj)
            throws EmailUniquesException{
        validate(obj);
        if (nextValidator != null) {
            nextValidator.doValidate(obj);
        }
    }

    protected abstract void validate(T obj)
            throws EmailUniquesException;
}
