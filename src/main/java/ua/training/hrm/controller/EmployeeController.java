package ua.training.hrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.service.EmployeeService;
import ua.training.hrm.service.exception.NoEntityFoundException;
import ua.training.hrm.service.exception.NonUniqueObjectException;
import ua.training.hrm.service.exception.SomethingWentWrongException;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/employees")
    public List<Employee> showAllEmployeesList() {

        return employeeService.getAllEmployees();
    }


    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee newEmployee) {

        try {
            return employeeService.create(newEmployee);

        } catch (NonUniqueObjectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (SomethingWentWrongException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("/employees/{id}")
    public Employee showOneEmployee(@PathVariable Long id) {
        try {
            return employeeService.getEmployeeById(id);

        } catch (NoEntityFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping("/employees/{id}")
    public Employee editEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        newEmployee.setId(id);
        try {
            return employeeService.edit(newEmployee);

        } catch (NonUniqueObjectException|IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SomethingWentWrongException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean wasDeleted = employeeService.deleteEmployee(id);

        if(wasDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
