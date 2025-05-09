package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.entity.Gender;

@Data
public class PetDataTransferObject {
    private String name;
    private String type;
    private Gender gender;
    private int age;
    @JsonIgnore
    private Long userId;
    private String fullName;
}