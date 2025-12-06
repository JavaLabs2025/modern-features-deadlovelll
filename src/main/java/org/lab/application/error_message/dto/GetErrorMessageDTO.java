package org.lab.application.error_message.dto;

import org.lab.core.constants.error_message.ErrorMessageStatus;
import org.lab.domain.interfaces.PresentationObject;

public record GetErrorMessageDTO(
        int id,
        int projectId,
        int createdBy,
        String text,
        ErrorMessageStatus status
) implements PresentationObject {
}