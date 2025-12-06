package org.lab.application.ticket.services;

import com.google.inject.Inject;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class TicketPermissionValidator {

    private final EmployeeRepository employeeRepository;

    @Inject
    public TicketPermissionValidator(
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
    }

    public void validate(int employeeId) {
        Employee creatorEmployee = employeeRepository.getById(employeeId);
        System.out.println(creatorEmployee);
        if (
                creatorEmployee.getType() != EmployeeType.MANAGER &&
                        creatorEmployee.getType() != EmployeeType.TEAMLEAD
        ) {
            throw new NotPermittedException("Only manager or team lead can create tickets");
        }
    }
}
