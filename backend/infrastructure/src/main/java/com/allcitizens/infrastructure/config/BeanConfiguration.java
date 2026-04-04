package com.allcitizens.infrastructure.config;

import com.allcitizens.application.catalog.service.CatalogServiceApplicationService;
import com.allcitizens.application.catalog.usecase.CreateCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.DeleteCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.GetCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.ListCatalogServicesUseCase;
import com.allcitizens.application.catalog.usecase.UpdateCatalogServiceUseCase;
import com.allcitizens.application.attachment.service.AttachmentApplicationService;
import com.allcitizens.application.attachment.usecase.CreateAttachmentUseCase;
import com.allcitizens.application.attachment.usecase.DeleteAttachmentUseCase;
import com.allcitizens.application.attachment.usecase.GetAttachmentUseCase;
import com.allcitizens.application.attachment.usecase.ListAttachmentsByRequestUseCase;
import com.allcitizens.application.address.service.AddressApplicationService;
import com.allcitizens.application.address.usecase.CreateAddressUseCase;
import com.allcitizens.application.address.usecase.DeleteAddressUseCase;
import com.allcitizens.application.address.usecase.GetAddressUseCase;
import com.allcitizens.application.address.usecase.UpdateAddressUseCase;
import com.allcitizens.application.forwarding.service.ForwardingApplicationService;
import com.allcitizens.application.forwarding.service.ForwardingReasonApplicationService;
import com.allcitizens.application.forwarding.usecase.AddForwardingAnswerUseCase;
import com.allcitizens.application.forwarding.usecase.CreateForwardingUseCase;
import com.allcitizens.application.forwarding.usecase.CreateRedistributionUseCase;
import com.allcitizens.application.forwarding.usecase.GetForwardingUseCase;
import com.allcitizens.application.forwarding.usecase.ListForwardingAnswersUseCase;
import com.allcitizens.application.forwarding.usecase.ListForwardingsByRequestUseCase;
import com.allcitizens.application.forwarding.usecase.ListRedistributionsUseCase;
import com.allcitizens.application.forwarding.usecase.UpdateForwardingUseCase;
import com.allcitizens.application.history.service.HistoryTypeApplicationService;
import com.allcitizens.application.history.service.RequestHistoryApplicationService;
import com.allcitizens.application.department.service.DepartmentApplicationService;
import com.allcitizens.application.department.usecase.CreateDepartmentUseCase;
import com.allcitizens.application.department.usecase.DeleteDepartmentUseCase;
import com.allcitizens.application.department.usecase.GetDepartmentUseCase;
import com.allcitizens.application.department.usecase.ListDepartmentsUseCase;
import com.allcitizens.application.department.usecase.UpdateDepartmentUseCase;
import com.allcitizens.application.request.service.ServiceRequestApplicationService;
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
import com.allcitizens.application.notification.service.NotificationApplicationService;
import com.allcitizens.application.notification.service.NotificationRuleApplicationService;
import com.allcitizens.application.notification.usecase.CreateNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.CreateNotificationUseCase;
import com.allcitizens.application.notification.usecase.DeleteNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationRulesUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationsUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationStatusUseCase;
import com.allcitizens.application.audit.service.AuditLogApplicationService;
import com.allcitizens.application.audit.usecase.ListAuditLogsUseCase;
import com.allcitizens.application.tenant.service.TenantApplicationService;
import com.allcitizens.domain.audit.AuditLogRepository;
import com.allcitizens.domain.notification.NotificationRepository;
import com.allcitizens.domain.notification.NotificationRuleRepository;
import com.allcitizens.domain.attachment.AttachmentRepository;
import com.allcitizens.domain.catalog.CatalogServiceRepository;
import com.allcitizens.domain.address.AddressRepository;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.domain.forwarding.ForwardingAnswerRepository;
import com.allcitizens.domain.forwarding.ForwardingReasonRepository;
import com.allcitizens.domain.forwarding.ForwardingRepository;
import com.allcitizens.domain.forwarding.RedistributionRepository;
import com.allcitizens.domain.history.HistoryTypeRepository;
import com.allcitizens.domain.history.RequestHistoryRepository;
import com.allcitizens.domain.request.ServiceRequestRepository;
import com.allcitizens.domain.subdivision.SubdivisionRepository;
import com.allcitizens.domain.person.PersonRepository;
import com.allcitizens.domain.subject.SubjectRepository;
import com.allcitizens.domain.tenant.TenantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
    public GetCatalogServiceUseCase getCatalogServiceUseCase(CatalogServiceApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateCatalogServiceUseCase updateCatalogServiceUseCase(
            CatalogServiceApplicationService service) {
        return service::update;
    }

    @Bean
    @Primary
    public ListCatalogServicesUseCase listCatalogServicesUseCase(
            CatalogServiceApplicationService service) {
        return service;
    }

    @Bean
    public DeleteCatalogServiceUseCase deleteCatalogServiceUseCase(
            CatalogServiceApplicationService service) {
        return service::delete;
    }

    // ── Address ─────────────────────────────────────────────────────────

    @Bean
    public AddressApplicationService addressApplicationService(AddressRepository repository) {
        return new AddressApplicationService(repository);
    }

    @Bean
    public CreateAddressUseCase createAddressUseCase(AddressApplicationService service) {
        return service::create;
    }

    @Bean
    public GetAddressUseCase getAddressUseCase(AddressApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateAddressUseCase updateAddressUseCase(AddressApplicationService service) {
        return service::update;
    }

    @Bean
    public DeleteAddressUseCase deleteAddressUseCase(AddressApplicationService service) {
        return service::delete;
    }

    // ── Department ──────────────────────────────────────────────────────

    @Bean
    public DepartmentApplicationService departmentApplicationService(DepartmentRepository repository) {
        return new DepartmentApplicationService(repository);
    }

    @Bean
    public CreateDepartmentUseCase createDepartmentUseCase(DepartmentApplicationService service) {
        return service::create;
    }

    @Bean
    public GetDepartmentUseCase getDepartmentUseCase(DepartmentApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateDepartmentUseCase updateDepartmentUseCase(DepartmentApplicationService service) {
        return service::update;
    }

    @Bean
    @Primary
    public ListDepartmentsUseCase listDepartmentsUseCase(DepartmentApplicationService service) {
        return service;
    }

    @Bean
    public DeleteDepartmentUseCase deleteDepartmentUseCase(DepartmentApplicationService service) {
        return service::delete;
    }

    // ── Service Request ─────────────────────────────────────────────────

    @Bean
    public ServiceRequestApplicationService serviceRequestApplicationService(
            ServiceRequestRepository repository) {
        return new ServiceRequestApplicationService(repository);
    }

    // ── Person ───────────────────────────────────────────────────────────

    @Bean
    public PersonApplicationService personApplicationService(PersonRepository repository) {
        return new PersonApplicationService(repository);
    }

    @Bean
    public CreatePersonUseCase createPersonUseCase(PersonApplicationService service) {
        return service::create;
    }

    @Bean
    public GetPersonUseCase getPersonUseCase(PersonApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdatePersonUseCase updatePersonUseCase(PersonApplicationService service) {
        return service::update;
    }

    @Bean
    @Primary
    public ListPersonsUseCase listPersonsUseCase(PersonApplicationService service) {
        return service;
    }

    @Bean
    public DeletePersonUseCase deletePersonUseCase(PersonApplicationService service) {
        return service::delete;
    }

    // ── Subject ──────────────────────────────────────────────────────────

    @Bean
    public SubjectApplicationService subjectApplicationService(SubjectRepository repository) {
        return new SubjectApplicationService(repository);
    }

    @Bean
    public CreateSubjectUseCase createSubjectUseCase(SubjectApplicationService service) {
        return service::create;
    }

    @Bean
    public GetSubjectUseCase getSubjectUseCase(SubjectApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateSubjectUseCase updateSubjectUseCase(SubjectApplicationService service) {
        return service::update;
    }

    @Bean
    @Primary
    public ListSubjectsUseCase listSubjectsUseCase(SubjectApplicationService service) {
        return service;
    }

    @Bean
    public DeleteSubjectUseCase deleteSubjectUseCase(SubjectApplicationService service) {
        return service::delete;
    }

    // ── Subdivision ──────────────────────────────────────────────────────

    @Bean
    public SubdivisionApplicationService subdivisionApplicationService(SubdivisionRepository repository) {
        return new SubdivisionApplicationService(repository);
    }

    @Bean
    public CreateSubdivisionUseCase createSubdivisionUseCase(SubdivisionApplicationService service) {
        return service::create;
    }

    @Bean
    public GetSubdivisionUseCase getSubdivisionUseCase(SubdivisionApplicationService service) {
        return service::getById;
    }

    @Bean
    public UpdateSubdivisionUseCase updateSubdivisionUseCase(SubdivisionApplicationService service) {
        return service::update;
    }

    @Bean
    @Primary
    public ListSubdivisionsUseCase listSubdivisionsUseCase(SubdivisionApplicationService service) {
        return service;
    }

    @Bean
    public DeleteSubdivisionUseCase deleteSubdivisionUseCase(SubdivisionApplicationService service) {
        return service::delete;
    }

    // ── Tenant ──────────────────────────────────────────────────────────

    @Bean
    public TenantApplicationService tenantApplicationService(TenantRepository repository) {
        return new TenantApplicationService(repository);
    }

    // ── Forwarding ───────────────────────────────────────────────────────

    @Bean
    public ForwardingReasonApplicationService forwardingReasonApplicationService(
            ForwardingReasonRepository repository) {
        return new ForwardingReasonApplicationService(repository);
    }

    @Bean
    public ForwardingApplicationService forwardingApplicationService(
            ForwardingRepository forwardingRepository,
            ForwardingAnswerRepository forwardingAnswerRepository,
            RedistributionRepository redistributionRepository,
            ServiceRequestRepository serviceRequestRepository,
            DepartmentRepository departmentRepository) {
        return new ForwardingApplicationService(
                forwardingRepository,
                forwardingAnswerRepository,
                redistributionRepository,
                serviceRequestRepository,
                departmentRepository);
    }

    @Bean
    public CreateForwardingUseCase createForwardingUseCase(ForwardingApplicationService service) {
        return service::create;
    }

    @Bean
    public GetForwardingUseCase getForwardingUseCase(ForwardingApplicationService service) {
        return service::getById;
    }

    @Bean
    public ListForwardingsByRequestUseCase listForwardingsByRequestUseCase(
            ForwardingApplicationService service) {
        return service::listByRequestId;
    }

    @Bean
    public UpdateForwardingUseCase updateForwardingUseCase(ForwardingApplicationService service) {
        return service::update;
    }

    @Bean
    public AddForwardingAnswerUseCase addForwardingAnswerUseCase(ForwardingApplicationService service) {
        return service::addAnswer;
    }

    @Bean
    public ListForwardingAnswersUseCase listForwardingAnswersUseCase(
            ForwardingApplicationService service) {
        return service::listAnswersByForwardingId;
    }

    @Bean
    public CreateRedistributionUseCase createRedistributionUseCase(
            ForwardingApplicationService service) {
        return service::createRedistribution;
    }

    @Bean
    public ListRedistributionsUseCase listRedistributionsUseCase(ForwardingApplicationService service) {
        return service::listRedistributionsByForwardingId;
    }

    // ── Request history ────────────────────────────────────────────────────

    @Bean
    public HistoryTypeApplicationService historyTypeApplicationService(HistoryTypeRepository repository) {
        return new HistoryTypeApplicationService(repository);
    }

    @Bean
    public RequestHistoryApplicationService requestHistoryApplicationService(
            RequestHistoryRepository historyRepository,
            ServiceRequestRepository serviceRequestRepository,
            HistoryTypeRepository historyTypeRepository) {
        return new RequestHistoryApplicationService(
                historyRepository, serviceRequestRepository, historyTypeRepository);
    }

    // ── Attachments ───────────────────────────────────────────────────────

    @Bean
    public AttachmentApplicationService attachmentApplicationService(
            AttachmentRepository attachmentRepository,
            ServiceRequestRepository serviceRequestRepository) {
        return new AttachmentApplicationService(attachmentRepository, serviceRequestRepository);
    }

    @Bean
    public CreateAttachmentUseCase createAttachmentUseCase(AttachmentApplicationService service) {
        return service::create;
    }

    @Bean
    public GetAttachmentUseCase getAttachmentUseCase(AttachmentApplicationService service) {
        return service::getById;
    }

    @Bean
    public ListAttachmentsByRequestUseCase listAttachmentsByRequestUseCase(
            AttachmentApplicationService service) {
        return service::listByRequestId;
    }

    @Bean
    public DeleteAttachmentUseCase deleteAttachmentUseCase(AttachmentApplicationService service) {
        return service::deleteById;
    }

    // ── Notifications ─────────────────────────────────────────────────────

    @Bean
    public NotificationApplicationService notificationApplicationService(
            NotificationRepository notificationRepository,
            TenantRepository tenantRepository,
            ServiceRequestRepository serviceRequestRepository) {
        return new NotificationApplicationService(
                notificationRepository, tenantRepository, serviceRequestRepository);
    }

    @Bean
    public CreateNotificationUseCase createNotificationUseCase(NotificationApplicationService service) {
        return service;
    }

    @Bean
    public GetNotificationUseCase getNotificationUseCase(NotificationApplicationService service) {
        return service;
    }

    @Bean
    public ListNotificationsUseCase listNotificationsUseCase(NotificationApplicationService service) {
        return service;
    }

    @Bean
    public UpdateNotificationStatusUseCase updateNotificationStatusUseCase(NotificationApplicationService service) {
        return service;
    }

    @Bean
    public NotificationRuleApplicationService notificationRuleApplicationService(
            NotificationRuleRepository ruleRepository, CatalogServiceRepository catalogServiceRepository) {
        return new NotificationRuleApplicationService(ruleRepository, catalogServiceRepository);
    }

    @Bean
    public CreateNotificationRuleUseCase createNotificationRuleUseCase(NotificationRuleApplicationService service) {
        return service;
    }

    @Bean
    public GetNotificationRuleUseCase getNotificationRuleUseCase(NotificationRuleApplicationService service) {
        return service;
    }

    @Bean
    public UpdateNotificationRuleUseCase updateNotificationRuleUseCase(NotificationRuleApplicationService service) {
        return service;
    }

    @Bean
    public DeleteNotificationRuleUseCase deleteNotificationRuleUseCase(NotificationRuleApplicationService service) {
        return service::deleteById;
    }

    @Bean
    public ListNotificationRulesUseCase listNotificationRulesUseCase(NotificationRuleApplicationService service) {
        return service::listForService;
    }

    // ── Audit log ─────────────────────────────────────────────────────────

    @Bean
    public AuditLogApplicationService auditLogApplicationService(AuditLogRepository auditLogRepository) {
        return new AuditLogApplicationService(auditLogRepository);
    }

    @Bean
    public ListAuditLogsUseCase listAuditLogsUseCase(AuditLogApplicationService service) {
        return service;
    }
}
