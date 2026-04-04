package com.allcitizens.infrastructure.config;

import com.allcitizens.application.audit.service.AuditLogApplicationService;
import com.allcitizens.infrastructure.adapter.inbound.web.AuditLoggingFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuditProperties.class)
public class AuditConfiguration {

    @Bean
    public AuditLoggingFilter auditLoggingFilter(
            AuditLogApplicationService auditLogApplicationService,
            AuditProperties auditProperties) {
        return new AuditLoggingFilter(auditLogApplicationService, auditProperties);
    }
}
