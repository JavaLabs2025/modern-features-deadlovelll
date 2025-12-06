package org.lab.application.project.use_cases;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.application.project.services.UserSpecFactory;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.infra.db.spec.Specification;
import org.lab.infra.db.spec.SqlSpec;

public class TestListProjectUseCase {

    private ProjectRepository projectRepository;
    private EmployeeProvider employeeProvider;
    private UserSpecFactory userSpecFactory;
    private ListProjectUseCase useCase;

    @BeforeEach
    void setup() {
        projectRepository = mock(ProjectRepository.class);
        employeeProvider = mock(EmployeeProvider.class);
        userSpecFactory = mock(UserSpecFactory.class);
        useCase = new ListProjectUseCase(
                projectRepository,
                employeeProvider,
                userSpecFactory
        );
    }

    @Test
    void execute_success_returnsProjects() {
        int employeeId = 10;
        Employee employee = new Employee();
        Specification spec = mock(SqlSpec.class);
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = List.of(project1, project2);

        when(employeeProvider.get(employeeId)).thenReturn(employee);
        when(userSpecFactory.getForType(employee)).thenReturn(spec);
        when(projectRepository.list(spec)).thenReturn(projects);

        List<Project> result = useCase.execute(employeeId);

        assertEquals(projects, result);
        verify(employeeProvider).get(employeeId);
        verify(userSpecFactory).getForType(employee);
        verify(projectRepository).list(spec);
    }

    @Test
    void execute_employeeProviderThrowsException_propagates() {
        int employeeId = 10;
        when(employeeProvider.get(employeeId)).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(employeeId)
        );
        assertEquals("boom", ex.getMessage());

        verify(employeeProvider).get(employeeId);
        verifyNoInteractions(userSpecFactory);
        verifyNoInteractions(projectRepository);
    }

    @Test
    void execute_userSpecFactoryThrowsException_propagates() {
        int employeeId = 10;
        Employee employee = new Employee();
        when(employeeProvider.get(employeeId)).thenReturn(employee);
        when(userSpecFactory.getForType(employee)).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(employeeId)
        );
        assertEquals("boom", ex.getMessage());

        verify(employeeProvider).get(employeeId);
        verify(userSpecFactory).getForType(employee);
        verifyNoInteractions(projectRepository);
    }

    @Test
    void execute_projectRepositoryThrowsException_propagates() {
        int employeeId = 10;
        Employee employee = new Employee();
        Specification spec = mock(SqlSpec.class);
        when(employeeProvider.get(employeeId)).thenReturn(employee);
        when(userSpecFactory.getForType(employee)).thenReturn(spec);
        when(projectRepository.list(spec)).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(employeeId)
        );
        assertEquals("boom", ex.getMessage());

        verify(employeeProvider).get(employeeId);
        verify(userSpecFactory).getForType(employee);
        verify(projectRepository).list(spec);
    }
}
