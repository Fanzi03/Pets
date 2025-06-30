package org.example.exception.custom;

import org.springframework.http.HttpStatus;

public class NotFoundUserException extends BusinessException{

    public NotFoundUserException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
    

}
