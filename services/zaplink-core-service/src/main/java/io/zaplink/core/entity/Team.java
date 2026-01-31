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
 * Team entity representing teams within an organization.
 * Each team can have multiple members with different roles.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Entity @Table(name = DatabaseConstants.TABLE_TEAMS) @EntityListeners(AuditingEntityListener.class) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public class Team
{
    /**
     * Primary key for the team.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /**
     * Name of the team.
     */
    @NotBlank @Size(max = 255) @Column(name = DatabaseConstants.COLUMN_NAME, nullable = false)
    private String  name;
    /**
     * Description of the team.
     */
    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION, columnDefinition = "TEXT")
    private String  description;
    /**
     * Organization ID this team belongs to.
     */
    @Column(name = DatabaseConstants.COLUMN_ORGANIZATION_ID, nullable = false)
    private Long    organizationId;
    /**
     * Flag indicating if the team is active.
     */
    @Builder.Default @Column(name = DatabaseConstants.COLUMN_IS_ACTIVE)
    private Boolean active = true;
    /**
     * Timestamp when the team was created.
     */
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private Instant createdAt;
    /**
     * Timestamp when the team was last updated.
     */
    @LastModifiedDate @Column(name = DatabaseConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt;
    /**
     * Custom getter for active status following Java conventions.
     * 
     * @return true if team is active, false otherwise
     */
    public boolean isActive()
    {
        return active;
    }
}
