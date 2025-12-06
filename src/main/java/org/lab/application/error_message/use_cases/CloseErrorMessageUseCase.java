package org.lab.application.error_message.use_cases;

import com.google.inject.Inject;
import org.lab.application.error_message.services.ErrorMessageValidator;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.infra.db.repository.error_message.ErrorMessageRepository;

public class CloseErrorMessageUseCase {

    private final ErrorMessageValidator errorMessageValidator;
    private final ErrorMessageRepository errorMessageRepository;

    @Inject
    public CloseErrorMessageUseCase(
            ErrorMessageValidator errorMessageValidator,
            ErrorMessageRepository errorMessageRepository
    ) {
        this.errorMessageValidator = errorMessageValidator;
        this.errorMessageRepository = errorMessageRepository;
    }

    public ErrorMessage execute(
            int messageId
    ) {
        this.errorMessageValidator.validate(messageId);
        ErrorMessage message = this.errorMessageRepository.close(messageId);
        return message;
    }
}
