package org.lab.application.ticket.use_cases;

import com.google.inject.Inject;

import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.repository.ticket.TicketRepository;
import org.lab.application.ticket.services.TicketCreateValidator;

public class CreateTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketCreateValidator ticketCreateValidator;

    @Inject
    public CreateTicketUseCase(
            TicketRepository ticketRepository,
            TicketCreateValidator ticketCreateValidator
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketCreateValidator = ticketCreateValidator;
    }

    public Ticket execute(
            Ticket ticket,
            int employeeId,
            int projectId
    ) {
        this.ticketCreateValidator.validate(employeeId, projectId);
        Ticket createdTicket = this.ticketRepository.create(
                ticket,
                employeeId,
                projectId
        );
        return createdTicket;
    }
}
