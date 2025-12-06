package org.lab.api.adapters.ticket;

import java.util.Map;

import com.google.inject.Inject;
import io.javalin.http.Context;

import org.lab.application.ticket.dto.GetTicketDTO;
import org.lab.application.ticket.use_cases.CloseTicketUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.ticket.model.Ticket;

public class TicketCloseAdapter {

    private final CloseTicketUseCase useCase;
    private final ObjectMapper objectMapper;

    @Inject
    public TicketCloseAdapter(
            CloseTicketUseCase useCase,
            ObjectMapper objectMapper
    ) {
        this.useCase = useCase;
        this.objectMapper = objectMapper;
    }

    public Context closeTicket(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            int ticketId = Integer.parseInt(ctx.pathParam("ticketId"));
            Ticket updatedTicket = useCase.execute(
                    ticketId,
                    employeeId
            );
            GetTicketDTO presentationTicket = objectMapper.mapToPresentation(
                    updatedTicket,
                    GetTicketDTO.class
            );
            return ctx.status(201).json(presentationTicket);

        } catch (NotPermittedException e) {
            return ctx.status(403).json(
                    Map.of(
                            "error",
                            "You do not have permission to perform this operation"
                    )
            );

        } catch (Exception e) {
            System.err.println("ERROR "+ e.getMessage());
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
