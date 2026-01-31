package io.zaplink.core.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.zaplink.core.common.constants.DatabaseConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Organization entity representing companies or teams using the Zaplink platform.
 * Each organization can have multiple teams and members.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Entity @Table(name = DatabaseConstants.TABLE_ORGANIZATIONS) @EntityListeners(AuditingEntityListener.class) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public class Organization
{
    /**
     * Primary key for the organization.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /**
     * Name of the organization.
     */
    @NotBlank @Size(max = 255) @Column(name = DatabaseConstants.COLUMN_NAME, nullable = false)
    private String  name;
    /**
     * Description of the organization.
     */
    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION, columnDefinition = "TEXT")
    private String  description;
    /**
     * User ID of the organization owner.
     */
    @Column(name = DatabaseConstants.COLUMN_OWNER_ID, nullable = false)
    private Long    ownerId;
    /**
     * Flag indicating if the organization is active.
     */
    @Builder.Default @Column(name = DatabaseConstants.COLUMN_IS_ACTIVE)
    private Boolean active = true;
    /**
     * Timestamp when the organization was created.
     */
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private Instant createdAt;
    /**
     * Timestamp when the organization was last updated.
     */
    @LastModifiedDate @Column(name = DatabaseConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt;
    /**
     * Custom getter for active status following Java conventions.
     * 
     * @return true if organization is active, false otherwise
     */
    public boolean isActive()
    {
        return active;
    }
}
