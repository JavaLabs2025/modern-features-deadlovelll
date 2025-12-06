package org.lab.api.adapters.error_message;

import java.util.Map;

import com.google.inject.Inject;
import io.javalin.http.Context;

import org.lab.application.error_message.dto.GetErrorMessageDTO;
import org.lab.application.error_message.use_cases.CloseErrorMessageUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.MessageNotFoundException;

public class ErrorMessageCloseAdapter {

    private final CloseErrorMessageUseCase useCase;
    private final ObjectMapper mapper;

    @Inject
    public ErrorMessageCloseAdapter(
            CloseErrorMessageUseCase useCase,
            ObjectMapper mapper
    ) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    public Context closeMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("messageId"));
            ErrorMessage createdMessage = useCase.execute(messageId);
            GetErrorMessageDTO presentationMessage = mapper.mapToPresentation(
                    createdMessage,
                    GetErrorMessageDTO.class
            );
            return ctx.status(201).json(presentationMessage);

        } catch (MessageNotFoundException e) {
            return ctx.status(409).json(Map.of("error", "Message doesnt exist"));

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
