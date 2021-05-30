package com.rozsa.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class InvalidArgsException extends NpcDataManagerAuthException {

    public InvalidArgsException(String message) {
        super(message);
    }
}
