package org.lab.application.shared.services;

import com.google.inject.Inject;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class EmployeeProvider {

    private final EmployeeRepository employeeRepository;

    @Inject
    public EmployeeProvider(
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
    }

    public Employee get(
            int employeeId
    ) throws
            UserNotFoundException
    {
        Employee employee = employeeRepository.getById(employeeId);
        if (employee == null) {
            throw new UserNotFoundException();
        }
        return employee;
    }
}
