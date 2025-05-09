package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDataTransferObject {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private int age;
    private List<PetDataTransferObject> pets;
}
