package org.lab.application.project.use_cases;

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
        System.out.println("Getting pair " + projectId);
        Pair pair = this.getValidator.validate(projectId, employeeId);
        Employee employee = pair.employee();
        Project project = pair.project();
        System.out.println("Got pair " + projectId);
        this.projectMembershipValidator.validate(employee, project);
        this.projectRepository.delete(projectId);
    }
}
