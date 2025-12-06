package org.lab.api.adapters.ticket;

import java.util.Date;
import java.util.Map;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.lab.application.ticket.dto.GetTicketDTO;
import org.lab.application.ticket.use_cases.CloseTicketUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.core.constants.ticket.TicketStatus;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.ticket.model.Ticket;

public class TestTicketCloseAdapter {

    private CloseTicketUseCase useCase;
    private ObjectMapper objectMapper;
    private TicketCloseAdapter adapter;
    private Context ctx;

    @BeforeEach
    void setup() {
        useCase = mock(CloseTicketUseCase.class);
        objectMapper = mock(ObjectMapper.class);
        adapter = new TicketCloseAdapter(useCase, objectMapper);
        ctx = mock(Context.class);
    }

    @Test
    void closeTicket_success() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("ticketId")).thenReturn("5");

        Ticket domainTicket = new Ticket();
        domainTicket.setId(5);
        domainTicket.setCreatedBy(10);
        domainTicket.setAssignedTo(11);
        domainTicket.setProjectId(1);
        domainTicket.setDescription("Fix bug");
        domainTicket.setStatus(TicketStatus.CLOSED);
        domainTicket.setCreatedDate(new Date());
        domainTicket.setClosedDate(new Date());

        GetTicketDTO dto = new GetTicketDTO(
                5,
                10,
                11,
                1,
                "Fix bug",
                TicketStatus.CLOSED,
                domainTicket.getCreatedDate(),
                domainTicket.getClosedDate()
        );

        when(useCase.execute(5, 10)).thenReturn(domainTicket);
        when(objectMapper.mapToPresentation(domainTicket, GetTicketDTO.class)).thenReturn(dto);

        when(ctx.status(201)).thenReturn(ctx);
        when(ctx.json(dto)).thenReturn(ctx);

        adapter.closeTicket(ctx);

        verify(ctx).status(201);
        verify(ctx).json(dto);
    }

    @Test
    void closeTicket_notPermitted() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("ticketId")).thenReturn("5");

        when(useCase.execute(5, 10))
                .thenThrow(new NotPermittedException("you are not permitted"));

        when(ctx.status(403)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.closeTicket(ctx);

        verify(ctx).status(403);
        verify(ctx).json(Map.of(
                "error",
                "You do not have permission to perform this operation"
        ));
    }

    @Test
    void closeTicket_internalServerError() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.pathParam("ticketId")).thenReturn("5");

        when(useCase.execute(5, 10)).thenThrow(new RuntimeException("boom"));

        when(ctx.status(500)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.closeTicket(ctx);

        verify(ctx).status(500);
        verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
