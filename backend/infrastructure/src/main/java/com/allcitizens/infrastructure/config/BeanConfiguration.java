package com.allcitizens.infrastructure.config;

import com.allcitizens.application.catalog.service.CatalogServiceApplicationService;
import com.allcitizens.application.catalog.usecase.CreateCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.DeleteCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.GetCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.ListCatalogServicesUseCase;
import com.allcitizens.application.catalog.usecase.UpdateCatalogServiceUseCase;
import com.allcitizens.application.address.service.AddressApplicationService;
import com.allcitizens.application.address.usecase.CreateAddressUseCase;
import com.allcitizens.application.address.usecase.DeleteAddressUseCase;
import com.allcitizens.application.address.usecase.GetAddressUseCase;
import com.allcitizens.application.address.usecase.UpdateAddressUseCase;
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
import com.allcitizens.application.subdivision.service.SubdivisionApplicationService;
import com.allcitizens.application.subdivision.usecase.CreateSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.DeleteSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.GetSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.ListSubdivisionsUseCase;
import com.allcitizens.application.subdivision.usecase.UpdateSubdivisionUseCase;
import com.allcitizens.application.person.service.PersonApplicationService;
import com.allcitizens.application.person.usecase.CreatePersonUseCase;
import com.allcitizens.application.person.usecase.DeletePersonUseCase;
import com.allcitizens.application.person.usecase.GetPersonUseCase;
import com.allcitizens.application.person.usecase.ListPersonsUseCase;
import com.allcitizens.application.person.usecase.UpdatePersonUseCase;
import com.allcitizens.application.subject.service.SubjectApplicationService;
import com.allcitizens.application.subject.usecase.CreateSubjectUseCase;
import com.allcitizens.application.subject.usecase.DeleteSubjectUseCase;
import com.allcitizens.application.subject.usecase.GetSubjectUseCase;
import com.allcitizens.application.subject.usecase.ListSubjectsUseCase;
import com.allcitizens.application.subject.usecase.UpdateSubjectUseCase;
import com.allcitizens.application.tenant.service.TenantApplicationService;
import com.allcitizens.application.tenant.usecase.DeleteTenantUseCase;
import com.allcitizens.domain.catalog.CatalogServiceRepository;
import com.allcitizens.domain.address.AddressRepository;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.domain.request.ServiceRequestRepository;
import com.allcitizens.domain.subdivision.SubdivisionRepository;
import com.allcitizens.domain.person.PersonRepository;
import com.allcitizens.domain.subject.SubjectRepository;
import com.allcitizens.domain.tenant.TenantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    // ── Catalog Service ──────────────────────────────────────────────────

    @Bean
    public CatalogServiceApplicationService catalogServiceApplicationService(
            CatalogServiceRepository repository) {
        return new CatalogServiceApplicationService(repository);
    }

    @Bean
    public CreateCatalogServiceUseCase createCatalogServiceUseCase(
            CatalogServiceApplicationService service) {
        return service::create;
    }

    @Bean
    public GetCatalogServiceUseCase getCatalogServiceUseCase(
            CatalogServiceApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateCatalogServiceUseCase updateCatalogServiceUseCase(
            CatalogServiceApplicationService service) {
        return service::update;
    }

    @Bean
    public ListCatalogServicesUseCase listCatalogServicesUseCase(
            CatalogServiceApplicationService service) {
        return service::listByTenantId;
    }

    @Bean
    public DeleteCatalogServiceUseCase deleteCatalogServiceUseCase(
            CatalogServiceApplicationService service) {
        return service::delete;
    }

    // ── Address ─────────────────────────────────────────────────────────

    @Bean
    public AddressApplicationService addressApplicationService(
            AddressRepository repository) {
        return new AddressApplicationService(repository);
    }

    @Bean
    public CreateAddressUseCase createAddressUseCase(
            AddressApplicationService service) {
        return service::create;
    }

    @Bean
    public GetAddressUseCase getAddressUseCase(
            AddressApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateAddressUseCase updateAddressUseCase(
            AddressApplicationService service) {
        return service::update;
    }

    @Bean
    public DeleteAddressUseCase deleteAddressUseCase(
            AddressApplicationService service) {
        return service::delete;
    }

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

    // ── Person ───────────────────────────────────────────────────────────

    @Bean
    public PersonApplicationService personApplicationService(
            PersonRepository repository) {
        return new PersonApplicationService(repository);
    }

    @Bean
    public CreatePersonUseCase createPersonUseCase(
            PersonApplicationService service) {
        return service::create;
    }

    @Bean
    public GetPersonUseCase getPersonUseCase(
            PersonApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdatePersonUseCase updatePersonUseCase(
            PersonApplicationService service) {
        return service::update;
    }

    @Bean
    public ListPersonsUseCase listPersonsUseCase(
            PersonApplicationService service) {
        return service::listByTenantId;
    }

    @Bean
    public DeletePersonUseCase deletePersonUseCase(
            PersonApplicationService service) {
        return service::delete;
    }

    // ── Subject ──────────────────────────────────────────────────────────

    @Bean
    public SubjectApplicationService subjectApplicationService(
            SubjectRepository repository) {
        return new SubjectApplicationService(repository);
    }

    @Bean
    public CreateSubjectUseCase createSubjectUseCase(
            SubjectApplicationService service) {
        return service::create;
    }

    @Bean
    public GetSubjectUseCase getSubjectUseCase(
            SubjectApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateSubjectUseCase updateSubjectUseCase(
            SubjectApplicationService service) {
        return service::update;
    }

    @Bean
    public ListSubjectsUseCase listSubjectsUseCase(
            SubjectApplicationService service) {
        return service::listByTenantId;
    }

    @Bean
    public DeleteSubjectUseCase deleteSubjectUseCase(
            SubjectApplicationService service) {
        return service::delete;
    }

    // ── Subdivision ──────────────────────────────────────────────────────

    @Bean
    public SubdivisionApplicationService subdivisionApplicationService(
            SubdivisionRepository repository) {
        return new SubdivisionApplicationService(repository);
    }

    @Bean
    public CreateSubdivisionUseCase createSubdivisionUseCase(
            SubdivisionApplicationService service) {
        return service::create;
    }

    @Bean
    public GetSubdivisionUseCase getSubdivisionUseCase(
            SubdivisionApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateSubdivisionUseCase updateSubdivisionUseCase(
            SubdivisionApplicationService service) {
        return service::update;
    }

    @Bean
    public ListSubdivisionsUseCase listSubdivisionsUseCase(
            SubdivisionApplicationService service) {
        return service::listAll;
    }

    @Bean
    public DeleteSubdivisionUseCase deleteSubdivisionUseCase(
            SubdivisionApplicationService service) {
        return service::delete;
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
