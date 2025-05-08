package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String fullName;
    private String email;
    private int age;
    private List<PetDTO> pets;
}
