package com.pj.worldRestaurantTourbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Countries {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "next")
    private boolean next;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
