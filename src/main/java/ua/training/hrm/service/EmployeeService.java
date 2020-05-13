package ua.training.hrm.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.repository.EmployeeRepository;
import ua.training.hrm.service.exception.NoEntityFoundException;
import ua.training.hrm.service.exception.NonUniqueObjectException;
import ua.training.hrm.service.exception.SomethingWentWrongException;

import java.util.List;

@Service
public class EmployeeService {
    private static final Logger LOGGER = LogManager.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) throws NoEntityFoundException {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoEntityFoundException("There is no employee with provided id (" + id + ")"));
    }

    public Employee create(Employee employee) throws NonUniqueObjectException, SomethingWentWrongException {
        ensureEmployeeIsNew(employee);
        try {
            Employee newEmployee = employeeRepository.save(employee);

            if (newEmployee.getId() == null)
                throw new SomethingWentWrongException("Employee wasn't saved");

            return newEmployee;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Error creating employee {}. {}", employee, e.getMessage());
            throw new NonUniqueObjectException("Employee with such name already exists.");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new SomethingWentWrongException(ex.getMessage(), ex);
        }
    }

    public Employee edit(Employee employee) throws NonUniqueObjectException, SomethingWentWrongException {
        if (employee.getId() == null) {
            LOGGER.error("Attempt to edit employee with empty id field");
            throw new IllegalArgumentException("Attempt to edit employee with empty id field");
        }

        try {
            return employeeRepository.save(employee);

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Error editing employee {}. {}", employee, e.getMessage());
            throw new NonUniqueObjectException("Employee with such name already exists.");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new SomethingWentWrongException(ex.getMessage(), ex);
        }
    }

    public boolean deleteEmployee(Long employeeId) {

        if(employeeRepository.existsById(employeeId))
            employeeRepository.deleteById(employeeId);

        return !employeeRepository.existsById(employeeId);
    }


    private void ensureEmployeeIsNew(Employee employee) {
        if (employee.getId() != null) {
            employee.setId(null);
        }
    }
}
