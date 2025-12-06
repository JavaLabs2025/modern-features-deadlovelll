package org.lab.application.error_message.use_cases;

import com.google.inject.Inject;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.infra.db.repository.error_message.ErrorMessageRepository;
import org.lab.application.error_message.services.CreateErrorMessageValidator;

public class CreateErrorMessageUseCase {

    private final ErrorMessageRepository errorMessageRepository;
    private final CreateErrorMessageValidator createErrorMessageValidator;

    @Inject
    public CreateErrorMessageUseCase(
            ErrorMessageRepository errorMessageRepository,
            CreateErrorMessageValidator createErrorMessageValidator
    ) {
        this.errorMessageRepository = errorMessageRepository;
        this.createErrorMessageValidator = createErrorMessageValidator;
    }

    public ErrorMessage execute(
            ErrorMessage message,
            int employeeId
    ) {
        this.createErrorMessageValidator.validate(employeeId, message.getProjectId());
        ErrorMessage createdMessage = this.errorMessageRepository.create(message, employeeId);
        return createdMessage;
    }
}
