package de.jinba.server.repository;

import de.jinba.server.entity.JobOfferSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferSkillRepository extends JpaRepository<JobOfferSkill, Long> {
}
