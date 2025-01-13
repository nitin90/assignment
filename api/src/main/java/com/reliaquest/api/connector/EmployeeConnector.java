package com.reliaquest.api.connector;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.DeleteEmployeeInput;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.dto.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@Component
public class EmployeeConnector {

    private final RestTemplate restTemplate;

    private final String baseUrl;


    public EmployeeConnector(RestTemplate restTemplate, @Value("${employee.base.uri:NULL}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ServerResponse<List<EmployeeResponse>> getEmployees() {
        log.info("Request received to fetch all employees");
        ResponseEntity<ServerResponse<List<EmployeeResponse>>> responseEntity = this.restTemplate.exchange(baseUrl,
                GET, null,
                new ParameterizedTypeReference<>() {
                });
        log.info("Get all employee API response status - {}", responseEntity.getStatusCode());

        ServerResponse<List<EmployeeResponse>> serverResponse = null;
        if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
            serverResponse = responseEntity.getBody();

        }
        return serverResponse;
    }

    public ServerResponse<EmployeeResponse> getEmployeeById(String employeeId) {
        ServerResponse<EmployeeResponse> serverResponse = null;
        try {
            ResponseEntity<ServerResponse<EmployeeResponse>> responseEntity = this.restTemplate.exchange(baseUrl + "/" + employeeId,
                    GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            log.info("Response status - {}", responseEntity.getStatusCode());
            if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
                serverResponse = responseEntity.getBody();

            }
        } catch (HttpClientErrorException exception) {
            if (HttpStatus.NOT_FOUND == exception.getStatusCode()) {
                log.warn("No employee returned from the server");
                serverResponse = new ServerResponse<>(null, null);
            }

        }
        return serverResponse;

    }

    public ServerResponse<EmployeeResponse> createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        ServerResponse<EmployeeResponse> serverResponse = null;
        var httpEntity = new HttpEntity<>(createEmployeeRequest);
        ResponseEntity<ServerResponse<EmployeeResponse>> responseEntity = this.restTemplate.exchange(baseUrl,
                POST, httpEntity,
                new ParameterizedTypeReference<>() {
                });
        log.info("Create employee API response status - {}", responseEntity.getStatusCode());
        if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
            serverResponse = responseEntity.getBody();

        }
        return serverResponse;
    }

    public ServerResponse<Boolean> deleteEmployee(String name) {
        DeleteEmployeeInput deleteEmployeeInput = new DeleteEmployeeInput(name);
        ServerResponse<Boolean> serverResponse = null;
        var httpEntity = new HttpEntity<>(deleteEmployeeInput);
        ResponseEntity<ServerResponse<Boolean>> responseEntity = this.restTemplate.exchange(baseUrl,
                DELETE, httpEntity,
                new ParameterizedTypeReference<>() {
                });
        log.info("Get all employee API response status - {}", responseEntity.getStatusCode());
        if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
            serverResponse = responseEntity.getBody();

        }
        return serverResponse;
    }
}
