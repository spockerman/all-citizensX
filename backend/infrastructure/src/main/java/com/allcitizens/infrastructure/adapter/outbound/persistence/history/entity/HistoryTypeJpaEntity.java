package com.allcitizens.infrastructure.adapter.outbound.persistence.history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "history_type")
public class HistoryTypeJpaEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
