package org.lab.api.adapters.project;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.project.dto.GetProjectDTO;
import org.lab.application.project.use_cases.ListProjectUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.core.constants.project.ProjectStatus;
import org.lab.domain.shared.exceptions.UserNotFoundException;

import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestProjectListAdapter {

    private ListProjectUseCase useCase;
    private ObjectMapper mapper;
    private ProjectListAdapter adapter;
    private Context ctx;

    @BeforeEach
    public void setUp() {
        useCase = Mockito.mock(ListProjectUseCase.class);
        mapper = Mockito.mock(ObjectMapper.class);
        ctx = Mockito.mock(Context.class);
        adapter = new ProjectListAdapter(useCase, mapper);
    }

    @Test
    public void testListProjectsSuccess() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");

        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project1");
        project1.setDescription("Desc1");
        project1.setManagerId(10);
        project1.setStatus(ProjectStatus.OPEN);
        project1.setCreatedDate(new Date());
        project1.setCreatedBy(99);

        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Project2");
        project2.setDescription("Desc2");
        project2.setManagerId(10);
        project2.setStatus(ProjectStatus.OPEN);
        project2.setCreatedDate(new Date());
        project2.setCreatedBy(99);

        List<Project> projects = List.of(project1, project2);

        Mockito.when(useCase.execute(10)).thenReturn(projects);

        GetProjectDTO dto1 = new GetProjectDTO(
                project1.getId(), project1.getName(), project1.getDescription(),
                project1.getManagerId(), project1.getTeamLeadId(),
                project1.getDeveloperIds(), project1.getTesterIds(),
                project1.getStatus(),
                project1.getCurrentMilestoneId(), project1.getMilestoneIds(),
                project1.getBugReportIds(),
                project1.getCreatedDate(), project1.getCreatedBy(),
                project1.getUpdatedDate(), project1.getUpdatedBy()
        );

        GetProjectDTO dto2 = new GetProjectDTO(
                project2.getId(), project2.getName(), project2.getDescription(),
                project2.getManagerId(), project2.getTeamLeadId(),
                project2.getDeveloperIds(), project2.getTesterIds(),
                project2.getStatus(),
                project2.getCurrentMilestoneId(), project2.getMilestoneIds(),
                project2.getBugReportIds(),
                project2.getCreatedDate(), project2.getCreatedBy(),
                project2.getUpdatedDate(), project2.getUpdatedBy()
        );

        Mockito.when(mapper.mapToPresentation(project1, GetProjectDTO.class)).thenReturn(dto1);
        Mockito.when(mapper.mapToPresentation(project2, GetProjectDTO.class)).thenReturn(dto2);

        List<GetProjectDTO> dtoList = List.of(dto1, dto2);

        Mockito.when(ctx.status(200)).thenReturn(ctx);
        Mockito.when(ctx.json(dtoList)).thenReturn(ctx);

        adapter.listProjects(ctx);

        Mockito.verify(ctx).status(200);
        Mockito.verify(ctx).json(dtoList);
    }

    @Test
    public void testListProjects_UserNotFound() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");

        Mockito.when(useCase.execute(10)).thenThrow(new UserNotFoundException());

        Mockito.when(ctx.status(409)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.listProjects(ctx);

        Mockito.verify(ctx).status(409);
        Mockito.verify(ctx).json(Map.of("error", "User doesnt exist"));
    }
}
