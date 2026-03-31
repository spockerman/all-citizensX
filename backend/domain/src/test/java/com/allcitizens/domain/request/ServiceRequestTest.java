package com.allcitizens.domain.request;

import com.allcitizens.domain.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceRequestTest {

    @Test
    void shouldCreateValidServiceRequest() {
        var tenantId = UUID.randomUUID();
        var serviceId = UUID.randomUUID();

        var request = ServiceRequest.create(tenantId, "PROTO-001", serviceId,
                Channel.PHONE, Priority.NORMAL, "Test description");

        assertThat(request.getId()).isNotNull();
        assertThat(request.getTenantId()).isEqualTo(tenantId);
        assertThat(request.getProtocol()).isEqualTo("PROTO-001");
        assertThat(request.getServiceId()).isEqualTo(serviceId);
        assertThat(request.getChannel()).isEqualTo(Channel.PHONE);
        assertThat(request.getStatus()).isEqualTo(RequestStatus.OPEN);
        assertThat(request.getPriority()).isEqualTo(Priority.NORMAL);
        assertThat(request.getDescription()).isEqualTo("Test description");
        assertThat(request.getCreatedAt()).isNotNull();
        assertThat(request.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailWhenTenantIdIsNull() {
        assertThatThrownBy(() ->
                ServiceRequest.create(null, "PROTO-001", UUID.randomUUID(),
                        Channel.PHONE, Priority.NORMAL, "desc"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("tenantId");
    }

    @Test
    void shouldFailWhenProtocolIsNull() {
        assertThatThrownBy(() ->
                ServiceRequest.create(UUID.randomUUID(), null, UUID.randomUUID(),
                        Channel.PHONE, Priority.NORMAL, "desc"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("protocol");
    }

    @Test
    void shouldFailWhenServiceIdIsNull() {
        assertThatThrownBy(() ->
                ServiceRequest.create(UUID.randomUUID(), "PROTO-001", null,
                        Channel.PHONE, Priority.NORMAL, "desc"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("serviceId");
    }

    @Test
    void shouldForwardFromOpenStatus() {
        var request = createDefaultRequest();
        request.forward();
        assertThat(request.getStatus()).isEqualTo(RequestStatus.FORWARDED);
    }

    @Test
    void shouldNotForwardFromClosedStatus() {
        var request = createDefaultRequest();
        request.close();

        assertThatThrownBy(request::forward)
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot forward");
    }

    @Test
    void shouldAnswerRequest() {
        var request = createDefaultRequest();
        request.answer();
        assertThat(request.getStatus()).isEqualTo(RequestStatus.ANSWERED);
    }

    @Test
    void shouldCloseAndSetClosedAt() {
        var request = createDefaultRequest();
        request.close();

        assertThat(request.getStatus()).isEqualTo(RequestStatus.CLOSED);
        assertThat(request.getClosedAt()).isNotNull();
    }

    @Test
    void shouldCancelAndSetClosedAt() {
        var request = createDefaultRequest();
        request.cancel();

        assertThat(request.getStatus()).isEqualTo(RequestStatus.CANCELLED);
        assertThat(request.getClosedAt()).isNotNull();
    }

    @Test
    void shouldReopenFromClosedStatus() {
        var request = createDefaultRequest();
        request.close();
        request.reopen();

        assertThat(request.getStatus()).isEqualTo(RequestStatus.REOPENED);
        assertThat(request.getClosedAt()).isNull();
    }

    @Test
    void shouldReopenFromCancelledStatus() {
        var request = createDefaultRequest();
        request.cancel();
        request.reopen();

        assertThat(request.getStatus()).isEqualTo(RequestStatus.REOPENED);
        assertThat(request.getClosedAt()).isNull();
    }

    @Test
    void shouldNotReopenFromOpenStatus() {
        var request = createDefaultRequest();

        assertThatThrownBy(request::reopen)
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot reopen");
    }

    private ServiceRequest createDefaultRequest() {
        return ServiceRequest.create(UUID.randomUUID(), "PROTO-001", UUID.randomUUID(),
                Channel.PHONE, Priority.NORMAL, "Test");
    }
}
