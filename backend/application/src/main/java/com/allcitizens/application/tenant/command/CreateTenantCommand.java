package com.allcitizens.application.tenant.command;

public record CreateTenantCommand(
    String name,
    String code
) {
}
