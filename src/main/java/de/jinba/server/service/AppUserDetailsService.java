package de.jinba.server.service;

import de.jinba.server.dto.AccountInformationChangeRequest;
import de.jinba.server.dto.EmailChangeRequest;
import de.jinba.server.dto.PasswordChangeRequest;
import de.jinba.server.entity.AppUser;
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

    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        AppUser currentUser = getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to change password"));
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }
        if (!isCorrectPassword(passwordChangeRequest.getCurrentPassword())) {
            throw new IllegalStateException("Current password is incorrect");
        }
        currentUser.setPassword(passwordChangeRequest.getNewPassword());
        appUserRepository.save(currentUser);
    }

    public boolean isCorrectPassword(String currentPassword) {
        AppUser currentUser = getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to change password"));
        return passwordEncoder.matches(currentPassword, currentUser.getPassword());
    }

    public void changeAccountInformation(AccountInformationChangeRequest accountInformationChangeRequest) {
        AppUser currentUser = getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to change account information"));
        currentUser.setFirstName(accountInformationChangeRequest.getFirstName());
        currentUser.setLastName(accountInformationChangeRequest.getLastName());
        currentUser.setDetails(accountInformationChangeRequest.getDetails());
        appUserRepository.save(currentUser);
    }

    public void changeEmail(EmailChangeRequest emailChangeRequest) {
        AppUser currentUser = getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to change email"));
        if (existsByUsername(emailChangeRequest.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }
        currentUser.setEmail(emailChangeRequest.getEmail());
        appUserRepository.save(currentUser);
    }
}
