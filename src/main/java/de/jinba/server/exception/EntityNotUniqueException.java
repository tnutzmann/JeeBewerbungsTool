package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * This exception is thrown when an Entity is tried to be created violates a unique constraint.
 * The exception is mapped to the HTTP status code 409 Conflict.
 * It inherits from {@link ResponseStatusException}. Therefore, it will be automatically handled by the
 * Spring Boot error handling mechanism.
 */
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
