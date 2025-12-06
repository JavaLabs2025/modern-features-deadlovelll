package org.lab.api.adapters.project;

import java.util.Map;

import com.google.inject.Inject;
import io.javalin.http.Context;

import org.lab.application.project.use_cases.DeleteProjectUseCase;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

public class ProjectDeleteAdapter {

    private final DeleteProjectUseCase useCase;

    @Inject
    public ProjectDeleteAdapter(
            DeleteProjectUseCase useCase
    ) {
        this.useCase = useCase;
    }

    public Context deleteProject(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            int projectId = Integer.parseInt(ctx.pathParam("projectId"));
            this.useCase.execute(employeeId, projectId);
            return ctx.status(204);

        } catch (UserNotFoundException e) {
            return ctx.status(409).json(Map.of("error", "User doesnt exist"));

        } catch (NotPermittedException e) {
            return ctx.status(403).json(
                    Map.of(
                            "error",
                            "You do not have permission to perform this operation"
                    )
            );

        } catch (ProjectNotFoundException e) {
            return ctx.status(404).json(Map.of("error", "Project doesn't exist"));

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
