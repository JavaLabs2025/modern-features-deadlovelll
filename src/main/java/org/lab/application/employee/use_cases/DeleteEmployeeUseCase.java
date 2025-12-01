package org.lab.application.employee.use_cases;

import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.application.employee.services.EmployeePermissionValidator;

public class DeleteEmployeeUseCase {

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final EmployeePermissionValidator validator = new EmployeePermissionValidator();

    public void execute(
            int deleteEmployeeId,
            int employeeId
    ) {
        validator.validate(employeeId);
        employeeRepository.delete(deleteEmployeeId);
    }
}
