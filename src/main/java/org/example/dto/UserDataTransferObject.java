package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataTransferObject {
    
    Long id;
    @NotBlank(message = "UserName is required")
    @Size(max = 255, min = 5)
    String userName;
    @NotBlank(message = "FullName is required")
    @Size(max = 40, min = 2)
    String fullName;
    @NotBlank(message = "Email must be valid")
    @Size(max = 255, min = 6)
    @Email(message = "Invalid email format")
    String email;
    @Size(max = 40, min = 10)
    @NotBlank(message = "Password is necessary")
    String password;
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Too old for a person")
    Integer age;

    public UserDataTransferObject(
            String userName, String fullName, String email, String password, Integer age) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.age = age;
    }
}
