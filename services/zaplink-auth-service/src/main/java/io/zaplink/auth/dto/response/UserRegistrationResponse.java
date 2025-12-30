package io.zaplink.auth.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * User registration response DTO containing created user information.
 * Uses modern Java 17 features with builder pattern and modern time API.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false)
public class UserRegistrationResponse
    extends
    BaseResponse
{
    private Long    userId;
    private String  username;
    private String  email;
    private String  firstName;
    private String  lastName;
    private boolean verified;
    private Instant createdAt;
}
