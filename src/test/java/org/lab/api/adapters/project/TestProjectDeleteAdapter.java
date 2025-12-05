package org.lab.api.adapters.project;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.project.use_cases.DeleteProjectUseCase;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

import org.mockito.Mockito;

import java.util.Map;

public class TestProjectDeleteAdapter {

    private DeleteProjectUseCase useCase;
    private ProjectDeleteAdapter adapter;
    private Context ctx;

    @BeforeEach
    public void setUp() {
        useCase = Mockito.mock(DeleteProjectUseCase.class);
        ctx = Mockito.mock(Context.class);
        adapter = new ProjectDeleteAdapter(useCase);
    }

    @Test
    public void testDeleteProjectSuccess() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.when(ctx.status(204)).thenReturn(ctx);

        adapter.deleteProject(ctx);

        Mockito.verify(useCase).execute(10, 50);
        Mockito.verify(ctx).status(204);
    }

    @Test
    public void testDeleteProject_UserNotFound() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.doThrow(new UserNotFoundException())
                .when(useCase).execute(10, 50);

        Mockito.when(ctx.status(409)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.deleteProject(ctx);

        Mockito.verify(ctx).status(409);
        Mockito.verify(ctx).json(
                Map.of("error", "User doesnt exist")
        );
    }

    @Test
    public void testDeleteProject_NotPermitted() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.doThrow(new NotPermittedException("denied"))
                .when(useCase).execute(10, 50);

        Mockito.when(ctx.status(403)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.deleteProject(ctx);

        Mockito.verify(ctx).status(403);
        Mockito.verify(ctx).json(
                Map.of(
                        "error",
                        "You do not have permission to perform this operation"
                )
        );
    }

    @Test
    public void testDeleteProject_ProjectNotFound() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.doThrow(new ProjectNotFoundException())
                .when(useCase).execute(10, 50);

        Mockito.when(ctx.status(404)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.deleteProject(ctx);

        Mockito.verify(ctx).status(404);
        Mockito.verify(ctx).json(Map.of("error", "Project doesn't exist"));
    }

    @Test
    public void testDeleteProject_UnexpectedError() {
        Mockito.when(ctx.pathParam("employeeId")).thenReturn("10");
        Mockito.when(ctx.pathParam("projectId")).thenReturn("50");

        Mockito.doThrow(new RuntimeException("boom"))
                .when(useCase).execute(10, 50);

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.deleteProject(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
