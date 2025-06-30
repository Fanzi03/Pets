package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

import org.example.annotation.AllowedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AllowedTypesValidator implements ConstraintValidator<AllowedTypes, String> {

    @Autowired
    Environment environment;

    String configKey;
    String[] allowed;

    @Override
    public void initialize(AllowedTypes constraintAnnotation) {
        this.allowed = constraintAnnotation.value();
        this.configKey = constraintAnnotation.configKey();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return false;
        String[] allowedValues = getAllowedValues();

        return Arrays.stream(allowedValues).anyMatch(allowedValue -> allowedValue.equalsIgnoreCase(value));
    }

    private String[] getAllowedValues(){
        if(!configKey.isEmpty()){
            String envValue = environment.getProperty(configKey);
            
            if(envValue != null && !envValue.trim().isEmpty()){
                return envValue.split(",");
            }
        }

        return allowed;
    }
}
