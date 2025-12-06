package org.lab.application.ticket.use_cases;

import com.google.inject.Inject;

import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.repository.ticket.TicketRepository;
import org.lab.application.ticket.services.TicketCloseValidator;

public class CloseTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketCloseValidator ticketCloseValidator;

    @Inject
    public CloseTicketUseCase(
            TicketRepository ticketRepository,
            TicketCloseValidator ticketCloseValidator
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketCloseValidator = ticketCloseValidator;
    }

    public Ticket execute(
            int ticketId,
            int employeeId
    ) {
        this.ticketCloseValidator.validate(ticketId, employeeId);
        Ticket ticket = this.ticketRepository.close(ticketId);
        return ticket;
    }
}
