package org.lab.api.adapters.project;

import io.javalin.http.Context;

import org.lab.application.project.dto.CreateProjectDTO;
import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.CreateProjectUseCase;
import org.lab.domain.project.model.Project;
import org.lab.core.utils.mapper.ObjectMapper;

public class ProjectCreateAdapter {

    private final ObjectMapper objectMapper;
    private final CreateProjectUseCase useCase;

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
        CreateProjectDTO dto = ctx.bodyAsClass(CreateProjectDTO.class);
        Project project = objectMapper.mapToDomain(dto, Project.class);
        Project createdProject = useCase.execute(project);
        GetProjectDTO presentationProject = objectMapper.mapToPresentation(
                createdProject,
                GetProjectDTO.class
        );
        return ctx.status(201).json(presentationProject);
    }
}
