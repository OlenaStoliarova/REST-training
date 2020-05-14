package ua.training.hrm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoEntityFoundException extends RuntimeException{
    public NoEntityFoundException(String message) {
        super(message);
    }
}
