package com.rozsa.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends NpcDataManagerAuthException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
