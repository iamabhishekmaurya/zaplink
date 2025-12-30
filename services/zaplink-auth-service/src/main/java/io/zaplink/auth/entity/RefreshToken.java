package io.zaplink.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.zaplink.auth.common.constants.DatabaseConstants;

import java.time.Instant;

/**
 * RefreshToken entity for JWT refresh token management.
 * Uses modern Java 17 features with builder pattern and modern time API.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = DatabaseConstants.TABLE_REFRESH_TOKENS)
@AllArgsConstructor
@EqualsAndHashCode(exclude =
{ "user" })
public class RefreshToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    @Column(nullable = false)
    private String  token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID, nullable = false)
    private User    user;
    @Column(name = DatabaseConstants.COLUMN_EXPIRY_DATE, nullable = false)
    private Instant expiryDate;
    @CreatedDate
    @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private Instant createdAt;
}
