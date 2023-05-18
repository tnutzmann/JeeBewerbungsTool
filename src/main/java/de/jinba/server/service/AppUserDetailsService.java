package de.jinba.server.service;

import de.jinba.server.entity.AppUser;
import de.jinba.server.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }

    public Boolean existsByUsername(String username) {
        return appUserRepository.existsByEmail(username);
    }

    public Optional<AppUser> getCurrentAuthenticatedUser(){
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ?
                Optional.of((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                : Optional.empty();
    }
}
