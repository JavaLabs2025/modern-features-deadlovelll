package org.lab.application.employee.use_cases;

import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class CreateEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final EmployeePermissionValidator validator;

    public CreateEmployeeUseCase(
            EmployeeRepository employeeRepository,
            EmployeePermissionValidator validator
    ) {
        this.employeeRepository = employeeRepository;
        this.validator = validator;
    }

    public Employee execute(
            Employee employee,
            int creatorId
    ) {
        validator.validate(creatorId);
        Employee createdEmployee = employeeRepository.create(
                employee,
                creatorId
        );
        return createdEmployee;
    }
}
