package com.reliaquest.api.service;

import com.reliaquest.api.connector.EmployeeConnector;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.dto.ServerResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IEmployeeServiceImpl implements IEmployeeService {

    private static final String SUCCESS = "Successfully processed request.";
    private static final String SUCCESS_RESPONSE = "SUCCESS";
    private static final String FAILURE_RESPONSE = "FAILURE";

    private final EmployeeConnector employeeConnector;

    public IEmployeeServiceImpl(RestTemplate restTemplate, EmployeeConnector employeeConnector) {
        this.employeeConnector = employeeConnector;
    }

    @Override
    public Response<List<com.reliaquest.api.model.Employee>> findAllEmployees() {
        Response<List<com.reliaquest.api.model.Employee>> response;
        ServerResponse<List<EmployeeResponse>> serverResponse = employeeConnector.getEmployees();
        if (hasEmployees(serverResponse)) {
            response = new Response<>(SUCCESS_RESPONSE, null,
                    Optional.ofNullable(serverResponse.data()).orElseGet(ArrayList::new)
                            .stream().map(this::mapToEmployee).toList());
        } else {
            log.info("No employees returned from the server due to - {}", serverResponse.status());
            response = new Response<>(FAILURE_RESPONSE, "No employees returned from server", null);
        }
        return response;
    }

    private boolean hasEmployees(ServerResponse<List<EmployeeResponse>> serverResponse) {
        return null != serverResponse && SUCCESS.equals(serverResponse.status());
    }

    @Override
    public Response<List<Employee>> findEmployeesByName(String searchString) {
        Response<List<Employee>> response;
        ServerResponse<List<EmployeeResponse>> serverResponse = employeeConnector.getEmployees();
        if (hasEmployees(serverResponse)) {
            response = new Response<>(SUCCESS_RESPONSE, null, serverResponse.data().
                    stream().filter(emp -> emp.employee_name().equals(searchString))
                    .map(this::mapToEmployee).toList());
        } else {
            log.info("No employees returned from the server due to - {}", serverResponse.status());
            response = new Response<>(FAILURE_RESPONSE, "No employees returned from server", null);
        }
        return response;
    }

    @Override
    public Response<Employee> findEmployeeById(String id) {
        Response<Employee> response;
        ServerResponse<EmployeeResponse> serverResponse = employeeConnector.getEmployeeById(id);
        if (null != serverResponse && null != serverResponse.data()) {
            response = new Response<>(SUCCESS_RESPONSE, null, mapToEmployee(serverResponse.data()));
        } else {
            log.info("No employees returned from the server due to - {}", serverResponse.status());
            response = new Response<>(FAILURE_RESPONSE, "No employees returned from server", null);
        }

        return response;
    }

    @Override
    public BigDecimal highestSalaryOfEmployee() {
        ServerResponse<List<EmployeeResponse>> serverResponse = employeeConnector.getEmployees();
        if (hasEmployees(serverResponse))
            return Optional.ofNullable(serverResponse.data()).orElseGet(ArrayList::new)
                    .stream().map(EmployeeResponse::employee_salary).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        return BigDecimal.ZERO;
    }

    @Override
    public Response<List<String>> topTenHighEarningEmployees() {
        ServerResponse<List<EmployeeResponse>> serverResponse = employeeConnector.getEmployees();
        if (hasEmployees(serverResponse)) {
            return new Response<>(SUCCESS_RESPONSE, null, serverResponse.data().stream()
                    .sorted(Comparator.comparing(EmployeeResponse::employee_salary
                            , Comparator.reverseOrder())).map(EmployeeResponse::employee_name).limit(10).toList());
        }
        return Response.emptyResponse();
    }

    @Override
    public Response<?> createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        Response<?> response;
        ServerResponse<EmployeeResponse> employee = employeeConnector.createEmployee(createEmployeeRequest);
        if (null != employee && null != employee.data()) {
            response = new Response<>(SUCCESS_RESPONSE, null, mapToEmployee(employee.data()));
        } else {
            response = new Response<>(SUCCESS_RESPONSE, "The requested employee were not created ", null);
        }
        return response;
    }

    @Override
    public Response<String> deleteEmployee(String id) {
        Response<String> response;
        ServerResponse<Boolean> employee = employeeConnector.deleteEmployee(id);
        if (null != employee && employee.data()) {
            return new Response<>(SUCCESS_RESPONSE, "The employee deleted successfully", null);
        }
        return new Response<>(SUCCESS_RESPONSE, "No such employee exist", null);
    }

    private Employee mapToEmployee(EmployeeResponse res) {
        return new Employee(res.id(), res.employee_name(), res.employee_salary(), res.employee_age(), res.employee_title(), res.employee_email());
    }
}
