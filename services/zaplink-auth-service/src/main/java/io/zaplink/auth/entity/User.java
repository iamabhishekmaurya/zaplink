package io.zaplink.auth.entity;

import io.zaplink.auth.common.constants.DatabaseConstants;
import io.zaplink.auth.common.constants.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Set;

/**
 * User entity representing application users.
 * Uses modern Java 17 features including records pattern and builder pattern.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-30
 */
@Entity
@Table(name = DatabaseConstants.TABLE_USERS, uniqueConstraints =
{ @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_EMAIL),
  @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_USERNAME) })
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long      id;
    @Column(name = DatabaseConstants.COLUMN_USERNAME, nullable = false)
    @Size(min = ValidationConstants.USERNAME_MIN_LENGTH, max = ValidationConstants.USERNAME_MAX_LENGTH)
    private String    username;
    @Column(name = DatabaseConstants.COLUMN_EMAIL, nullable = false)
    @Email
    private String    email;
    @Column(name = DatabaseConstants.COLUMN_PASSWORD, nullable = false)
    @Size(min = ValidationConstants.PASSWORD_MIN_LENGTH, max = ValidationConstants.PASSWORD_MAX_LENGTH)
    private String    password;
    @Column(name = DatabaseConstants.COLUMN_FIRST_NAME)
    @Size(max = ValidationConstants.FIRST_NAME_MAX_LENGTH)
    private String    firstName;
    @Column(name = DatabaseConstants.COLUMN_LAST_NAME)
    @Size(max = ValidationConstants.LAST_NAME_MAX_LENGTH)
    private String    lastName;
    @Column(name = DatabaseConstants.COLUMN_PHONE_NUMBER)
    @Size(max = ValidationConstants.PHONE_NUMBER_MAX_LENGTH)
    private String    phoneNumber;
    @Column(name = DatabaseConstants.COLUMN_IS_ACTIVE)
    @Builder.Default
    private Boolean   active   = true;
    @Column(name = DatabaseConstants.COLUMN_IS_VERIFIED)
    @Builder.Default
    private Boolean   verified = false;
    @Column(name = DatabaseConstants.COLUMN_VERIFICATION_TOKEN)
    private String    verificationToken;
    @Column(name = DatabaseConstants.COLUMN_RESET_TOKEN)
    private String    resetToken;
    @Column(name = DatabaseConstants.COLUMN_RESET_TOKEN_EXPIRY)
    private Instant   resetTokenExpiry;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DatabaseConstants.TABLE_USER_ROLES, joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID), inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ROLE_ID))
    @Builder.Default
    private Set<Role> roles    = Set.of();
    @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    @CreatedDate
    private Instant   createdAt;
    @Column(name = DatabaseConstants.COLUMN_UPDATED_AT)
    @LastModifiedDate
    private Instant   updatedAt;
    // Custom getters for boolean fields to follow Java conventions
    public boolean isActive()
    {
        return active;
    }

    public boolean isVerified()
    {
        return verified;
    }
}
