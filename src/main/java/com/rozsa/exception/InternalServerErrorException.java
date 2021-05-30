package com.rozsa.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends NpcDataManagerAuthException {

    public InternalServerErrorException(String message) {
        super(message);
    }
}
