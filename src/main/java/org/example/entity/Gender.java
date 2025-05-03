package org.example.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    WOMAN, MAN;


    @JsonCreator
    public static Gender fromString(String value){
        return switch (value.toUpperCase()) {
            case "WOMAN", "FEMALE" -> WOMAN;
            case "MAN", "MALE" -> MAN;
            default -> throw new IllegalStateException("Unexpected value: " + value.toUpperCase());
        };
    }
}
