package com.allcitizens.application.audit.query;

import java.time.Instant;

public record ListAuditLogsQuery(int page, int size, Instant fromInclusive, Instant toInclusive) {
}
