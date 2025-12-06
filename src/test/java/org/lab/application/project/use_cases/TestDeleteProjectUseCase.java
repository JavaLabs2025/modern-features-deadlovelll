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
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;

class TestDeleteProjectUseCase {

    private ProjectRepository projectRepository;
    private GetValidator getValidator;
    private ProjectMembershipValidator projectMembershipValidator;
    private DeleteProjectUseCase useCase;

    @BeforeEach
    void setup() {
        projectRepository = mock(ProjectRepository.class);
        getValidator = mock(GetValidator.class);
        projectMembershipValidator = mock(ProjectMembershipValidator.class);
        useCase = new DeleteProjectUseCase(
                projectRepository,
                getValidator,
                projectMembershipValidator
        );
    }

    @Test
    void execute_success_deletesProject() throws Exception {
        int employeeId = 10;
        int projectId = 5;
        Employee employee = new Employee();
        Project project = new Project();
        Pair pair = new Pair(project, employee);

        when(getValidator.validate(projectId, employeeId)).thenReturn(pair);

        useCase.execute(employeeId, projectId);

        verify(getValidator).validate(projectId, employeeId);
        verify(projectMembershipValidator).validate(employee, project);
        verify(projectRepository).delete(projectId);
    }

    @Test
    void execute_validatorThrows_exception() throws Exception {
        int employeeId = 10;
        int projectId = 5;

        when(getValidator.validate(projectId, employeeId))
                .thenThrow(new ProjectNotFoundException());

        assertThrows(
                ProjectNotFoundException.class,
                () -> useCase.execute(employeeId, projectId)
        );

        verify(getValidator).validate(projectId, employeeId);
        verifyNoInteractions(projectMembershipValidator);
        verifyNoInteractions(projectRepository);
    }

    @Test
    void execute_membershipValidatorThrows_exception() throws Exception {
        int employeeId = 10;
        int projectId = 5;
        Employee employee = new Employee();
        Project project = new Project();
        Pair pair = new Pair(project, employee);

        when(getValidator.validate(projectId, employeeId)).thenReturn(pair);
        doThrow(
                new NotPermittedException("Not permitted")
        ).when(projectMembershipValidator).validate(employee, project);

        assertThrows(NotPermittedException.class, () -> useCase.execute(employeeId, projectId));

        verify(getValidator).validate(projectId, employeeId);
        verify(projectMembershipValidator).validate(employee, project);
        verifyNoInteractions(projectRepository);
    }
}
