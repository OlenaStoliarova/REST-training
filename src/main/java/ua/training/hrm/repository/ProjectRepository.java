package ua.training.hrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.hrm.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
