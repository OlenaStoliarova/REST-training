package ua.training.hrm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NonUniqueObjectException extends RuntimeException {
    public NonUniqueObjectException(String message) {
        super(message);
    }
}

