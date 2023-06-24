package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * This exception is thrown when an Entity that is already exists is tried to be created.
 * The exception is mapped to the HTTP status code 409 Conflict.
 * It inherits from {@link ResponseStatusException}. Therefore, it will be automatically handled by the
 * Spring Boot error handling mechanism.
 */
public class EntityAlreadyExistsException extends ResponseStatusException {

    public static final HttpStatusCode statusCode = HttpStatus.CONFLICT;

    public EntityAlreadyExistsException() {
        this(null);
    }

    public EntityAlreadyExistsException(String reason) {
        this(reason, null);
    }

    public EntityAlreadyExistsException(String reason, Throwable cause) {
        this(reason, cause, null, null);
    }

    public EntityAlreadyExistsException(String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(statusCode, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
