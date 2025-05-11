package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
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
}
