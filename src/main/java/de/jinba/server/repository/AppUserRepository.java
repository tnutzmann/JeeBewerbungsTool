package de.jinba.server.repository;

import de.jinba.server.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This repository provides DB operations for {@link AppUser}.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    /**
     * This method finds an {@link AppUser} by its email.
     * @param email The email of the {@link AppUser}.
     * @return The {@link AppUser} with the given email.
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * This method checks if an {@link AppUser} with the given email exists.
     * @param email The email of the {@link AppUser}.
     * @return True if an {@link AppUser} with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
