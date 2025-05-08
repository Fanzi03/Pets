package org.example.dto;
import lombok.Data;

import org.example.entity.Gender;



@Data
public class PetDTO {
    private String name;
    private String type;
    private Gender gender;
    private int age;
    private String owner;
}
