package com.reliaquest.api.controller;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.model.Response;
import com.reliaquest.api.service.IEmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequestMapping("/v1")
@RestController
public class EmployeeController implements IEmployeeController {

    private final IEmployeeService iEmployeeService;

    public EmployeeController(IEmployeeService iEmployeeService) {
        this.iEmployeeService = iEmployeeService;
    }

    @Override
    public ResponseEntity<Response<?>> getAllEmployees() {
        return new ResponseEntity<>(iEmployeeService.findAllEmployees(),
                OK);
    }

    @Override
    public ResponseEntity<Response<?>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("Employee search request with search string as - {}", searchString);
        if (!StringUtils.hasText(searchString)) {
            return new ResponseEntity<>(new Response<>("FAILED",
                    "Please  provide a non empty search string", null),
                    BAD_REQUEST);
        }
        return new ResponseEntity<>(iEmployeeService.findEmployeesByName(searchString), OK);
    }

    @Override
    public ResponseEntity<Response<?>> getEmployeeById(@PathVariable String id) {
        log.info("Employee search request with employee id as - {}", id);
        if (!StringUtils.hasText(id)) {
            return new ResponseEntity<>(new Response<>("FAILED",
                    "Please  provide a non empty ID", null), BAD_REQUEST);
        }
        return new ResponseEntity<>(iEmployeeService.findEmployeeById(id), OK);
    }

    @Override
    public ResponseEntity<BigDecimal> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(iEmployeeService.highestSalaryOfEmployee());
    }

    @Override
    public ResponseEntity<Response<List<String>>> getTopTenHighestEarningEmployeeNames() {
        return new ResponseEntity<>(iEmployeeService.topTenHighEarningEmployees(), OK);
    }

    @Override
    public ResponseEntity<?> createEmployee(@Valid @RequestBody CreateEmployeeRequest employeeInput) {
        return new ResponseEntity<Response<?>>(iEmployeeService.createEmployee(employeeInput), OK);
    }


    @Override
    public ResponseEntity<Response<String>> deleteEmployeeById(@PathVariable String name) {
        log.info("Request received to delete the employee id - {}", name);
        if (!StringUtils.hasText(name)) {
            return ResponseEntity.badRequest().body(new Response<>("FAILED",
                    "Please  provide a valid employee id", null));
        }
        return new ResponseEntity<Response<String>>(iEmployeeService.deleteEmployee(name), OK);
    }
}
