package io.zaplink.auth.dto.request;

import io.zaplink.auth.common.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest
{
    @NotBlank(message = ValidationConstants.VALIDATION_EMAIL_REQUIRED) @Email(message = ValidationConstants.VALIDATION_EMAIL_VALID)
    private String  email;
    @NotBlank(message = ValidationConstants.VALIDATION_PASSWORD_REQUIRED)
    private String  password;
    private Boolean rememberMe;
}
