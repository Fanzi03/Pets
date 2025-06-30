package org.example.exception.custom;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    String message;
    LocalDateTime timestamp;  
    int status;
    String path;
}
