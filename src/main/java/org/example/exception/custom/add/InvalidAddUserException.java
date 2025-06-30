package org.example.exception.custom.add;

import org.example.exception.custom.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidAddUserException extends BusinessException{


    public InvalidAddUserException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
    
}
