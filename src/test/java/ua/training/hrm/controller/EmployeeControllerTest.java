package ua.training.hrm.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.exception.NonUniqueObjectException;
import ua.training.hrm.service.EmployeeService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Name";

    @InjectMocks
    private EmployeeController instance;

    @Mock
    private EmployeeService employeeService;
    @Mock
    private Employee employee;

    @Test
    public void showAllEmployeesListShouldReturnAllEmployeesList() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(employee));

        List<Employee> result = instance.showAllEmployeesList();

        verify(employeeService, times(1)).getAllEmployees();
        assertThat(result).contains(employee);
    }


    @Test
    public void addEmployeeShouldReturnEmployeeWithFieldsFromArgumentWhenCreateSuccess() {
        Employee argumentOfCreate = new Employee();
        argumentOfCreate.setFirstName(NAME);
        argumentOfCreate.setLastName(NAME);

        Employee expectedResult = new Employee();
        expectedResult.setId(ID);
        expectedResult.setFirstName(NAME);
        expectedResult.setLastName(NAME);

        when(employeeService.create(argumentOfCreate)).thenReturn(expectedResult);

        Employee result = instance.addEmployee(argumentOfCreate);

        verify(employeeService, times(1)).create(argumentOfCreate);
        assertThat(result.getFirstName()).isEqualTo(argumentOfCreate.getFirstName());
        assertThat(result.getLastName()).isEqualTo(argumentOfCreate.getLastName());
    }

    @Test(expected = NonUniqueObjectException.class)
    public void addEmployeeShouldThrowNonUniqueObjectExceptionWhenCreateDuplicate() {
        when(employeeService.create(any(Employee.class))).thenThrow(new NonUniqueObjectException("Employee with such name already exists."));
        instance.addEmployee(employee);
    }

    @Test
    public void showOneEmployeeShouldReturnEmployee() {
        when(employeeService.getEmployeeById(ID)).thenReturn(employee);

        Employee result = instance.showOneEmployee(ID);

        verify(employeeService, times(1)).getEmployeeById(ID);
        assertThat(result).isEqualTo(employee);
    }

    @Test
    public void editEmployeeShouldReturnEmployeeWithFieldsFromArgumentWhenEditSuccess() {
        Employee argumentOfEdit = new Employee();
        argumentOfEdit.setId(ID);
        argumentOfEdit.setFirstName(NAME);
        argumentOfEdit.setLastName(NAME);

        Employee expectedResult = new Employee();
        expectedResult.setId(ID);
        expectedResult.setFirstName(NAME);
        expectedResult.setLastName(NAME);

        when(employeeService.edit(argumentOfEdit)).thenReturn(expectedResult);

        Employee result = instance.editEmployee(argumentOfEdit, ID);

        verify(employeeService, times(1)).edit(argumentOfEdit);
        assertThat(result.getId()).isEqualTo(argumentOfEdit.getId());
        assertThat(result.getFirstName()).isEqualTo(argumentOfEdit.getFirstName());
        assertThat(result.getLastName()).isEqualTo(argumentOfEdit.getLastName());
    }

    @Test
    public void shouldDeleteEmployee() {
        when(employeeService.deleteEmployee(ID)).thenReturn(true);

        instance.deleteEmployee(ID);
        verify(employeeService, times(1)).deleteEmployee(ID);
    }

    @Test
    public void shouldReturnErrorCodeWhenDeleteFailed() {
        when(employeeService.deleteEmployee(ID)).thenReturn(false);

        ResponseEntity<Void> result = instance.deleteEmployee(ID);

        verify(employeeService, times(1)).deleteEmployee(ID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
