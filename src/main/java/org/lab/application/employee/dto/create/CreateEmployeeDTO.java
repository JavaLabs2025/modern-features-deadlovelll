package org.lab.application.employee.dto.create;

import java.util.Date;

import org.lab.core.constants.employee.EmployeeType;

public record CreateEmployeeDTO(
        int id,
        String name,
        int age,
        EmployeeType type,
        int createdBy,
        Date createdDate
) {
}
