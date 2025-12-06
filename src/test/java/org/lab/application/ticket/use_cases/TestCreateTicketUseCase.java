package org.lab.application.ticket.use_cases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.repository.ticket.TicketRepository;
import org.lab.application.ticket.services.TicketCreateValidator;
import org.lab.domain.shared.exceptions.NotPermittedException;

class TestCreateTicketUseCase {

    private TicketRepository ticketRepository;
    private TicketCreateValidator ticketCreateValidator;
    private CreateTicketUseCase useCase;

    @BeforeEach
    void setup() {
        ticketRepository = mock(TicketRepository.class);
        ticketCreateValidator = mock(TicketCreateValidator.class);
        useCase = new CreateTicketUseCase(ticketRepository, ticketCreateValidator);
    }

    @Test
    void execute_success() {
        Ticket ticket = new Ticket();
        Ticket createdTicket = new Ticket();
        createdTicket.setId(1);

        when(ticketRepository.create(ticket, 10, 5))
                .thenReturn(createdTicket);

        Ticket result = useCase.execute(ticket, 10, 5);

        verify(ticketCreateValidator).validate(10, 5);
        verify(ticketRepository).create(ticket, 10, 5);
        assertEquals(createdTicket, result);
    }

    @Test
    void execute_validatorThrows_notPermitted() {
        Ticket ticket = new Ticket();
        doThrow(
                new NotPermittedException("Not permitted")
        )
                .when(ticketCreateValidator)
                .validate(10, 5);

        assertThrows(
                NotPermittedException.class,
                () -> useCase.execute(ticket, 10, 5)
        );

        verify(ticketCreateValidator).validate(10, 5);
        verify(ticketRepository, never()).create(any(), anyInt(), anyInt());
    }
}
