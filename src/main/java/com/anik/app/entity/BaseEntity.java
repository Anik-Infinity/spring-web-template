package com.anik.app.entity;

import com.anik.app.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private Long version;

    @Basic
    @Column(nullable = false, updatable = false)
//    @CreatedDate
    private Instant createdAt;

    @Basic
    @Column(nullable = false)
//    @LastModifiedDate
    private Instant updatedAt;

    /*@Basic
    @Column(nullable = false)
    @CreatedBy
    private UUID createdBy;*/

    /*@Basic
    @Column(nullable = false)
    @LastModifiedBy
    private UUID updatedBy;*/

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @JsonIgnore
    @Builder.Default
    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
