package de.jinba.server.repository;

import de.jinba.server.entity.AppUserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository provides DB operations for {@link AppUserSkill}.
 */
@Repository
public interface AppUserSkillRepository extends JpaRepository<AppUserSkill, Long> {
}
