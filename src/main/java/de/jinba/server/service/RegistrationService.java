package de.jinba.server.service;

/**
 * Service for handling registration.
 *
 * @param <T> Type of registration request.
 */
public interface RegistrationService<T> {
    /**
     * Registers user.
     *
     * @param request Request containing user data.
     */
    void register(T request);
}
