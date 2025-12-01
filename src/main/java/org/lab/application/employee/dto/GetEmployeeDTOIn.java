package org.lab.application.employee.dto;

import org.lab.domain.interfaces.PresentationObject;

public record GetEmployeeDTOIn(
        int employeeId,
        int actorId
) implements PresentationObject {
}
