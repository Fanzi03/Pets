package org.example.exception.custom.update;

import org.example.exception.custom.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidPetUpdateException extends BusinessException{
    public InvalidPetUpdateException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
