package org.lab.api.adapters.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.javalin.http.Context;

import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.ListProjectUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.UserNotFoundException;

public class ProjectListAdapter {

    private final ListProjectUseCase useCase;
    private final ObjectMapper mapper;

    public ProjectListAdapter(
            ListProjectUseCase useCase,
            ObjectMapper mapper
    ) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    public Context listProjects(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            List<Project> projects = this.useCase.execute(employeeId);
            List<GetProjectDTO> presentationProjects = new ArrayList<>();
            for (Project project : projects) {
                GetProjectDTO presentationProject = this.mapper.mapToPresentation(
                        project,
                        GetProjectDTO.class
                );
                presentationProjects.add(presentationProject);
            }
            return ctx.status(201).json(presentationProjects);
        } catch (UserNotFoundException e) {
            return ctx.status(409).json(Map.of("error", "User doesnt exist"));
        }
    }
}
