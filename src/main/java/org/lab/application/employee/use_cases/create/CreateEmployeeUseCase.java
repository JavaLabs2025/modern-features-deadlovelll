package org.lab.application.employee.use_cases.create;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.application.employee.services.CreateValidator;

public class CreateEmployeeUseCase {

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final CreateValidator createValidator = new CreateValidator();

    public Employee execute(
            Employee employee,
            int creatorId
    ) {
        createValidator.validate(employee, creatorId);
        Employee createdEmployee = employeeRepository.create(employee);
        return createdEmployee;
    }
}
