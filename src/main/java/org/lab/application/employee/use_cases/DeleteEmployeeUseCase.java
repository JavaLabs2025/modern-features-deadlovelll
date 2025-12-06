package org.lab.application.employee.use_cases;

import com.google.inject.Inject;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.application.shared.services.EmployeePermissionValidator;

public class DeleteEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final EmployeePermissionValidator validator;

    @Inject
    public DeleteEmployeeUseCase(
            EmployeeRepository employeeRepository,
            EmployeePermissionValidator validator
    ) {
        this.employeeRepository = employeeRepository;
        this.validator = validator;
    }

    public void execute(
            int deleteEmployeeId,
            int employeeId
    ) {
        validator.validate(employeeId);
        employeeRepository.delete(deleteEmployeeId);
    }
}
