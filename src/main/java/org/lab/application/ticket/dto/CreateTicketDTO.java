package org.lab.application.ticket.dto;

import org.lab.core.constants.ticket.TicketStatus;
import org.lab.domain.interfaces.PresentationObject;

public record CreateTicketDTO(
        int assignedTo,
        String description,
        TicketStatus status
) implements PresentationObject {
}