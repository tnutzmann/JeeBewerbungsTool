package de.jinba.server.repository;

import de.jinba.server.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This repository provides DB operations for {@link Company}.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    /**
     * This method finds a {@link Company} by its name.
     * @param name The name of the {@link Company}.
     * @return The {@link Company} with the given name.
     */
    Optional<Company> findByName(String name);

    /**
     * This method checks if a {@link Company} with the given name exists.
     * @param name The name of the {@link Company}.
     * @return True if a {@link Company} with the given name exists, false otherwise.
     */
    boolean existsByName(String name);

    /**
     * This method finds a {@link Company} by its admin.
     * @param id The id of the admin.
     * @return The {@link Company} with the given admin.
     */
    Optional<Company> findByAdmin_Id(String id);

}
