package com.reliaquest.api.controller;

import java.util.List;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Please <b>do not</b> modify this interface. If you believe there's a bug or the API contract does not align with our
 * mock web server... that is intentional. Good luck!
 *
 * @implNote It's uncommon to have a web controller implement an interface; We include such design pattern to
 * ensure users are following the desired input/output for our API contract, as outlined in the code assessment's README.
 *
 * @param <Employee>> object representation of an Employee
 * @param <Input> object representation of a request body for creating Employee(s)
 */
public interface IEmployeeController<Employee, Input> {

    @GetMapping("/employees")
    ResponseEntity<List<Employee>> getAllEmployees();

    @GetMapping("/employee/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/employee/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

    @GetMapping("/employee/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/employee/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping("/create-employee")
    ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest employeeInput);

    @DeleteMapping("/employee/{name}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String name);
}
