package de.jinba.server.service;

import de.jinba.server.dto.RegisterRequest;

public interface RegistrationService {
    void register(RegisterRequest request);
}
