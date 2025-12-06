package org.lab.application.project.use_cases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.application.shared.services.EmployeePermissionValidator;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestCreateProjectUseCase {

    private ProjectRepository projectRepository;
    private EmployeePermissionValidator employeePermissionValidator;
    private CreateProjectUseCase useCase;

    @BeforeEach
    void setup() {
        projectRepository = mock(ProjectRepository.class);
        employeePermissionValidator = mock(EmployeePermissionValidator.class);
        useCase = new CreateProjectUseCase(
                projectRepository,
                employeePermissionValidator
        );
    }

    @Test
    void execute_success_createsProject() {
        Project project = new Project();
        project.setId(1);
        int employeeId = 10;

        when(projectRepository.create(project, employeeId)).thenReturn(project);

        Project result = useCase.execute(project, employeeId);

        verify(employeePermissionValidator).validate(employeeId);
        verify(projectRepository).create(project, employeeId);
        assertEquals(project, result);
    }

    @Test
    void execute_notPermitted_throwsException() {
        Project project = new Project();
        int employeeId = 5;

        doThrow(new NotPermittedException("Not allowed"))
                .when(employeePermissionValidator).validate(employeeId);

        assertThrows(NotPermittedException.class, () -> useCase.execute(project, employeeId));

        verify(employeePermissionValidator).validate(employeeId);
        verifyNoInteractions(projectRepository);
    }
}

