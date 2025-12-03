package org.lab.application.employee.use_cases;

import org.lab.domain.emploee.model.Employee;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.application.shared.services.EmployeePermissionValidator;

public class GetEmployeeUseCase {

    private EmployeePermissionValidator employeePermissionValidator;
    private EmployeeProvider employeeProvider;

    public GetEmployeeUseCase(
            EmployeePermissionValidator employeePermissionValidator,
            EmployeeProvider employeeProvider
    ) {
        this.employeePermissionValidator = employeePermissionValidator;
        this.employeeProvider = employeeProvider;
    }

    public Employee execute(
            int employeeId,
            int actorId
    ) {
        employeePermissionValidator.validate(actorId);
        Employee employee = this.employeeProvider.get(employeeId);
        return employee;
    }
}
