package io.zaplink.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.zaplink.auth.common.constants.DatabaseConstants;

import java.time.LocalDateTime;

@Data @Entity @Table(name = DatabaseConstants.TABLE_LOGIN_ATTEMPTS) @EntityListeners(AuditingEntityListener.class)
public class LoginAttempt
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    @Column(nullable = false)
    private String        email;
    @Column(name = DatabaseConstants.COLUMN_IP_ADDRESS)
    private String        ipAddress;
    @Column(name = DatabaseConstants.COLUMN_USER_AGENT)
    private String        userAgent;
    @Column(nullable = false)
    private boolean       successful;
    @Column(name = DatabaseConstants.COLUMN_FAILURE_REASON)
    private String        failureReason;
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private LocalDateTime createdAt;
    public LoginAttempt()
    {
    }

    public LoginAttempt( String email, boolean successful )
    {
        this.email = email;
        this.successful = successful;
    }
}
