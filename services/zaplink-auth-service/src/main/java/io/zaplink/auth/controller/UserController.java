package io.zaplink.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.constants.StatusConstants;
import io.zaplink.auth.dto.response.BaseResponse;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController @RequiredArgsConstructor @RequestMapping("/users") @Tag(name = ApiConstants.TAG_USER_MANAGEMENT, description = ApiConstants.TAG_USER_MANAGEMENT_DESC)
public class UserController
{
    private final UserService userService;
    @GetMapping("/{userId}") @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id") @Operation(summary = ApiConstants.USER_GET_SUMMARY, description = ApiConstants.USER_GET_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_USER_RETRIEVED, content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ApiConstants.RESPONSE_403_ACCESS_DENIED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ApiConstants.RESPONSE_404_USER_NOT_FOUND) })
    public ResponseEntity<User> getUser( @Parameter(description = ApiConstants.PARAM_USER_ID) @PathVariable("userId") @P("userId") Long userId )
    {
        log.info( LogConstants.LOG_GET_USER_REQUEST, userId );
        User user = userService.findById( userId )
                .orElseThrow( () -> new RuntimeException( ApiConstants.MESSAGE_USER_NOT_FOUND ) );
        return ResponseEntity.ok( user );
    }

    @PutMapping("/{userId}") @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id") @Operation(summary = ApiConstants.USER_UPDATE_SUMMARY, description = ApiConstants.USER_UPDATE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_USER_UPDATED, content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ApiConstants.RESPONSE_400_INVALID_INPUT),
      @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ApiConstants.RESPONSE_403_ACCESS_DENIED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ApiConstants.RESPONSE_404_USER_NOT_FOUND) })
    public ResponseEntity<BaseResponse> updateUser( @Parameter(description = ApiConstants.PARAM_USER_ID) @PathVariable("userId") @P("userId") Long userId,
                                                    @RequestBody User updatedUser )
    {
        log.info( LogConstants.LOG_UPDATE_USER_REQUEST, userId );
        userService.updateUser( userId, updatedUser );
        return ResponseEntity.ok( BaseResponse.success( ApiConstants.MESSAGE_USER_UPDATED_SUCCESSFULLY ) );
    }

    @PutMapping("/{userId}/deactivate") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = ApiConstants.USER_DEACTIVATE_SUMMARY, description = ApiConstants.USER_DEACTIVATE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_USER_DEACTIVATED, content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ApiConstants.RESPONSE_403_ACCESS_DENIED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ApiConstants.RESPONSE_404_USER_NOT_FOUND) })
    public ResponseEntity<BaseResponse> deactivateUser( @Parameter(description = ApiConstants.PARAM_USER_ID) @PathVariable("userId") Long userId )
    {
        log.info( LogConstants.LOG_DEACTIVATE_USER_REQUEST, userId );
        userService.deactivateUser( userId );
        return ResponseEntity.ok( BaseResponse.success( ApiConstants.MESSAGE_USER_DEACTIVATED_SUCCESSFULLY ) );
    }

    @PutMapping("/{userId}/activate") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = ApiConstants.USER_ACTIVATE_SUMMARY, description = ApiConstants.USER_ACTIVATE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ApiConstants.RESPONSE_200_USER_ACTIVATED, content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ApiConstants.RESPONSE_403_ACCESS_DENIED),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ApiConstants.RESPONSE_404_USER_NOT_FOUND) })
    public ResponseEntity<BaseResponse> activateUser( @Parameter(description = ApiConstants.PARAM_USER_ID) @PathVariable("userId") Long userId )
    {
        log.info( LogConstants.LOG_ACTIVATE_USER_REQUEST, userId );
        userService.activateUser( userId );
        return ResponseEntity.ok( BaseResponse.success( ApiConstants.MESSAGE_USER_ACTIVATED_SUCCESSFULLY ) );
    }
}
