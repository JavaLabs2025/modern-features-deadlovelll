package org.lab.application.ticket.services;

import com.google.inject.Inject;

import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.repository.ticket.TicketRepository;

public class TicketCloseValidator {

    private TicketRepository ticketRepository;

    @Inject
    public TicketCloseValidator(
            TicketRepository ticketRepository
    ) {
        this.ticketRepository = ticketRepository;
    }

    public void validate(
            int ticketId,
            int employeeId
    ) throws
            NotPermittedException
    {
        Ticket ticket = this.ticketRepository.get(ticketId, employeeId);
        if (ticket == null) {
            throw new NotPermittedException("Ticket with id " + ticketId + " does not exist");
        }
    }
}
