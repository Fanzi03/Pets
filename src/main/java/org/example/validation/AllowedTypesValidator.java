package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.validation.annotation.AllowedTypes;

import java.util.Arrays;

public class AllowedTypesValidator implements ConstraintValidator<AllowedTypes, String> {
    private String[] allowed;

    @Override
    public void initialize(AllowedTypes constraintAnnotation) {
        this.allowed = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return false;
        return Arrays.stream(allowed).anyMatch(allowedValue -> allowedValue.equalsIgnoreCase(value));
    }
}
