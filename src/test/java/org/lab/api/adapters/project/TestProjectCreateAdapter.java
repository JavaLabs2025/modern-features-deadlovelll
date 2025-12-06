package org.lab.api.adapters.project;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.project.dto.CreateProjectDTO;
import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.CreateProjectUseCase;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.core.constants.project.ProjectStatus;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.NotPermittedException;

import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestProjectCreateAdapter {

    private ObjectMapper mapper;
    private CreateProjectUseCase useCase;
    private ProjectCreateAdapter adapter;
    private Context ctx;

    @BeforeEach
    public void setUp() {
        mapper = Mockito.mock(ObjectMapper.class);
        useCase = Mockito.mock(CreateProjectUseCase.class);
        ctx = Mockito.mock(Context.class);
        adapter = new ProjectCreateAdapter(mapper, useCase);
    }

    @Test
    public void testCreateProjectSuccess() {
        CreateProjectDTO dto = new CreateProjectDTO(
                "TestProject",
                "Description",
                10,
                List.of(1, 2),
                List.of(3, 4)
        );

        Mockito.when(ctx.bodyAsClass(CreateProjectDTO.class))
                .thenReturn(dto);

        Project mapped = new Project();
        Mockito.when(mapper.mapToDomain(dto, Project.class))
                .thenReturn(mapped);

        Project created = new Project();
        created.setId(111);
        created.setName("TestProject");
        created.setDescription("Description");
        created.setManagerId(10);
        created.setTeamLeadId(20);
        created.setDeveloperIds(List.of(1, 2));
        created.setTesterIds(List.of(3, 4));
        created.setStatus(ProjectStatus.OPEN);
        created.setMilestoneIds(List.of());
        created.setBugReportIds(List.of());
        created.setCreatedDate(new Date());
        created.setCreatedBy(99);

        Mockito.when(useCase.execute(mapped, 20))
                .thenReturn(created);

        GetProjectDTO presentation = new GetProjectDTO(
                created.getId(),
                created.getName(),
                created.getDescription(),

                created.getManagerId(),
                created.getTeamLeadId(),

                created.getDeveloperIds(),
                created.getTesterIds(),

                created.getStatus(),

                created.getCurrentMilestoneId(),
                created.getMilestoneIds(),

                created.getBugReportIds(),

                created.getCreatedDate(),
                created.getCreatedBy(),
                created.getUpdatedDate(),
                created.getUpdatedBy()
        );

        Mockito.when(mapper.mapToPresentation(created, GetProjectDTO.class))
                .thenReturn(presentation);

        Mockito.when(ctx.status(201)).thenReturn(ctx);
        Mockito.when(ctx.json(presentation)).thenReturn(ctx);

        adapter.createProject(ctx);

        Mockito.verify(ctx).status(201);
        Mockito.verify(ctx).json(presentation);
    }

    @Test
    public void testCreateProject_NotPermitted() {
        CreateProjectDTO dto = new CreateProjectDTO(
                "TestProject",
                "Description",
                10,
                List.of(),
                List.of()
        );

        Mockito.when(ctx.bodyAsClass(CreateProjectDTO.class))
                .thenReturn(dto);

        Mockito.when(mapper.mapToDomain(dto, Project.class))
                .thenReturn(new Project());

        Mockito.when(useCase.execute(Mockito.any(), 20))
                .thenThrow(
                        new NotPermittedException(
                                "You do not have permission to perform this operation"
                        )
                );

        Mockito.when(ctx.status(403)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.createProject(ctx);

        Mockito.verify(ctx).status(403);
        Mockito.verify(ctx).json(
                Map.of(
                        "error",
                        "You do not have permission to perform this operation"
                )
        );
    }

    @Test
    public void testCreateProject_UnexpectedError() {
        CreateProjectDTO dto = new CreateProjectDTO(
                "TestProject",
                "Description",
                10,
                List.of(),
                List.of()
        );

        Mockito.when(ctx.bodyAsClass(CreateProjectDTO.class))
                .thenReturn(dto);

        Mockito.when(mapper.mapToDomain(dto, Project.class))
                .thenThrow(new RuntimeException("boom"));

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.createProject(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
