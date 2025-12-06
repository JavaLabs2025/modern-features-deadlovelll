package org.lab.application.ticket.dto;

import java.util.Date;

import org.lab.core.constants.ticket.TicketStatus;
import org.lab.domain.interfaces.PresentationObject;

public record GetTicketDTO(
        int id,
        int createdBy,
        int assignedTo,
        String description,
        TicketStatus status,
        Date createdDate,
        Date closedDate
) implements PresentationObject {
}
