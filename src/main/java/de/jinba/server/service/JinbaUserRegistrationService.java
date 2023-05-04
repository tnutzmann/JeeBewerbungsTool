package de.jinba.server.service;

import de.jinba.server.dto.RegisterRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Role;
import de.jinba.server.exception.ParameterInvalidException;
import de.jinba.server.repository.AppUserRepository;
import de.jinba.server.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JinbaUserRegistrationService implements RegistrationService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(RegisterRequest request) {
        if(!ValidationUtil.isValidPassword(request.getPassword())){
            throw new ParameterInvalidException("Password is invalid");
        }
        if(!ValidationUtil.isValidEmail(request.getEmail())){
            throw new ParameterInvalidException("Email is invalid");
        }
        AppUser appUser =  AppUser.builder()
                .role(Role.DEFAULT_USER)
                .details(request.getDetails())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .locked(false)
                .enabled(true)
                .skills(new ArrayList<>())
                .build();
        appUserRepository.save(appUser);
    }
}