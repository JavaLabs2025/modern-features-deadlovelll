package org.lab.application.ticket.use_cases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.repository.ticket.TicketRepository;
import org.lab.application.ticket.services.TicketCloseValidator;

class TestCloseTicketUseCase {

    private TicketRepository ticketRepository;
    private TicketCloseValidator ticketCloseValidator;
    private CloseTicketUseCase useCase;

    @BeforeEach
    void setup() {
        ticketRepository = mock(TicketRepository.class);
        ticketCloseValidator = mock(TicketCloseValidator.class);
        useCase = new CloseTicketUseCase(ticketRepository, ticketCloseValidator);
    }

    @Test
    void execute_success() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        when(ticketRepository.close(1)).thenReturn(ticket);

        Ticket result = useCase.execute(1, 10);

        verify(ticketCloseValidator).validate(1, 10);
        verify(ticketRepository).close(1);
        assertEquals(ticket, result);
    }

    @Test
    void execute_validatorThrows_notPermitted() {
        doThrow(
                new NotPermittedException("not permitted")
        ).when(ticketCloseValidator).validate(1, 10);

        assertThrows(
                NotPermittedException.class,
                () -> useCase.execute(1, 10)
        );

        verify(ticketCloseValidator).validate(1, 10);
        verify(ticketRepository, never()).close(anyInt());
    }
}
