package com.softserve.academy.spaced.repetition.service.validators.EmailExistsAnnotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.softserve.academy.spaced.repetition.service.validators.ValidationConstants.EMAIL_EXIST_MESSAGE;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailExistsValidator.class})
public @interface EmailExists {
    String message() default EMAIL_EXIST_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
