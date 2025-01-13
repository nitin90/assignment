package com.reliaquest.api.dto;

import java.math.BigDecimal;

public record EmployeeResponse(String id, String employee_name, BigDecimal employee_salary, Integer employee_age,
                               String employee_title,
                               String employee_email) {
}
