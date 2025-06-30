package org.example.exception.custom;

import org.springframework.http.HttpStatus;

public class NotFoundPetException extends BusinessException{

    public NotFoundPetException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
    
}
