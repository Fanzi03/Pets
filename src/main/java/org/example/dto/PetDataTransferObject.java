package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.entity.Gender;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetDataTransferObject {
    Long id;
    String name;
    String type;
    Gender gender;
    int age;
    String ownerName;
}