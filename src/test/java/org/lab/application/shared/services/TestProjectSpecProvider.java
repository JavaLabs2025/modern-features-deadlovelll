package org.lab.application.shared.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.infra.db.spec.Specification;

class TestProjectSpecProvider {

    private ProjectRepository repository;
    private ProjectSpecProvider provider;
    private Specification spec;

    @BeforeEach
    void setup() {
        repository = mock(ProjectRepository.class);
        provider = new ProjectSpecProvider(repository);
        spec = mock(DummySpec.class);
    }

    @Test
    void get_existingProject_returnsProject()
            throws
            ProjectNotFoundException
    {
        int projectId = 1;
        Project project = new Project();
        project.setId(projectId);

        when(repository.getWithSpec(projectId, spec)).thenReturn(project);

        Project result = provider.get(projectId, spec);

        assertEquals(project, result);
        verify(repository).getWithSpec(projectId, spec);
    }

    @Test
    void get_nonExistingProject_throwsException() {
        int projectId = 1;

        when(repository.getWithSpec(projectId, spec)).thenReturn(null);

        assertThrows(ProjectNotFoundException.class, () -> provider.get(projectId, spec));
        verify(repository).getWithSpec(projectId, spec);
    }
}
