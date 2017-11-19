package com.softserve.academy.spaced.repetition.service.validators.PasswordMatchesAnnotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordMatchesValidator.class})
public @interface PasswordMatches {
    String message() default "Password should match with current";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
