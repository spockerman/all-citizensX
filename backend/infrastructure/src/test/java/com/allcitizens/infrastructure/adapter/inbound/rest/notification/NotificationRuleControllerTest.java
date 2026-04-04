package com.allcitizens.infrastructure.adapter.inbound.rest.notification;

import com.allcitizens.application.notification.result.NotificationRuleResult;
import com.allcitizens.application.notification.usecase.CreateNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.DeleteNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationRulesUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationRuleUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.CreateNotificationRuleRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.mapper.NotificationRuleRestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationRuleController.class)
@Import(NotificationRuleRestMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateNotificationRuleUseCase createNotificationRuleUseCase;

    @MockBean
    private GetNotificationRuleUseCase getNotificationRuleUseCase;

    @MockBean
    private UpdateNotificationRuleUseCase updateNotificationRuleUseCase;

    @MockBean
    private DeleteNotificationRuleUseCase deleteNotificationRuleUseCase;

    @MockBean
    private ListNotificationRulesUseCase listNotificationRulesUseCase;

    @Test
    void createRule201() throws Exception {
        var sid = UUID.randomUUID();
        var res = new NotificationRuleResult(UUID.randomUUID(), sid, "E", "PUSH", "Tpl", true);
        when(createNotificationRuleUseCase.execute(any())).thenReturn(res);

        var body = new CreateNotificationRuleRequest(sid, "E", "PUSH", "Tpl");
        mockMvc.perform(
                        post("/api/v1/notification-rules")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.event").value("E"));
    }

    @Test
    void listByService() throws Exception {
        var sid = UUID.randomUUID();
        when(listNotificationRulesUseCase.listForService(sid))
                .thenReturn(List.of(new NotificationRuleResult(
                        UUID.randomUUID(), sid, "A", "EMAIL", "T", true)));

        mockMvc.perform(get("/api/v1/notification-rules").param("serviceId", sid.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].channel").value("EMAIL"));
    }
}
