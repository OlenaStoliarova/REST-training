package ua.training.hrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.hrm.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
