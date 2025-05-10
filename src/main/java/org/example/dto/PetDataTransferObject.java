package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.entity.Gender;

@Data
public class PetDataTransferObject {
//    private Long id;
    private String name;
    private String type;
    private Gender gender;
    private int age;
//    private Long userId;
//    private String ownerName;
}