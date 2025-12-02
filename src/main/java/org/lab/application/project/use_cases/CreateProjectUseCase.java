package org.lab.application.project.use_cases;

import org.lab.domain.project.model.Project;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.application.shared.services.EmployeePermissionValidator;

public class CreateProjectUseCase {

    private final ProjectRepository projectRepository;
    private final EmployeePermissionValidator employeePermissionValidator;

    public CreateProjectUseCase(
            ProjectRepository projectRepository,
            EmployeePermissionValidator employeePermissionValidator
    ) {
        this.projectRepository = projectRepository;
        this.employeePermissionValidator = employeePermissionValidator;
    }

    public Project execute(
            Project project
    ) {
        this.employeePermissionValidator.validate(project.getManagerId());
        Project domainProject = this.projectRepository.create(project);
        return domainProject;
    }
}
