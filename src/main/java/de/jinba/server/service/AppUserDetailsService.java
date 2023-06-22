package de.jinba.server.service;

import de.jinba.server.dto.AccountInformationChangeRequest;
import de.jinba.server.dto.EmailChangeRequest;
import de.jinba.server.dto.PasswordChangeRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }

    public Boolean existsByUsername(String username) {
        return appUserRepository.existsByEmail(username);
    }

    public Optional<AppUser> getCurrentAuthenticatedUser(){
        // Ugly but needed to get the most recent data, since changes are not reflected in the SecurityContext after login
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ?
                appUserRepository.findById(
                        ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .getId())
                : Optional.empty();
    }

    public boolean isAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public AppUser getById(String id){
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Could not find user with id: %s", id)));
    }

    public boolean existsById(String id){
        return appUserRepository.existsById(id);
    }

    public void changePassword(AppUser appUser,
                               PasswordChangeRequest passwordChangeRequest) {
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }
        if (!isCorrectPassword(appUser, passwordChangeRequest.getCurrentPassword())) {
            throw new IllegalStateException("Current password is incorrect");
        }
        appUser.setPassword(passwordChangeRequest.getNewPassword());
        appUserRepository.save(appUser);
    }

    public boolean isCorrectPassword(AppUser user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public void changeAccountInformation(AppUser user, AccountInformationChangeRequest accountInformationChangeRequest) {
        user.setFirstName(accountInformationChangeRequest.getFirstName());
        user.setLastName(accountInformationChangeRequest.getLastName());
        user.setDetails(accountInformationChangeRequest.getDetails());
        appUserRepository.save(user);
    }

    public void changeEmail(AppUser user, EmailChangeRequest emailChangeRequest) {
        if (existsByUsername(emailChangeRequest.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }
        user.setEmail(emailChangeRequest.getEmail());
        appUserRepository.save(user);
    }
}
