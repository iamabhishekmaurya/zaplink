package io.zaplink.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Login response DTO containing JWT tokens and user information.
 * Uses modern Java 17 features with SuperBuilder pattern and records.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false)
public class LoginResponse
    extends
    BaseResponse
{
    private String       accessToken;
    private String       refreshToken;
    private final String tokenType = "Bearer";
    private Long         expiresIn;
    private UserInfo     userInfo;
    /**
     * User information record using modern Java 17 record pattern.
     * Immutable data carrier for user details.
     */
    @Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false)
    public static class UserInfo
    {
        private Long    id;
        private String  username;
        private String  email;
        private String  firstName;
        private String  lastName;
        private boolean verified;
    }
}
