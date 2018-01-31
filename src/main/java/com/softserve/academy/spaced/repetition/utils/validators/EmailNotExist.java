package com.softserve.academy.spaced.repetition.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailNotExistValidator.class})
public @interface EmailNotExist {
    String message() default "{message.exception.emailNotExists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
