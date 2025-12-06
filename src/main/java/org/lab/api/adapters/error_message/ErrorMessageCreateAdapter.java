package org.lab.api.adapters.error_message;

import com.google.inject.Inject;
import io.javalin.http.Context;

import org.lab.application.error_message.dto.CreateErrorMessageDTO;
import org.lab.application.error_message.dto.GetErrorMessageDTO;
import org.lab.application.error_message.use_cases.CreateErrorMessageUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

import java.util.Map;

public class ErrorMessageCreateAdapter {

    private final CreateErrorMessageUseCase useCase;
    private final ObjectMapper mapper;

    @Inject
    public ErrorMessageCreateAdapter(
            CreateErrorMessageUseCase useCase,
            ObjectMapper mapper
    ) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    public Context createErrorMessage(
            Context ctx
    ) {
        try {
            int actorId = Integer.parseInt(ctx.pathParam("actorId"));
            CreateErrorMessageDTO dto = ctx.bodyAsClass(CreateErrorMessageDTO.class);
            ErrorMessage errorMessage = mapper.mapToDomain(dto, ErrorMessage.class);
            ErrorMessage createdMessage = useCase.execute(
                    errorMessage,
                    actorId
            );
            GetErrorMessageDTO presentationMessage = mapper.mapToPresentation(
                    createdMessage,
                    GetErrorMessageDTO.class
            );
            return ctx.status(201).json(presentationMessage);

        } catch (UserNotFoundException e) {
            return ctx.status(409).json(Map.of("error", "User doesnt exist"));

        } catch (NotPermittedException e) {
            return ctx.status(403).json(
                    Map.of(
                            "error",
                            "You do not have permission to perform this operation"
                    )
            );

        } catch (ProjectNotFoundException e) {
            return ctx.status(404).json(Map.of("error", "Project doesnt exist"));

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
