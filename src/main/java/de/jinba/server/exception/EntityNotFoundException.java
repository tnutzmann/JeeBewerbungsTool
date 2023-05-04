package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFoundException extends ResponseStatusException {

    public static final HttpStatusCode statusCode = HttpStatus.NOT_FOUND;

    public EntityNotFoundException() {
        this(null);
    }

    public EntityNotFoundException(String reason) {this(reason, null);}

    public EntityNotFoundException(String reason, Throwable cause) {
        this(reason, cause, null, null);
    }

    public EntityNotFoundException(String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(statusCode, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
