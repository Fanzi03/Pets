package org.example.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataTransferObject {
    Long id;
    String fullName;
    String email;
    String password;
    int age;
}
