package org.lab.domain.error_mesage.model;

import lombok.Data;

import org.lab.core.constants.error_message.ErrorMessageStatus;
import org.lab.domain.interfaces.DomainObject;

@Data
public class ErrorMessage implements DomainObject {
    private int id;
    private int projectId;
    private int createdBy;
    private String text;
    private ErrorMessageStatus status;
}
