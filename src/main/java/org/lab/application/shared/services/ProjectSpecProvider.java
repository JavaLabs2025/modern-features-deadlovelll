package org.lab.application.shared.services;

import com.google.inject.Inject;

import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.infra.db.spec.Specification;

public class ProjectSpecProvider {

    private ProjectRepository projectRepository;

    @Inject
    public ProjectSpecProvider(
            ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }

    public Project get(
            int projectId,
            Specification spec
    ) throws
            ProjectNotFoundException
    {
        Project project = this.projectRepository.getWithSpec(
                projectId,
                spec
        );
        if (project == null) {
            throw new ProjectNotFoundException();
        }
        return project;
    }
}

