package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseEntity implements Persistable<Integer>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private boolean isNew = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @PrePersist
    public void prePersist() {
        this.isNew = false;
        LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.setUpdatedAt(now);
    }
}
