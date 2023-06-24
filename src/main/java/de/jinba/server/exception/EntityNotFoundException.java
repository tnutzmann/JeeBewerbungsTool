package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * This exception is thrown when an Entity that does not exist is tried to be accessed.
 * The exception is mapped to the HTTP status code 404 Not Found.
 * It inherits from {@link ResponseStatusException}. Therefore, it will be automatically handled by the
 * Spring Boot error handling mechanism.
 */
public class EntityNotFoundException extends ResponseStatusException {

    public static final HttpStatusCode statusCode = HttpStatus.NOT_FOUND;

    public EntityNotFoundException() {
        this(null);
    }

    public EntityNotFoundException(String reason) {
        this(reason, null);
    }

    public EntityNotFoundException(String reason, Throwable cause) {
        this(reason, cause, null, null);
    }

    public EntityNotFoundException(String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(statusCode, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
