package org.lab.domain.emploee.model;

import java.util.Date;

import lombok.Data;

import org.lab.core.constants.employee.EmployeeType;

@Data
public class Employee {
    private int id;
    private String name;
    private int age;
    private EmployeeType type;
    private int createdBy;
    private Date createdDate;
}