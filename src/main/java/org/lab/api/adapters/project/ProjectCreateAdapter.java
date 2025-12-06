package org.lab.api.adapters.project;

import java.util.Map;

import com.google.inject.Inject;
import io.javalin.http.Context;

import org.lab.application.project.dto.CreateProjectDTO;
import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.CreateProjectUseCase;
import org.lab.domain.project.model.Project;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class ProjectCreateAdapter {

    private final ObjectMapper objectMapper;
    private final CreateProjectUseCase useCase;

    @Inject
    public ProjectCreateAdapter(
            ObjectMapper objectMapper,
            CreateProjectUseCase useCase
    ) {
        this.objectMapper = objectMapper;
        this.useCase = useCase;
    }

    public Context createProject(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            CreateProjectDTO dto = ctx.bodyAsClass(CreateProjectDTO.class);
            Project project = objectMapper.mapToDomain(dto, Project.class);
            Project createdProject = useCase.execute(project, employeeId);
            GetProjectDTO presentationProject = objectMapper.mapToPresentation(
                    createdProject,
                    GetProjectDTO.class
            );
            return ctx.status(201).json(presentationProject);

        } catch (NotPermittedException e) {
            return ctx.status(403).json(
                    Map.of(
                            "error",
                            "You do not have permission to perform this operation"
                    )
            );
        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
