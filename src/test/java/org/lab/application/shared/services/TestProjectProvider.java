package org.lab.application.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.infra.db.repository.project.ProjectRepository;

class TestProjectProvider {

    private ProjectRepository repository;
    private ProjectProvider provider;

    @BeforeEach
    void setup() {
        repository = mock(ProjectRepository.class);
        provider = new ProjectProvider(repository);
    }

    @Test
    void get_existingEmployee_returnsEmployee()
            throws
            UserNotFoundException
    {
        int projectId = 1;
        Project project = new Project();
        project.setId(projectId);

        when(repository.get(projectId)).thenReturn(project);

        Project result = provider.get(projectId);

        assertEquals(project, result);
        verify(repository).get(projectId);
    }

    @Test
    void get_nonExistingProject_throwsException() {
        int projectId = 1;

        when(repository.get(projectId)).thenReturn(null);

        assertThrows(ProjectNotFoundException.class, () -> provider.get(projectId));
        verify(repository).get(projectId);
    }
}