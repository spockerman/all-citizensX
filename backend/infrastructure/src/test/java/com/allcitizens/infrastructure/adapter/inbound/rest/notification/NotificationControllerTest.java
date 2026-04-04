package com.allcitizens.infrastructure.adapter.inbound.rest.notification;

import com.allcitizens.application.notification.result.NotificationResult;
import com.allcitizens.application.notification.usecase.CreateNotificationUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationsUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationStatusUseCase;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.CreateNotificationRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.mapper.NotificationRestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(NotificationRestMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateNotificationUseCase createNotificationUseCase;

    @MockBean
    private GetNotificationUseCase getNotificationUseCase;

    @MockBean
    private ListNotificationsUseCase listNotificationsUseCase;

    @MockBean
    private UpdateNotificationStatusUseCase updateNotificationStatusUseCase;

    @Test
    void createReturns201() throws Exception {
        var tenantId = UUID.randomUUID();
        var result = new NotificationResult(
                UUID.randomUUID(),
                tenantId,
                null,
                null,
                "EMAIL",
                "t",
                "msg",
                "PENDING",
                null,
                Instant.now(),
                null,
                null);
        when(createNotificationUseCase.execute(any())).thenReturn(result);

        var body = new CreateNotificationRequest(tenantId, null, null, "EMAIL", "t", "msg", null);
        mockMvc.perform(
                        post("/api/v1/notifications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void listReturnsPage() throws Exception {
        var tenantId = UUID.randomUUID();
        var r = new NotificationResult(
                UUID.randomUUID(),
                tenantId,
                null,
                null,
                "SMS",
                null,
                "x",
                "PENDING",
                null,
                Instant.now(),
                null,
                null);
        when(listNotificationsUseCase.execute(any())).thenReturn(new PageResult<>(List.of(r), 1, 0, 20));

        mockMvc.perform(get("/api/v1/notifications").param("tenantId", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].channel").value("SMS"));
    }
}
