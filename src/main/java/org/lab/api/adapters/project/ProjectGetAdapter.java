package org.lab.api.adapters.project;

import java.util.Map;

import com.google.inject.Inject;
import io.javalin.http.Context;

import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.GetProjectUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

public class ProjectGetAdapter {

    private final GetProjectUseCase useCase;
    private final ObjectMapper mapper;

    @Inject
    public ProjectGetAdapter(
            GetProjectUseCase useCase,
            ObjectMapper mapper
    ) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    public Context getProject(Context ctx) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            int projectId = Integer.parseInt(ctx.pathParam("projectId"));
            Project project = useCase.execute(
                    projectId,
                    employeeId
            );
            GetProjectDTO presentationProject = mapper.mapToPresentation(
                    project,
                    GetProjectDTO.class
            );
            return ctx.status(200).json(presentationProject);

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
            return ctx.status(404).json(Map.of("error", "Project doesnt exist"));

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
