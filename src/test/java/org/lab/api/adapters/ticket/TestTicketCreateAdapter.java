package org.lab.api.adapters.ticket;

import java.util.Date;
import java.util.Map;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.lab.application.ticket.dto.CreateTicketDTO;
import org.lab.application.ticket.dto.GetTicketDTO;
import org.lab.application.ticket.use_cases.CreateTicketUseCase;
import org.lab.core.constants.ticket.TicketStatus;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.ticket.model.Ticket;

public class TestTicketCreateAdapter {

    private CreateTicketUseCase useCase;
    private ObjectMapper objectMapper;
    private TicketCreateAdapter adapter;
    private Context ctx;

    @BeforeEach
    void setup() {
        useCase = mock(CreateTicketUseCase.class);
        objectMapper = mock(ObjectMapper.class);
        adapter = new TicketCreateAdapter(useCase, objectMapper);
        ctx = mock(Context.class);
    }

    @Test
    void createTicket_success() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("projectId")).thenReturn("3");

        CreateTicketDTO bodyDto = new CreateTicketDTO(
                20,
                "Fix memory leak",
                TicketStatus.OPEN
        );

        Ticket domainTicket = new Ticket();
        domainTicket.setId(0);
        domainTicket.setAssignedTo(20);
        domainTicket.setCreatedBy(10);
        domainTicket.setProjectId(3);
        domainTicket.setDescription("Fix memory leak");
        domainTicket.setStatus(TicketStatus.OPEN);

        Ticket created = new Ticket();
        created.setId(7);
        created.setAssignedTo(20);
        created.setCreatedBy(10);
        created.setProjectId(3);
        created.setDescription("Fix memory leak");
        created.setStatus(TicketStatus.OPEN);
        created.setCreatedDate(new Date());

        GetTicketDTO dto = new GetTicketDTO(
                7,
                10,
                20,
                3,
                "Fix memory leak",
                TicketStatus.OPEN,
                created.getCreatedDate(),
                null
        );

        when(ctx.bodyAsClass(CreateTicketDTO.class)).thenReturn(bodyDto);
        when(objectMapper.mapToDomain(bodyDto, Ticket.class)).thenReturn(domainTicket);
        when(useCase.execute(domainTicket, 10, 3)).thenReturn(created);
        when(objectMapper.mapToPresentation(created, GetTicketDTO.class)).thenReturn(dto);

        when(ctx.status(201)).thenReturn(ctx);
        when(ctx.json(dto)).thenReturn(ctx);

        adapter.createTicket(ctx);

        verify(ctx).status(201);
        verify(ctx).json(dto);
    }

    @Test
    void createTicket_notPermitted() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("projectId")).thenReturn("3");

        when(ctx.bodyAsClass(CreateTicketDTO.class))
                .thenReturn(new CreateTicketDTO(
                        20,
                        "Fix bug",
                        TicketStatus.OPEN)
                );

        when(objectMapper.mapToDomain(any(), eq(Ticket.class)))
                .thenReturn(new Ticket());

        when(useCase.execute(any(), eq(10), eq(3)))
                .thenThrow(new NotPermittedException("not permitted"));

        when(ctx.status(403)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createTicket(ctx);

        verify(ctx).status(403);
        verify(ctx).json(Map.of(
                "error",
                "You do not have permission to perform this operation"
        ));
    }

    @Test
    void createTicket_projectNotFound() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("projectId")).thenReturn("3");

        when(ctx.bodyAsClass(CreateTicketDTO.class))
                .thenReturn(new CreateTicketDTO(
                        20,
                        "Fix bug",
                        TicketStatus.OPEN)
                );

        when(objectMapper.mapToDomain(any(), eq(Ticket.class)))
                .thenReturn(new Ticket());

        when(useCase.execute(any(), eq(10), eq(3)))
                .thenThrow(new ProjectNotFoundException());

        when(ctx.status(404)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createTicket(ctx);

        verify(ctx).status(404);
        verify(ctx).json(Map.of("error", "Project doesnt exist"));
    }

    @Test
    void createTicket_internalServerError() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("projectId")).thenReturn("3");

        when(ctx.bodyAsClass(CreateTicketDTO.class))
                .thenReturn(new CreateTicketDTO(
                        20,
                        "Fix bug",
                        TicketStatus.OPEN)
                );

        when(objectMapper.mapToDomain(any(), eq(Ticket.class)))
                .thenReturn(new Ticket());

        when(useCase.execute(any(), eq(10), eq(3)))
                .thenThrow(new RuntimeException("boom"));

        when(ctx.status(500)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createTicket(ctx);

        verify(ctx).status(500);
        verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
