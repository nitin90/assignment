package com.reliaquest.api.service;

import com.reliaquest.api.connector.EmployeeConnector;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.dto.ServerResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IEmployeeServiceImplTest {
    @Mock
    private EmployeeConnector employeeConnector;

    @InjectMocks
    private IEmployeeServiceImpl iEmployeeService;

    @Test
    public void testFindAllEmployee() {
        Mockito.when(employeeConnector.getEmployees())
                .thenReturn(new ServerResponse<>(List.of(createEmployee()), "Successfully processed request."));
        Response<List<Employee>> allEmployees = iEmployeeService.findAllEmployees();
        assertNotNull(allEmployees);
        assertEquals(1, allEmployees.employees().size());
        assertEquals(expectedEmployeeResponse(), allEmployees.employees().get(0));
    }

    @Test
    public void testFindAllEmployee_whenNoEmployeeFound() {
        Mockito.when(employeeConnector.getEmployees())
                .thenReturn(new ServerResponse<>(List.of(createEmployee()), "Failled"));
        Response<List<Employee>> allEmployees = iEmployeeService.findAllEmployees();
        assertNotNull(allEmployees);
        Assertions.assertNull(allEmployees.employees());
        assertEquals("No employees returned from server", allEmployees.statusMessage());
    }

    @Test
    public void testFindAllEmployeeByName() {
        Mockito.when(employeeConnector.getEmployees())
                .thenReturn(new ServerResponse<>(List.of(createEmployee(), createEmployee("Paliwal", 2500)),
                        "Successfully processed request."));
        Response<List<Employee>> allEmployees = iEmployeeService.findEmployeesByName("Nitin");
        assertNotNull(allEmployees);
        assertEquals(1, allEmployees.employees().size());
        assertEquals(expectedEmployeeResponse(), allEmployees.employees().get(0));
    }

    @Test
    public void testFindAllEmployeeById() {
        Mockito.when(employeeConnector.getEmployeeById("ID"))
                .thenReturn(new ServerResponse<>(createEmployee(),
                        "Successfully processed request."));
        Response<Employee> allEmployees = iEmployeeService.findEmployeeById("ID");
        assertNotNull(allEmployees);
        assertEquals(expectedEmployeeResponse(), allEmployees.employees());
    }

    @Test
    public void testHighestSalary() {
        Mockito.when(employeeConnector.getEmployees())
                .thenReturn(new ServerResponse<>(List.of(createEmployee("A", 20),
                        createEmployee("A", 40)),
                        "Successfully processed request."));
        BigDecimal salary = iEmployeeService.highestSalaryOfEmployee();
        assertEquals(new BigDecimal(40), salary);
    }

    @Test
    public void testTopTenHighestSalariedEmployeeNames() {
        Mockito.when(employeeConnector.getEmployees())
                .thenReturn(new ServerResponse<>(List.of(createEmployee("A", 20),
                        createEmployee("B", 40)),
                        "Successfully processed request."));
        Response<List<String>> employees = iEmployeeService.topTenHighEarningEmployees();
        assertNotNull(employees);
        assertEquals(2, employees.employees().size());
    }

    @Test
    public void testCreateEmployee() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("Nitin",200,2,"Employee");
        Mockito.when(employeeConnector.createEmployee(createEmployeeRequest))
                .thenReturn(new ServerResponse<>(createEmployee("Nitin", 200),
                        "Successfully processed request."));
        Response<?> employee = iEmployeeService.createEmployee(createEmployeeRequest);
        assertNotNull(employee);
        if(employee.employees() instanceof  Employee e) {
            assertEquals(expectedEmployeeResponse(), e);
        }
    }

    @Test
    public void testCreateEmployee_whenEmployeeCreationFails() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("Nitin",200,2,"Employee");
        Mockito.when(employeeConnector.createEmployee(createEmployeeRequest))
                .thenReturn(new ServerResponse<>(null,
                        "Failed"));
        Response<?> employee = iEmployeeService.createEmployee(createEmployeeRequest);
        assertNotNull(employee);
        assertNull(employee.employees());
    }

    @Test
    public void testDeleteEmployee() {
        Mockito.when(employeeConnector.deleteEmployee(Mockito.eq("Nitin")))
                .thenReturn(new ServerResponse<>(true, null));
        Response<String> response = iEmployeeService.deleteEmployee("Nitin");
                assertNotNull(response);
        assertEquals("The employee deleted successfully", response.statusMessage() );
    }

    @Test
    public void testDeleteEmployee_whenEmployeeNotFound() {
        Mockito.when(employeeConnector.deleteEmployee(Mockito.eq("Nitin")))
                .thenReturn(new ServerResponse<>(false, null));
        Response<String> response = iEmployeeService.deleteEmployee("Nitin");
        assertNotNull(response);
        assertEquals("No such employee exist", response.statusMessage() );
    }

    private EmployeeResponse createEmployee() {
        return new EmployeeResponse("ID", "Nitin", new BigDecimal(200), 26,
                "Employee", "abc@gmail.com");
    }

    private EmployeeResponse createEmployee(String name, Integer salary) {
        return new EmployeeResponse("ID", name, new BigDecimal(salary), 26,
                "Employee", "abc@gmail.com");
    }

    private Employee expectedEmployeeResponse() {
        return new Employee("ID", "Nitin", new BigDecimal(200), 26,
                "Employee", "abc@gmail.com");
    }

}