package com.rozsa.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NpcDataManagerAuthException extends RuntimeException {

    public NpcDataManagerAuthException(String message) {
        super(message);
    }
}
