package com.allcitizens.domain.notification;

import java.util.Objects;
import java.util.UUID;

public class NotificationRule {

    private static final int MAX_EVENT_LEN = 50;

    private UUID id;
    private UUID serviceId;
    private String event;
    private NotificationChannel channel;
    private String template;
    private boolean active;

    private NotificationRule() {
    }

    public static NotificationRule create(
            UUID serviceId, String event, NotificationChannel channel, String template) {
        Objects.requireNonNull(serviceId, "serviceId must not be null");
        Objects.requireNonNull(channel, "channel must not be null");
        validateEvent(event);
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("template must not be blank");
        }

        var r = new NotificationRule();
        r.id = UUID.randomUUID();
        r.serviceId = serviceId;
        r.event = event.trim();
        r.channel = channel;
        r.template = template;
        r.active = true;
        return r;
    }

    public static NotificationRule reconstitute(
            UUID id,
            UUID serviceId,
            String event,
            NotificationChannel channel,
            String template,
            boolean active) {
        var r = new NotificationRule();
        r.id = id;
        r.serviceId = serviceId;
        r.event = event;
        r.channel = channel;
        r.template = template;
        r.active = active;
        return r;
    }

    private static void validateEvent(String event) {
        if (event == null || event.isBlank()) {
            throw new IllegalArgumentException("event must not be blank");
        }
        if (event.length() > MAX_EVENT_LEN) {
            throw new IllegalArgumentException("event must not exceed " + MAX_EVENT_LEN + " characters");
        }
    }

    public void update(String event, NotificationChannel channel, String template, Boolean active) {
        if (event != null) {
            validateEvent(event);
            this.event = event.trim();
        }
        if (channel != null) {
            this.channel = channel;
        }
        if (template != null) {
            if (template.isBlank()) {
                throw new IllegalArgumentException("template must not be blank");
            }
            this.template = template;
        }
        if (active != null) {
            this.active = active;
        }
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getEvent() {
        return event;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getTemplate() {
        return template;
    }

    public boolean isActive() {
        return active;
    }
}
