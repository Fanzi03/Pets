package org.example.controller.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public interface ControllerHelper {
    
    default Map<String, Object> returnResponse(
        String indicator, Object body, HttpStatus httpStatus
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("message", indicator);
        response.put("body", body);
        response.put("status", httpStatus);
        return response;
    }
}
