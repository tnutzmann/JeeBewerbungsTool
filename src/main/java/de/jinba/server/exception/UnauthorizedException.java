package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {

    public static final HttpStatusCode statusCode = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
        this(null);
    }

    public UnauthorizedException(String reason) {
        this(reason, null);
    }

    public UnauthorizedException(String reason, Throwable cause) {
        this(reason, cause, null, null);
    }

    public UnauthorizedException(String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(statusCode, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
