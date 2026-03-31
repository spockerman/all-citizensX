package com.allcitizens.application.tenant.command;

public record UpdateTenantCommand(
    String name,
    Boolean active
) {
}
