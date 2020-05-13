package ua.training.hrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.hrm.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
