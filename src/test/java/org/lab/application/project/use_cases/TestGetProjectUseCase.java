package org.lab.application.project.use_cases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.lab.application.project.services.GetValidator;
import org.lab.application.project.services.Pair;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.domain.project.services.ProjectMembershipValidator;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

public class TestGetProjectUseCase {
    private GetValidator getValidator;
    private ProjectMembershipValidator projectMembershipValidator;
    private GetProjectUseCase useCase;

    @BeforeEach
    void setup() {
        getValidator = mock(GetValidator.class);
        projectMembershipValidator = mock(ProjectMembershipValidator.class);
        useCase = new GetProjectUseCase(
                getValidator,
                projectMembershipValidator
        );
    }

    @Test
    void execute_success_returnsProject() {
        int employeeId = 10;
        int projectId = 5;
        Employee employee = new Employee();
        Project project = new Project();
        Pair pair = new Pair(project, employee);

        when(getValidator.validate(projectId, employeeId)).thenReturn(pair);

        Project result = useCase.execute(projectId, employeeId);

        assertEquals(project, result);
        verify(getValidator).validate(projectId, employeeId);
        verify(projectMembershipValidator).validate(employee, project);
    }

    @Test
    void execute_validatorThrowsProjectNotFoundException() {
        int employeeId = 10;
        int projectId = 5;

        when(getValidator.validate(projectId, employeeId))
                .thenThrow(new ProjectNotFoundException());

        assertThrows(
                ProjectNotFoundException.class,
                () -> useCase.execute(projectId, employeeId)
        );

        verify(getValidator).validate(projectId, employeeId);
        verifyNoInteractions(projectMembershipValidator);
    }

    @Test
    void execute_validatorThrowsUserNotFoundException() {
        int employeeId = 10;
        int projectId = 5;

        when(getValidator.validate(projectId, employeeId))
                .thenThrow(new UserNotFoundException());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(projectId, employeeId)
        );

        verify(getValidator).validate(projectId, employeeId);
        verifyNoInteractions(projectMembershipValidator);
    }

    @Test
    void execute_membershipValidatorThrowsNotPermittedException() {
        int employeeId = 10;
        int projectId = 5;
        Employee employee = new Employee();
        Project project = new Project();
        Pair pair = new Pair(project, employee);

        when(getValidator.validate(projectId, employeeId)).thenReturn(pair);
        doThrow(
                new NotPermittedException("not permitted")
        ).when(projectMembershipValidator).validate(employee, project);

        assertThrows(
                NotPermittedException.class,
                () -> useCase.execute(projectId, employeeId)
        );

        verify(getValidator).validate(projectId, employeeId);
        verify(projectMembershipValidator).validate(employee, project);
    }
}
