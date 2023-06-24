package de.jinba.server.service;

import de.jinba.server.dto.AccountInformationChangeRequest;
import de.jinba.server.dto.EmailChangeRequest;
import de.jinba.server.dto.PasswordChangeRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.exception.EntityNotUniqueException;
import de.jinba.server.exception.ParameterInvalidException;
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

/**
 * Service for handling Jinba user details.
 */
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

    /**
     * Checks if a user with the given username exists.
     *
     * @param username The username to check
     * @return True if a user with the given username exists, false otherwise
     */
    public Boolean existsByUsername(String username) {
        return appUserRepository.existsByEmail(username);
    }

    /**
     * Returns the currently authenticated user.
     *
     * @return The currently authenticated user
     */
    public Optional<AppUser> getCurrentAuthenticatedUser() {
        // Ugly but needed to get the most recent data, since changes are not reflected in the SecurityContext after login
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ?
                appUserRepository.findById(
                        ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .getId())
                : Optional.empty();
    }

    /**
     * Checks if the current requester is authenticated.
     *
     * @return True if the current requester is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * Returns the user with the given id.
     *
     * @param id The id of the user to return
     * @return The user with the given id
     */
    public AppUser getById(String id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Could not find user with id: %s", id)));
    }

    /**
     * Checks if a user with the given id exists.
     *
     * @param id the id to check
     * @return True if a user with the given id exists, false otherwise
     */
    public boolean existsById(String id) {
        return appUserRepository.existsById(id);
    }

    /**
     * Changes the password of the given user.
     *
     * @param appUser               The user to change the password for
     * @param passwordChangeRequest The request containing the new password
     */
    public void changePassword(AppUser appUser,
                               PasswordChangeRequest passwordChangeRequest) {
        // Check if the new password and the confirmation password match
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            throw new ParameterInvalidException("Passwords do not match");
        }
        // Check if the current password is correct
        if (!isCorrectPassword(appUser, passwordChangeRequest.getCurrentPassword())) {
            throw new ParameterInvalidException("Current password is incorrect");
        }
        appUser.setPassword(passwordChangeRequest.getNewPassword());
        appUserRepository.save(appUser);
    }

    /**
     * Checks if the given password is correct for the given user.
     *
     * @param user            The user to check the password for
     * @param currentPassword The password to check
     * @return True if the given password is correct for the given user, false otherwise
     */
    public boolean isCorrectPassword(AppUser user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    /**
     * Changes the account information of the given user.
     *
     * @param user                            The user to change the account information for
     * @param accountInformationChangeRequest The request containing the new account information
     */
    public void changeAccountInformation(AppUser user, AccountInformationChangeRequest accountInformationChangeRequest) {
        user.setFirstName(accountInformationChangeRequest.getFirstName());
        user.setLastName(accountInformationChangeRequest.getLastName());
        user.setDetails(accountInformationChangeRequest.getDetails());
        appUserRepository.save(user);
    }

    /**
     * Changes the email of the given user.
     *
     * @param user               The user to change the email for
     * @param emailChangeRequest The request containing the new email
     */
    public void changeEmail(AppUser user, EmailChangeRequest emailChangeRequest) {
        if (existsByUsername(emailChangeRequest.getEmail())) {
            throw new EntityNotUniqueException("Email already exists");
        }
        user.setEmail(emailChangeRequest.getEmail());
        appUserRepository.save(user);
    }
}
