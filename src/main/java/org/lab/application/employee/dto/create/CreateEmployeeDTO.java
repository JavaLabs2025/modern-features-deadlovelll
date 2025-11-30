package org.lab.application.employee.dto.create;

import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.interfaces.PresentationObject;

public record CreateEmployeeDTO(
        String name,
        int age,
        EmployeeType type,
        int creatorId
) implements PresentationObject {
}
