package com.allcitizens.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.audit")
public class AuditProperties {

    /**
     * When {@code true}, GET requests under {@code /api/v1/} are also stored (noisy; default off).
     */
    private boolean logGetRequests = false;

    public boolean isLogGetRequests() {
        return logGetRequests;
    }

    public void setLogGetRequests(boolean logGetRequests) {
        this.logGetRequests = logGetRequests;
    }
}
