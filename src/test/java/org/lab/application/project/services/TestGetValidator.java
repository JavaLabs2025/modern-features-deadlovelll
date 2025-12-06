package org.lab.application.project.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.application.shared.services.ProjectProvider;

import java.util.concurrent.ExecutionException;

class TestGetValidator {

    private ProjectProvider projectProvider;
    private EmployeeProvider employeeProvider;
    private GetValidator validator;

    @BeforeEach
    void setup() {
        projectProvider = mock(ProjectProvider.class);
        employeeProvider = mock(EmployeeProvider.class);
        validator = new GetValidator(projectProvider, employeeProvider);
    }

    @Test
    void validate_success() throws Exception {
        Project project = new Project();
        Employee employee = new Employee();

        when(projectProvider.get(5)).thenReturn(project);
        when(employeeProvider.get(10)).thenReturn(employee);

        Pair result = validator.validate(5, 10);

        verify(projectProvider).get(5);
        verify(employeeProvider).get(10);
        assertSame(project, result.project());
        assertSame(employee, result.employee());
    }

    @Test
    void validate_projectNotFound_throwsException() throws Exception {
        when(projectProvider.get(5)).thenThrow(new ProjectNotFoundException());
        when(employeeProvider.get(10)).thenReturn(new Employee());

        RuntimeException thrown = Assertions.assertThrows(
                RuntimeException.class,
                () -> validator.validate(5, 10)
        );

        Assertions.assertTrue(
                thrown.getCause() instanceof ExecutionException &&
                        ((ExecutionException) thrown.getCause()
                        ).getCause() instanceof ProjectNotFoundException
        );
        verify(projectProvider).get(5);
        verify(employeeProvider).get(10);
    }

    @Test
    void validate_userNotFound_throwsException() throws Exception {
        when(projectProvider.get(5)).thenReturn(new Project());
        when(employeeProvider.get(10)).thenThrow(new UserNotFoundException());

        RuntimeException thrown = Assertions.assertThrows(
                RuntimeException.class,
                () -> validator.validate(5, 10)
        );

        Assertions.assertTrue(
                thrown.getCause() instanceof ExecutionException &&
                        ((ExecutionException) thrown.getCause()
                        ).getCause() instanceof UserNotFoundException
        );

        verify(projectProvider).get(5);
        verify(employeeProvider).get(10);
    }
}
