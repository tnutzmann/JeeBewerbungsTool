package de.jinba.server.repository;

import de.jinba.server.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByName(String name);

    boolean existsByName(String name);
    Optional<Company> findByAdmin_Id(String id);

}
