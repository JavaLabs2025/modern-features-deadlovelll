package org.lab.api.adapters.ticket;

import io.javalin.http.Context;
import org.lab.application.ticket.dto.CreateTicketDTO;
import org.lab.application.ticket.dto.GetTicketDTO;
import org.lab.application.ticket.use_cases.CreateTicketUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.ticket.model.Ticket;

import java.util.Map;

public class TicketCreateAdapter {

    private CreateTicketUseCase useCase;
    private ObjectMapper objectMapper;

    public TicketCreateAdapter(
            CreateTicketUseCase useCase,
            ObjectMapper objectMapper
    ) {
        this.useCase = useCase;
        this.objectMapper = objectMapper;
    }

    public Context createTicket(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            int projectId = Integer.parseInt(ctx.pathParam("projectId"));
            CreateTicketDTO dto = ctx.bodyAsClass(CreateTicketDTO.class);
            Ticket ticket = objectMapper.mapToDomain(dto, Ticket.class);
            Ticket createdTicket = useCase.execute(
                    ticket,
                    employeeId,
                    projectId
            );
            GetTicketDTO presentationTicket = objectMapper.mapToPresentation(
                    createdTicket,
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

        } catch (ProjectNotFoundException e) {
            return ctx.status(404).json(Map.of("error", "Project doesnt exist"));

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
