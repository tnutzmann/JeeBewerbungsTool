package de.jinba.server.controller.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

/**
 * This class handles exceptions that are not yet handled by the Spring Boot error handling mechanism.
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @Value("${server.error.include-message}")
    private String includeMessage;

    /**
     * Handles {@link ConstraintViolationException}. This exception is thrown when a constraint is violated.
     *
     * @param e     The exception.
     * @param model The model.
     * @return The name of the error template.
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public String handleConstraintViolationException(ConstraintViolationException e,
                                                     Model model) {
        log.error(e.getMessage());
        StringBuilder s = new StringBuilder();
        if (includeMessage.equals("always")) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            s.append(violations.stream()
                    .findFirst()
                    .orElseThrow()
                    .getMessage());
            if (violations.size() >= 2) {
                s.append("; ...");
            }
        } else {
            s.append("No Message available");
        }
        model.addAttribute("message", s.toString());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        return "error";
    }
}
