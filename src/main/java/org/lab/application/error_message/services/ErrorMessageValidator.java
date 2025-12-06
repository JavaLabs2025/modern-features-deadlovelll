package org.lab.application.error_message.services;

import com.google.inject.Inject;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.MessageNotFoundException;
import org.lab.infra.db.repository.error_message.ErrorMessageRepository;

public class ErrorMessageValidator {

    private final ErrorMessageRepository errorMessageRepository;

    @Inject
    public ErrorMessageValidator(
            ErrorMessageRepository errorMessageRepository
    ) {
        this.errorMessageRepository = errorMessageRepository;
    }

    public void validate(int messageId) {
        ErrorMessage message = this.errorMessageRepository.get(messageId);
        if (message == null) {
            throw new MessageNotFoundException();
        }
    }
}
