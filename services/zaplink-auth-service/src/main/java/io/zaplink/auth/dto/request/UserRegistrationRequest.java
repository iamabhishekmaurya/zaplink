package io.zaplink.auth.dto.request;

import io.zaplink.auth.common.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest
{
    @NotBlank(message = ValidationConstants.VALIDATION_USERNAME_REQUIRED) @Size(min = ValidationConstants.USERNAME_MIN_LENGTH, max = ValidationConstants.USERNAME_MAX_LENGTH, message = ValidationConstants.VALIDATION_USERNAME_SIZE)
    private String username;
    @NotBlank(message = ValidationConstants.VALIDATION_EMAIL_REQUIRED) @Email(message = ValidationConstants.VALIDATION_EMAIL_VALID)
    private String email;
    @NotBlank(message = ValidationConstants.VALIDATION_PASSWORD_REQUIRED) @Size(min = ValidationConstants.PASSWORD_MIN_LENGTH, max = ValidationConstants.PASSWORD_MAX_LENGTH, message = ValidationConstants.VALIDATION_PASSWORD_SIZE)
    private String password;
    @Size(max = ValidationConstants.FIRST_NAME_MAX_LENGTH, message = ValidationConstants.VALIDATION_FIRST_NAME_SIZE)
    private String firstName;
    @Size(max = ValidationConstants.LAST_NAME_MAX_LENGTH, message = ValidationConstants.VALIDATION_LAST_NAME_SIZE)
    private String lastName;
    @Size(max = ValidationConstants.PHONE_NUMBER_MAX_LENGTH, message = ValidationConstants.VALIDATION_PHONE_NUMBER_SIZE)
    private String phoneNumber;
}
