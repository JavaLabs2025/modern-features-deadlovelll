package org.lab.application.employee.services;

import java.util.concurrent.*;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.UserAlreadyExistsException;
import org.lab.core.constants.employee.EmployeeType;

public class CreateValidator {

    private EmployeeRepository employeeRepository = new EmployeeRepository();

    public void validate(
            Employee employee,
            int creatorId
    )
            throws NotPermittedException,
            UserAlreadyExistsException
    {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure<Employee>()){
            scope.fork(() -> {
                Employee creatorEmployee = employeeRepository.getById(creatorId);
                if (creatorEmployee.getType() != EmployeeType.MANAGER) {
                    throw new NotPermittedException("Only manager can add employees");
                }
                return creatorEmployee;
            });
            scope.fork(() -> {
                Employee employeeCreated = employeeRepository.getById(employee.getId());
                if (employeeCreated != null) {
                    throw new UserAlreadyExistsException("Employee already exists");
                }
            });

            scope.join();
            scope.throwIfFailed();
        }
    }
}