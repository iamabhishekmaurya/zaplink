package io.zaplink.auth.dto.request;

import io.zaplink.auth.common.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest( @NotBlank(message = ValidationConstants.VALIDATION_EMAIL_REQUIRED) @Email(message = ValidationConstants.VALIDATION_EMAIL_VALID) String email,
                            @NotBlank(message = ValidationConstants.VALIDATION_PASSWORD_REQUIRED) String password,
                            Boolean rememberMe )
{
}
