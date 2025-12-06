package org.lab.application.shared.services;

import com.google.inject.Inject;

import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class EmployeePermissionValidator {

    private final EmployeeRepository employeeRepository;

    @Inject
    public EmployeePermissionValidator(
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
    }

    public void validate(int employeeId) {
        Employee creatorEmployee = employeeRepository.getById(employeeId);
        if (creatorEmployee.getType() != EmployeeType.MANAGER) {
            throw new NotPermittedException("Only manager can work with employees");
        }
    }
}
