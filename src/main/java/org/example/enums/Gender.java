package org.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;

public enum Gender {
    WOMAN, MAN;


    @SneakyThrows
    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new JsonProcessingException("Gender value cannot be null or empty") {};
        }

        return switch (value.toUpperCase()) {
            case "WOMAN", "FEMALE" -> WOMAN;
            case "MAN", "MALE" -> MAN;
            default -> throw new JsonProcessingException("Unexpected value: " + value) {};
        };
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
