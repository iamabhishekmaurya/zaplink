package io.zaplink.auth.entity;

import io.zaplink.auth.common.constants.DatabaseConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role entity representing user roles in the system.
 * Uses modern Java 17 features with builder pattern and Lombok annotations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Entity
@Table(name = DatabaseConstants.TABLE_ROLES)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;
    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50)
    private String name;
    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION)
    @Size(max = 200)
    private String description;
}
