package com.reliaquest.api.service;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;

import java.math.BigDecimal;
import java.util.List;

public interface IEmployeeService {
    Response<List<Employee>> findAllEmployees();

    Response<List<Employee>> findEmployeesByName(String searchString);

    Response<Employee> findEmployeeById(String id);

    BigDecimal highestSalaryOfEmployee();

    Response<List<String>> topTenHighEarningEmployees();

    Response createEmployee(CreateEmployeeRequest createEmployeeRequest);

    Response deleteEmployee(String id);
}
