package com.reliaquest.api.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Employee {

    private String id;
    private String name;
    private BigDecimal salary;
    private Integer age;
    private String title;
    private String email;
}
