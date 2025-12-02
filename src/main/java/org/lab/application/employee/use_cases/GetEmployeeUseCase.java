package org.lab.application.employee.use_cases;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.application.employee.services.EmployeePermissionValidator;

public class GetEmployeeUseCase {

    private EmployeeRepository employeeRepository;
    private EmployeePermissionValidator employeePermissionValidator;

    public GetEmployeeUseCase(
            EmployeeRepository employeeRepository,
            EmployeePermissionValidator employeePermissionValidator
    ) {
        this.employeeRepository = employeeRepository;
        this.employeePermissionValidator = employeePermissionValidator;
    }

    public Employee execute(
            int employeeId,
            int actorId
    ) {
        employeePermissionValidator.validate(actorId);
        Employee employee = employeeRepository.getById(employeeId);
        if (employee == null) {
            throw new UserNotFoundException();
        }
        return employee;
    }
}
