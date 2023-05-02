package com.logicea.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidCardStatusValidator.class})
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCardStatus {

    String message() default "Invalid card status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}