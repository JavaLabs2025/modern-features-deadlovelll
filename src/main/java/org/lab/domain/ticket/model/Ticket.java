package org.lab.domain.ticket.model;

import java.util.Date;

import lombok.Data;

import org.lab.core.constants.ticket.TicketStatus;
import org.lab.domain.interfaces.DomainObject;

@Data
public class Ticket implements DomainObject {
    private int id;
    private int createdBy;
    private int assignedTo;
    private int projectId;
    private String description;
    private TicketStatus status;
    private Date createdDate;
    private Date closedDate;
}