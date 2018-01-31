package com.softserve.academy.spaced.repetition.utils.validators.annotations;

import com.softserve.academy.spaced.repetition.utils.validators.EmailNotExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailNotExistValidator.class})
public @interface EmailNotUsed {
    String message() default "{message.validation.emailAlreadyUsed}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
