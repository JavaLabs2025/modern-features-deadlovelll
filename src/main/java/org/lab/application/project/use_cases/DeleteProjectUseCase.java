package org.lab.application.project.use_cases;

import com.google.inject.Inject;
import org.lab.application.project.services.Pair;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.domain.project.services.ProjectMembershipValidator;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.application.project.services.GetValidator;

public class DeleteProjectUseCase {

    private final ProjectRepository projectRepository;
    private final GetValidator getValidator;
    private ProjectMembershipValidator projectMembershipValidator;

    @Inject
    public DeleteProjectUseCase(
            ProjectRepository projectRepository,
            GetValidator getValidator,
            ProjectMembershipValidator projectMembershipValidator
    ) {
        this.projectRepository = projectRepository;
        this.getValidator = getValidator;
        this.projectMembershipValidator = projectMembershipValidator;
    }

    public void execute(
            int employeeId,
            int projectId
    ) {
        Pair pair = this.getValidator.validate(projectId, employeeId);
        Employee employee = pair.employee();
        Project project = pair.project();
        this.projectMembershipValidator.validate(employee, project);
        this.projectRepository.delete(projectId);
    }
}
