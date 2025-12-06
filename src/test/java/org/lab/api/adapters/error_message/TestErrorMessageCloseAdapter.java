package org.lab.api.adapters.error_message;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.application.error_message.dto.GetErrorMessageDTO;
import org.lab.application.error_message.use_cases.CloseErrorMessageUseCase;
import org.lab.core.constants.error_message.ErrorMessageStatus;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.MessageNotFoundException;

public class TestErrorMessageCloseAdapter  {

    private CloseErrorMessageUseCase useCase;
    private ObjectMapper mapper;
    private ErrorMessageCloseAdapter adapter;
    private Context ctx;

    @BeforeEach
    void setup() {
        useCase = mock(CloseErrorMessageUseCase.class);
        mapper = mock(ObjectMapper.class);
        adapter = new ErrorMessageCloseAdapter(useCase, mapper);
        ctx = mock(Context.class);
    }

    @Test
    void closeMessage_success() {
        when(ctx.pathParam("messageId")).thenReturn("1");

        ErrorMessage msg = new ErrorMessage();
        msg.setId(1);
        msg.setProjectId(100);
        msg.setText("Some text");
        msg.setStatus(ErrorMessageStatus.CLOSED);
        msg.setCreatedBy(999);

        GetErrorMessageDTO dto = new GetErrorMessageDTO(
                1,
                100,
                999,
                "Some text",
                ErrorMessageStatus.CLOSED
        );

        when(useCase.execute(1)).thenReturn(msg);
        when(mapper.mapToPresentation(msg, GetErrorMessageDTO.class)).thenReturn(dto);
        when(ctx.status(201)).thenReturn(ctx);
        when(ctx.json(dto)).thenReturn(ctx);

        Context result = adapter.closeMessage(ctx);

        verify(ctx).status(201);
        verify(ctx).json(dto);
        assertEquals(ctx, result);
    }

    @Test
    void closeMessage_messageNotFound() {
        when(ctx.pathParam("messageId")).thenReturn("5");

        when(useCase.execute(5)).thenThrow(new MessageNotFoundException());

        when(ctx.status(409)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.closeMessage(ctx);

        verify(ctx).status(409);
        verify(ctx).json(Map.of("error", "Message doesnt exist"));
    }

    @Test
    void closeMessage_internalServerError() {
        when(ctx.pathParam("messageId")).thenReturn("10");

        when(useCase.execute(10)).thenThrow(new RuntimeException("boom"));

        when(ctx.status(500)).thenReturn(ctx);
        when(ctx.json(any())).thenReturn(ctx);

        adapter.closeMessage(ctx);

        verify(ctx).status(500);
        verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
