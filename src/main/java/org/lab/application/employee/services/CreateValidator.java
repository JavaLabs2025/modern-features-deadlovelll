package org.lab.application.employee.services;

import java.util.concurrent.*;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.UserAlreadyExistsException;

public class CreateValidator {

    private final EmployeePermissionValidator validator = new EmployeePermissionValidator();
    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    public void validate(
            Employee employee,
            int creatorId
    )
            throws NotPermittedException,
            UserAlreadyExistsException
    {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()){
            scope.fork(() -> {
                validator.validate(creatorId);
                return null;
            });
            scope.fork(() -> {
                Employee employeeCreated = employeeRepository.getById(employee.getId());
                if (employeeCreated != null) {
                    throw new UserAlreadyExistsException("Employee already exists");
                }
                return null;
            });

            scope.join();
            scope.throwIfFailed();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}