package org.lab.application.employee.dto.delete;

import org.lab.domain.interfaces.PresentationObject;

public record DeleteEmployeeDTO(
        int employeeId,
        int deletedEmployeeId
) implements PresentationObject {
}