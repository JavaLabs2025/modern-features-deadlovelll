package org.lab.api.adapters.project;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.GetProjectUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.core.constants.project.ProjectStatus;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestProjectGetAdapter {

    private GetProjectUseCase useCase;
    private ObjectMapper mapper;
    private ProjectGetAdapter adapter;
    private Context ctx;

    @BeforeEach
    public void setUp() {
        useCase = Mockito.mock(GetProjectUseCase.class);
        mapper = Mockito.mock(ObjectMapper.class);
        ctx = Mockito.mock(Context.class);
        adapter = new ProjectGetAdapter(useCase, mapper);
    }

    @Test
    public void testGetProjectSuccess() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Project project = new Project();
        project.setId(50);
        project.setName("TestProject");
        project.setDescription("Description");
        project.setManagerId(10);
        project.setTeamLeadId(20);
        project.setDeveloperIds(List.of(1, 2));
        project.setTesterIds(List.of(3, 4));
        project.setStatus(ProjectStatus.OPEN);
        project.setCreatedDate(new Date());
        project.setCreatedBy(99);

        Mockito.when(
                useCase.execute(
                        Mockito.eq(50),
                        Mockito.eq(10)
                )
        ).thenReturn(project);

        GetProjectDTO dto = new GetProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getManagerId(),
                project.getTeamLeadId(),
                project.getDeveloperIds(),
                project.getTesterIds(),
                project.getStatus(),
                project.getCurrentMilestoneId(),
                project.getMilestoneIds(),
                project.getBugReportIds(),
                project.getCreatedDate(),
                project.getCreatedBy(),
                project.getUpdatedDate(),
                project.getUpdatedBy()
        );

        Mockito.when(mapper.mapToPresentation(project, GetProjectDTO.class)).thenReturn(dto);

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.getProject(ctx);

        Mockito.verify(ctx).status(200);
        Mockito.verify(ctx).json(dto);
    }

    @Test
    public void testGetProject_UserNotFound() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.when(useCase.execute(Mockito.eq(50), Mockito.eq(10)))
                .thenThrow(new UserNotFoundException());

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.getProject(ctx);

        Mockito.verify(ctx).status(409);
        Mockito.verify(ctx).json(Map.of("error", "User doesnt exist"));
    }

    @Test
    public void testGetProject_NotPermitted() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.when(useCase.execute(Mockito.eq(50), Mockito.eq(10)))
                .thenThrow(new NotPermittedException("denied"));

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.getProject(ctx);

        Mockito.verify(ctx).status(403);
        Mockito.verify(ctx).json(
                Map.of("error", "You do not have permission to perform this operation")
        );
    }

    @Test
    public void testGetProject_ProjectNotFound() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.when(useCase.execute(Mockito.eq(50), Mockito.eq(10)))
                .thenThrow(new ProjectNotFoundException());

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.getProject(ctx);

        Mockito.verify(ctx).status(404);
        Mockito.verify(ctx).json(Map.of("error", "Project doesnt exist"));
    }

    @Test
    public void testGetProject_UnexpectedError() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.when(useCase.execute(10, 50))
                .thenThrow(new RuntimeException("boom"));

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.getProject(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
