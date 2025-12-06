package org.lab.application.error_messages.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.error_message.services.CreateErrorMessageValidator;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.application.shared.services.ProjectSpecProvider;
import org.lab.application.project.services.UserSpecFactory;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.repository.employee.spec.TesterSpec;
import org.lab.infra.db.spec.Specification;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateErrorMessageValidator {

    private EmployeeProvider employeeProvider;
    private ProjectSpecProvider projectSpecProvider;
    private UserSpecFactory userSpecFactory;
    private CreateErrorMessageValidator validator;

    @BeforeEach
    void setup() {
        employeeProvider = mock(EmployeeProvider.class);
        projectSpecProvider = mock(ProjectSpecProvider.class);
        userSpecFactory = mock(UserSpecFactory.class);
        validator = new CreateErrorMessageValidator(
                employeeProvider,
                projectSpecProvider,
                userSpecFactory
        );
    }

    @Test
    void validate_onlyTesterAllowed() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setType(EmployeeType.PROGRAMMER);
        when(employeeProvider.get(1)).thenReturn(employee);

        NotPermittedException ex = assertThrows(NotPermittedException.class, () -> {
            validator.validate(1, 10);
        });

        assertEquals("Only tester can create error messages", ex.getMessage());
        verifyNoInteractions(userSpecFactory, projectSpecProvider);
    }

    @Test
    void validate_testerCallsProjectSpecProvider() {
        Employee employee = new Employee();
        employee.setId(2);
        employee.setType(EmployeeType.TESTER);

        Specification spec = mock(TesterSpec.class);

        when(employeeProvider.get(2)).thenReturn(employee);
        when(userSpecFactory.getForType(employee)).thenReturn(spec);

        validator.validate(2, 10);

        verify(userSpecFactory).getForType(employee);
        verify(projectSpecProvider).get(10, spec);
    }
}
