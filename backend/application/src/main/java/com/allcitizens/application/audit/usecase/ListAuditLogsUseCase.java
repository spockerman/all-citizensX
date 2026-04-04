package com.allcitizens.application.audit.usecase;

import com.allcitizens.application.audit.query.ListAuditLogsQuery;
import com.allcitizens.application.audit.result.AuditLogResult;
import com.allcitizens.domain.common.PageResult;

@FunctionalInterface
public interface ListAuditLogsUseCase {

    PageResult<AuditLogResult> execute(ListAuditLogsQuery query);
}
