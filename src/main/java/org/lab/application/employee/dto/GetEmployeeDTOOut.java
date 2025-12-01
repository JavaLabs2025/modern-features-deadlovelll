package org.lab.application.employee.dto;

import java.util.Date;

import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.interfaces.PresentationObject;

public record GetEmployeeDTOOut(
        int id,
        String name,
        int age,
        EmployeeType type,
        int createdBy,
        Date createdDate
) implements PresentationObject {
}

