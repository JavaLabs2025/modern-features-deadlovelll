package org.lab.application.error_message.dto;

import org.lab.domain.interfaces.PresentationObject;

public record CreateErrorMessageDTO(
        int projectId,
        String text
) implements PresentationObject {
}
