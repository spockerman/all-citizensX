package com.allcitizens.infrastructure.adapter.inbound.rest.audit;

import com.allcitizens.application.audit.result.AuditLogResult;
import com.allcitizens.application.audit.usecase.ListAuditLogsUseCase;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.audit.mapper.AuditLogRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditLogController.class)
@Import(AuditLogRestMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListAuditLogsUseCase listAuditLogsUseCase;

    @Test
    void listReturnsPage() throws Exception {
        var id = UUID.randomUUID();
        var at = Instant.parse("2026-04-01T12:00:00Z");
        var result = new AuditLogResult(id, at, "sub", "user", "SUPERVISAO", "POST", "/api/v1/x", "127.0.0.1", 201, "c");
        when(listAuditLogsUseCase.execute(any())).thenReturn(new PageResult<>(List.of(result), 1, 0, 20));

        mockMvc.perform(get("/api/v1/audit-logs").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].httpMethod").value("POST"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
