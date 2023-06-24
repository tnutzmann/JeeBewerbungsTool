package de.jinba.server.repository;

import de.jinba.server.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository provides DB operations for {@link JobApplication}.
 */
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, String> {
}
