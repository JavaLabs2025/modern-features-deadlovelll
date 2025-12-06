package org.lab.application.shared.services;

import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.infra.db.repository.project.ProjectRepository;

public class ProjectProvider {

    private ProjectRepository projectRepository;

    public ProjectProvider(
            ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }

    public Project get(
            int projectId
    ) throws
            ProjectNotFoundException
    {
        Project project = this.projectRepository.get(projectId);
        if (project == null) {
            throw new ProjectNotFoundException();
        }
        return project;
    }
}
