package com.pj.worldRestaurantTourbe.type.entity;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractEntity {


    @Column(name = "created_at", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name = "updated_at", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;

    @PrePersist
    public void onPrePersist() {
        this.setCreated_at(LocalDateTime.now());
        this.setUpdated_at(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdated_at(LocalDateTime.now());
    }
}
