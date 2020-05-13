package ua.training.hrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.hrm.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
