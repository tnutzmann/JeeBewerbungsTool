package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotUniqueException extends ResponseStatusException {
    public static final HttpStatus statusCode = HttpStatus.CONFLICT;

    public EntityNotUniqueException() {
        this(null);
    }

    public EntityNotUniqueException(String message) {
        this(message, null);
    }

    public EntityNotUniqueException(String message, Throwable cause) {
        this(message, cause, null, null);
    }

    public EntityNotUniqueException(String message, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(statusCode, message, cause, messageDetailCode, messageDetailArguments);
    }

}
