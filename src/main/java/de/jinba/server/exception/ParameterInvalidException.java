package de.jinba.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ParameterInvalidException extends ResponseStatusException {

    public static final HttpStatusCode statusCode = HttpStatus.BAD_REQUEST;

    public ParameterInvalidException() {
        this(null);
    }

    public ParameterInvalidException(String reason) {
        this(reason, null);
    }

    public ParameterInvalidException(String reason, Throwable cause) {
        this(reason, cause, null, null);
    }

    public ParameterInvalidException(String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(statusCode, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
