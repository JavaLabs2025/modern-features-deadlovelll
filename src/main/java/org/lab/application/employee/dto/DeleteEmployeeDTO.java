package org.lab.application.employee.dto;

import org.lab.domain.interfaces.PresentationObject;

public record DeleteEmployeeDTO(
        int employeeId,
        int deletedEmployeeId
) implements PresentationObject {
}