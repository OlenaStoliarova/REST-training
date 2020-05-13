package ua.training.hrm.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.service.EmployeeService;
import ua.training.hrm.service.exception.NonUniqueObjectException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 0L;
    private static final String NAME = "Name";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService service;

    @Test
    public void getAllShouldReturnJsonArray() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName(NAME);
        List<Employee> allEmployees = Arrays.asList(employee);

        when(service.getAllEmployees()).thenReturn(allEmployees);

        mvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(employee.getFirstName())));
    }

    @Test
    public void createEmployeeOk() throws Exception
    {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Vasiliy");
        newEmployee.setLastName("Bubkin");

        Employee createdEmployee = new Employee();
        createdEmployee.setId(ID);

        String newEmployeeJson = "{\"firstName\":\"Vasiliy\"," +
                                    "\"lastName\":\"Bubkin\"}}";

        when(service.create(newEmployee)).thenReturn(createdEmployee);

        mvc.perform( post("/employees")
                .content(newEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void shouldReturnBadRequestWhenCreatingDuplicate() throws Exception {
        when(service.create(any(Employee.class))).thenThrow(new NonUniqueObjectException("Employee with such name already exists."));

        String newEmployeeJson = "{\"firstName\":\"Vasiliy\"," +
                "\"lastName\":\"Bubkin\"}}";

        mvc.perform( post("/employees")
                .content(newEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
