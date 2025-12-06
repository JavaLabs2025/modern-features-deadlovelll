package org.lab.api.adapters.error_message;

import java.util.Map;

import static org.mockito.Mockito.*;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.error_message.dto.CreateErrorMessageDTO;
import org.lab.application.error_message.dto.GetErrorMessageDTO;
import org.lab.application.error_message.use_cases.CreateErrorMessageUseCase;
import org.lab.core.constants.error_message.ErrorMessageStatus;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;

public class TestErrorMessageCreateAdapter {

    private CreateErrorMessageUseCase useCase;
    private ObjectMapper mapper;
    private ErrorMessageCreateAdapter adapter;
    private Context ctx;

    @BeforeEach
    void setup() {
        useCase = mock(CreateErrorMessageUseCase.class);
        mapper = mock(ObjectMapper.class);
        adapter = new ErrorMessageCreateAdapter(useCase, mapper);
        ctx = mock(Context.class);
    }

    @Test
    void createErrorMessage_success() {
        when(ctx.pathParam("employeeId")).thenReturn("10");

        CreateErrorMessageDTO bodyDto = new CreateErrorMessageDTO(1, "text");
        ErrorMessage domainMsg = new ErrorMessage();
        domainMsg.setId(5);
        domainMsg.setProjectId(1);
        domainMsg.setCreatedBy(10);
        domainMsg.setText("text");
        domainMsg.setStatus(ErrorMessageStatus.OPEN);

        ErrorMessage created = new ErrorMessage();
        created.setId(5);
        created.setProjectId(1);
        created.setCreatedBy(10);
        created.setText("text");
        created.setStatus(ErrorMessageStatus.OPEN);
        GetErrorMessageDTO dto = new GetErrorMessageDTO(
                5,
                1,
                10,
                "text",
                ErrorMessageStatus.OPEN
        );

        when(ctx.bodyAsClass(CreateErrorMessageDTO.class)).thenReturn(bodyDto);
        when(mapper.mapToDomain(bodyDto, ErrorMessage.class)).thenReturn(domainMsg);
        when(useCase.execute(domainMsg, 10)).thenReturn(created);
        when(mapper.mapToPresentation(created, GetErrorMessageDTO.class)).thenReturn(dto);

        when(ctx.status(201)).thenReturn(ctx);
        when(ctx.json(dto)).thenReturn(ctx);

        adapter.createErrorMessage(ctx);

        verify(ctx).status(201);
        verify(ctx).json(dto);
    }

    @Test
    void createErrorMessage_userNotFound() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.bodyAsClass(CreateErrorMessageDTO.class))
                .thenReturn(new CreateErrorMessageDTO(1,"x"));

        ErrorMessage expcMessage = new ErrorMessage();
        expcMessage.setId(0);
        expcMessage.setProjectId(1);
        expcMessage.setCreatedBy(10);
        expcMessage.setText("x");
        expcMessage.setStatus(ErrorMessageStatus.OPEN);

        when(mapper.mapToDomain(any(), eq(ErrorMessage.class))).thenReturn(expcMessage);

        when(useCase.execute(any(), eq(10))).thenThrow(new UserNotFoundException());

        when(ctx.status(409)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createErrorMessage(ctx);

        verify(ctx).status(409);
        verify(ctx).json(Map.of("error", "User doesnt exist"));
    }

    @Test
    void createErrorMessage_notPermitted() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.bodyAsClass(CreateErrorMessageDTO.class))
                .thenReturn(new CreateErrorMessageDTO(1,"x"));

        ErrorMessage expcMessage = new ErrorMessage();
        expcMessage.setId(0);
        expcMessage.setProjectId(1);
        expcMessage.setCreatedBy(10);
        expcMessage.setText("x");
        expcMessage.setStatus(ErrorMessageStatus.OPEN);

        when(mapper.mapToDomain(any(), eq(ErrorMessage.class))).thenReturn(expcMessage);

        when(useCase.execute(any(), eq(10)))
                .thenThrow(new NotPermittedException("not permitted"));

        when(ctx.status(403)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createErrorMessage(ctx);

        verify(ctx).status(403);
        verify(ctx).json(Map.of(
                "error", "You do not have permission to perform this operation"
        ));
    }

    @Test
    void createErrorMessage_projectNotFound() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.bodyAsClass(CreateErrorMessageDTO.class))
                .thenReturn(new CreateErrorMessageDTO(1,"x"));

        ErrorMessage expcMessage = new ErrorMessage();
        expcMessage.setId(0);
        expcMessage.setProjectId(1);
        expcMessage.setCreatedBy(10);
        expcMessage.setText("x");
        expcMessage.setStatus(ErrorMessageStatus.OPEN);

        when(mapper.mapToDomain(any(), eq(ErrorMessage.class)))
                .thenReturn(expcMessage);

        when(useCase.execute(any(), eq(10)))
                .thenThrow(new ProjectNotFoundException());

        when(ctx.status(404)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createErrorMessage(ctx);

        verify(ctx).status(404);
        verify(ctx).json(Map.of("error", "Project doesnt exist"));
    }

    @Test
    void createErrorMessage_internalServerError() {
        when(ctx.pathParam("employeeId")).thenReturn("10");
        when(ctx.bodyAsClass(CreateErrorMessageDTO.class))
                .thenReturn(new CreateErrorMessageDTO(1,"x"));

        ErrorMessage expcMessage = new ErrorMessage();
        expcMessage.setId(0);
        expcMessage.setProjectId(1);
        expcMessage.setCreatedBy(10);
        expcMessage.setText("x");
        expcMessage.setStatus(ErrorMessageStatus.OPEN);

        when(mapper.mapToDomain(any(), eq(ErrorMessage.class)))
                .thenReturn(expcMessage);

        when(useCase.execute(any(), eq(10)))
                .thenThrow(new RuntimeException("boom"));

        when(ctx.status(500)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.createErrorMessage(ctx);

        verify(ctx).status(500);
        verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
