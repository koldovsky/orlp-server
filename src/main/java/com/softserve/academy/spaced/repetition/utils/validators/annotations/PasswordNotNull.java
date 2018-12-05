package com.softserve.academy.spaced.repetition.utils.validators.annotations;

import com.softserve.academy.spaced.repetition.utils.validators.PasswordNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordNotNullValidator.class})
public @interface PasswordNotNull {
    String message() default "{message.validation.fieldNotNull}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
