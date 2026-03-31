package com.allcitizens.infrastructure.config;

import com.allcitizens.application.department.service.DepartmentApplicationService;
import com.allcitizens.application.department.usecase.CreateDepartmentUseCase;
import com.allcitizens.application.department.usecase.DeleteDepartmentUseCase;
import com.allcitizens.application.department.usecase.GetDepartmentUseCase;
import com.allcitizens.application.department.usecase.ListDepartmentsUseCase;
import com.allcitizens.application.department.usecase.UpdateDepartmentUseCase;
import com.allcitizens.application.request.service.ServiceRequestApplicationService;
import com.allcitizens.application.request.usecase.CancelServiceRequestUseCase;
import com.allcitizens.application.request.usecase.CloseServiceRequestUseCase;
import com.allcitizens.application.request.usecase.GetServiceRequestUseCase;
import com.allcitizens.application.request.usecase.ListServiceRequestsUseCase;
import com.allcitizens.application.tenant.service.TenantApplicationService;
import com.allcitizens.application.tenant.usecase.DeleteTenantUseCase;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.domain.request.ServiceRequestRepository;
import com.allcitizens.domain.tenant.TenantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    // ── Department ──────────────────────────────────────────────────────

    @Bean
    public DepartmentApplicationService departmentApplicationService(
            DepartmentRepository repository) {
        return new DepartmentApplicationService(repository);
    }

    @Bean
    public CreateDepartmentUseCase createDepartmentUseCase(
            DepartmentApplicationService service) {
        return service::create;
    }

    @Bean
    public GetDepartmentUseCase getDepartmentUseCase(
            DepartmentApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateDepartmentUseCase updateDepartmentUseCase(
            DepartmentApplicationService service) {
        return service::update;
    }

    @Bean
    public ListDepartmentsUseCase listDepartmentsUseCase(
            DepartmentApplicationService service) {
        return service::listByTenantId;
    }

    @Bean
    public DeleteDepartmentUseCase deleteDepartmentUseCase(
            DepartmentApplicationService service) {
        return service::delete;
    }

    // ── Service Request ─────────────────────────────────────────────────

    @Bean
    public ServiceRequestApplicationService serviceRequestApplicationService(
            ServiceRequestRepository repository) {
        return new ServiceRequestApplicationService(repository);
    }

    @Bean
    public GetServiceRequestUseCase getServiceRequestUseCase(
            ServiceRequestApplicationService service) {
        return service::get;
    }

    @Bean
    public ListServiceRequestsUseCase listServiceRequestsUseCase(
            ServiceRequestApplicationService service) {
        return service::list;
    }

    @Bean
    public CloseServiceRequestUseCase closeServiceRequestUseCase(
            ServiceRequestApplicationService service) {
        return service::close;
    }

    @Bean
    public CancelServiceRequestUseCase cancelServiceRequestUseCase(
            ServiceRequestApplicationService service) {
        return service::cancel;
    }

    // ── Tenant ──────────────────────────────────────────────────────────

    @Bean
    public TenantApplicationService tenantApplicationService(
            TenantRepository repository) {
        return new TenantApplicationService(repository);
    }

    @Bean
    public DeleteTenantUseCase deleteTenantUseCase(
            TenantApplicationService service) {
        return service::delete;
    }
}
