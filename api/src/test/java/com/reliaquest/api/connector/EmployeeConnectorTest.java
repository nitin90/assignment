package com.reliaquest.api.connector;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.dto.ServerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeConnectorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeConnector employeeConnector;

    @Test
    public void testGetAllEmployees() {
        ReflectionTestUtils.setField(employeeConnector, "baseUrl", "http://localhost");
        Mockito.when(restTemplate.exchange(Mockito.eq("http://localhost"), Mockito.eq(HttpMethod.GET),
                        Mockito.eq(null), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(
                        new ServerResponse<>(List.of(createEmployee()), null), HttpStatus.OK));
        ServerResponse<List<EmployeeResponse>> employees = employeeConnector.getEmployees();
        assertNotNull(employees);
        assertEquals(1, employees.data().size());
    }

    @Test
    public void testGetEmployeeByID() {
        ReflectionTestUtils.setField(employeeConnector, "baseUrl", "http://localhost");
        Mockito.when(restTemplate.exchange(Mockito.eq("http://localhost/ID"), Mockito.eq(HttpMethod.GET),
                        Mockito.eq(null), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(
                        new ServerResponse<>(createEmployee(), null), HttpStatus.OK));
        ServerResponse<EmployeeResponse> employees = employeeConnector.getEmployeeById("ID");
        assertNotNull(employees);
        assertEquals("Nitin", employees.data().employee_name());
        assertEquals(new BigDecimal(200), employees.data().employee_salary());
        assertEquals(26, employees.data().employee_age());
    }

    @Test
    public void testGetEmployeeByID_whenNoEmployeeIDPresent() {
        ReflectionTestUtils.setField(employeeConnector, "baseUrl", "http://localhost");
        Mockito.when(restTemplate.exchange(Mockito.eq("http://localhost/ID"), Mockito.eq(HttpMethod.GET),
                        Mockito.eq(null), Mockito.any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        ServerResponse<EmployeeResponse> employees = employeeConnector.getEmployeeById("ID");
        assertNotNull(employees);
        assertNull(employees.data());
    }

    @Test
    public void testCreateEmployee() {
        ReflectionTestUtils.setField(employeeConnector, "baseUrl", "http://localhost");
        Mockito.when(restTemplate.exchange(Mockito.eq("http://localhost"), Mockito.eq(HttpMethod.POST),
                        Mockito.any(HttpEntity.class), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(
                        new ServerResponse<>(createEmployee(), null), HttpStatus.OK));
        ServerResponse<EmployeeResponse> createdEmpployee = employeeConnector.createEmployee(
                new CreateEmployeeRequest("Nitin", 2000,26, "Employee"));
        assertNotNull(createdEmpployee);
        assertEquals("Nitin", createdEmpployee.data().employee_name());
        assertEquals(new BigDecimal(200), createdEmpployee.data().employee_salary());
        assertEquals(26, createdEmpployee.data().employee_age());
    }

    @Test
    public void testDeleteEmployee() {
        ReflectionTestUtils.setField(employeeConnector, "baseUrl", "http://localhost");
        Mockito.when(restTemplate.exchange(Mockito.eq("http://localhost"), Mockito.eq(HttpMethod.DELETE),
                        Mockito.any(HttpEntity.class), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(
                        new ServerResponse<>(true, null), HttpStatus.OK));
        ServerResponse<Boolean> deleteEmployeeResponse = employeeConnector.deleteEmployee("Nitin");
        assertNotNull(deleteEmployeeResponse);
        assertTrue(deleteEmployeeResponse.data());
    }

    private EmployeeResponse createEmployee() {
        return new EmployeeResponse(UUID.randomUUID().toString(), "Nitin", new BigDecimal(200), 26,
                "Employee", "abc@gmail.com");
    }

}