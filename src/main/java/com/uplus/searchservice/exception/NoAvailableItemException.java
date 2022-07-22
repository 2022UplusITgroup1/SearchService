package com.uplus.searchservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoAvailableItemException extends RuntimeException {
    public NoAvailableItemException(String message) {
        super(message);
    }
}
