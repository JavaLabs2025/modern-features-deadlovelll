package org.lab.application.employee.use_cases;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.application.employee.services.EmployeePermissionValidator;

public class GetEmployeeUseCase {
    private EmployeeRepository employeeRepository =
            new EmployeeRepository();

    private EmployeePermissionValidator employeePermissionValidator =
            new EmployeePermissionValidator();

    public Employee execute(
            int employeeId,
            int actorId
    ) {
        employeePermissionValidator.validate(actorId);
        Employee employee = employeeRepository.getById(employeeId);
        return employee;
    }
}
