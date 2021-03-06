package ua.training.hrm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.exception.NoEntityFoundException;
import ua.training.hrm.exception.NonUniqueObjectException;
import ua.training.hrm.repository.EmployeeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 0L;
    private static final String NAME = "Name";

    @InjectMocks
    private EmployeeService instance;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private Employee employee;


    @Test
    public void getAllEmployeesShouldReturnAllEmployeesList() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));

        List<Employee> result = instance.getAllEmployees();

        assertThat(result).contains(employee);
    }

    @Test
    public void shouldReturnEmployeeWhenFoundById() {
        when(employeeRepository.findById(ID)).thenReturn(Optional.of(employee));

        Employee result = instance.getEmployeeById(ID);

        assertThat(result).isEqualTo(employee);
    }

    @Test(expected = NoEntityFoundException.class)
    public void shouldThrowNoEntityFoundExceptionWhenNotFoundById() {

        when(employeeRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        instance.getEmployeeById(NON_EXISTING_ID);
    }


    @Test
    public void shouldReturnEmployeeWithFieldsFromArgumentWhenCreateSuccess() {
        Employee argumentOfCreate = new Employee();
        argumentOfCreate.setFirstName(NAME);
        argumentOfCreate.setLastName(NAME);

        Employee expectedSaveResult = new Employee();
        expectedSaveResult.setId(ID);
        expectedSaveResult.setFirstName(NAME);
        expectedSaveResult.setLastName(NAME);

        when(employeeRepository.save(argumentOfCreate)).thenReturn(expectedSaveResult);

        Employee result = instance.create(argumentOfCreate);

        assertThat(result.getFirstName()).isEqualTo(argumentOfCreate.getFirstName());
        assertThat(result.getLastName()).isEqualTo(argumentOfCreate.getLastName());
    }

    @Test(expected = NonUniqueObjectException.class)
    public void shouldThrowNonUniqueObjectExceptionWhenCreateDuplicate() {
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("constraint violation"));
        instance.create(employee);
    }


    @Test
    public void shouldReturnEmployeeWithFieldsFromArgumentWhenEditSuccess() {
        Employee argumentOfEdit = new Employee();
        argumentOfEdit.setId(ID);
        argumentOfEdit.setFirstName(NAME);
        argumentOfEdit.setLastName(NAME);

        Employee expectedSaveResult = new Employee();
        expectedSaveResult.setId(ID);
        expectedSaveResult.setFirstName(NAME);
        expectedSaveResult.setLastName(NAME);

        when(employeeRepository.save(argumentOfEdit)).thenReturn(expectedSaveResult);

        Employee result = instance.edit(argumentOfEdit);

        assertThat(result.getId()).isEqualTo(argumentOfEdit.getId());
        assertThat(result.getFirstName()).isEqualTo(argumentOfEdit.getFirstName());
        assertThat(result.getLastName()).isEqualTo(argumentOfEdit.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenEditWithoutId() {
        Employee employeeWithoutId = new Employee();
        instance.edit(employeeWithoutId);
    }

    @Test(expected = NonUniqueObjectException.class)
    public void shouldThrowNonUniqueObjectExceptionWhenEditCreatesDuplicate() {
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("constraint violation"));
        instance.edit(employee);
    }

    @Test
    public void shouldCallDeleteByIdFromRepositoryWhenIdExists() {
        when(employeeRepository.existsById(ID)).thenReturn(true);

        instance.deleteEmployee(ID);

        verify(employeeRepository).deleteById(ID);
    }

    @Test
    public void shouldReturnTrueWhenIdNotExist() {
        when(employeeRepository.existsById(ID)).thenReturn(false);

        Boolean result = instance.deleteEmployee(ID);

        assertThat(result).isTrue();
    }
}
