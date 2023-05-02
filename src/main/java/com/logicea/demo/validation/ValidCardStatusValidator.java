package com.logicea.demo.validation;

import com.logicea.demo.util.CardStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCardStatusValidator implements ConstraintValidator<ValidCardStatus, String> {

    @Override
    public void initialize(ValidCardStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null){
            return true;
        }
        return CardStatus.getAllStatusesLowerCase().contains(value.toLowerCase());
    }
}
