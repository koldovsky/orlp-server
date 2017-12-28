package com.softserve.academy.spaced.repetition.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.EMAIL_NOT_EXIST_MESSAGE;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailNotExistValidator.class})
public @interface EmailNotExist {
    String message() default EMAIL_NOT_EXIST_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
