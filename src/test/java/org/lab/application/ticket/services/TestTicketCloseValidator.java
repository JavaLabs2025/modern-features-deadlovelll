package org.lab.application.ticket.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.repository.ticket.TicketRepository;

public class TestTicketCloseValidator {

    private TicketRepository ticketRepository;
    private TicketCloseValidator validator;

    @BeforeEach
    void setup() {
        ticketRepository = mock(TicketRepository.class);
        validator = new TicketCloseValidator(ticketRepository);
    }

    @Test
    void validate_existingTicket_passes() throws NotPermittedException {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setAssignedTo(10);

        when(ticketRepository.get(1, 10)).thenReturn(ticket);

        validator.validate(1, 10);
        verify(ticketRepository).get(1, 10);
    }

    @Test
    void validate_nonExistingTicket_throwsException() {
        when(ticketRepository.get(2, 10)).thenReturn(null);

        assertThrows(
                NotPermittedException.class,
                () -> validator.validate(2, 10)
        );
        verify(ticketRepository).get(2, 10);
    }
}
