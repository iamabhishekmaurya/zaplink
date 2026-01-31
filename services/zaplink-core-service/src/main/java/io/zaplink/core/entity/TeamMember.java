package io.zaplink.core.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.zaplink.core.common.constants.DatabaseConstants;
import io.zaplink.core.common.enums.TeamMemberRole;
import io.zaplink.core.common.enums.TeamMemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TeamMember entity representing users who are members of teams with specific roles.
 * This is the core entity for RBAC implementation.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Entity @Table(name = DatabaseConstants.TABLE_TEAM_MEMBERS, uniqueConstraints = @UniqueConstraint(columnNames =
{ DatabaseConstants.COLUMN_TEAM_ID,
  DatabaseConstants.COLUMN_USER_ID })) @EntityListeners(AuditingEntityListener.class) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public class TeamMember
{
    /**
     * Primary key for the team member.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /**
     * Team ID this member belongs to.
     */
    @NotNull @Column(name = DatabaseConstants.COLUMN_TEAM_ID, nullable = false)
    private Long    teamId;
    /**
     * User ID of the team member.
     */
    @NotNull @Column(name = DatabaseConstants.COLUMN_USER_ID, nullable = false)
    private Long    userId;
    /**
     * Role of the team member within the team.
     * Valid values: ADMIN, EDITOR, APPROVER, VIEWER, INFLUENCER
     */
    @NotBlank @Column(name = DatabaseConstants.COLUMN_ROLE, nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamMemberRole role;
    /**
     * Status of the team member.
     * Valid values: ACTIVE, INACTIVE, PENDING
     */
    @Builder.Default @Column(name = DatabaseConstants.COLUMN_STATUS)
    @Enumerated(EnumType.STRING)
    private TeamMemberStatus status = TeamMemberStatus.ACTIVE;
    /**
     * User ID who invited this member.
     */
    @NotNull @Column(name = DatabaseConstants.COLUMN_INVITED_BY, nullable = false)
    private Long    invitedBy;
    /**
     * Timestamp when the member was invited.
     */
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_INVITED_AT, nullable = false, updatable = false)
    private Instant invitedAt;
    /**
     * Timestamp when the member joined the team.
     */
    @Column(name = DatabaseConstants.COLUMN_JOINED_AT)
    private Instant joinedAt;
    /**
     * Timestamp when the team member record was created.
     */
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private Instant createdAt;
    /**
     * Timestamp when the team member record was last updated.
     */
    @LastModifiedDate @Column(name = DatabaseConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt;
}
