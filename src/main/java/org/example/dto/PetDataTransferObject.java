package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.example.entity.enums.Gender;
import org.example.validation.annotation.AllowedTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetDataTransferObject {
    Long id;
    @Size(min = 2, max = 30)
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Type is required")
    @AllowedTypes(value = { "cat", "dog", "bear", "parrot" }, message = "Type must be exist")
    String type;
    @NotNull(message = "Gender is required")
    Gender gender;
    @NotNull(message = "Age is required, please write approximately")
    @Min(value = 0, message = "age must be positive")
    @Max(value = 40, message = "Too old for a pet")
    Integer age;
    String ownerName;

    public PetDataTransferObject(
            String name, String type, Gender gender, Integer age, String ownerName) {
        this.name = name;
        this.type = type;
        this.gender = gender;
        this.age = age;
        this.ownerName = ownerName;
    }
}
