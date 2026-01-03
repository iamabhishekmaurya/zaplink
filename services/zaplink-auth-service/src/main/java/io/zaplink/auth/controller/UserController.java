package io.zaplink.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.dto.response.BaseResponse;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController @RequestMapping("${api.base-path}/users") @RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    @GetMapping("/{userId}") //@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<User> getUser( @PathVariable("userId") Long userId )
    {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        log.info( "DEBUG: Controller: param userId value: {}, type: {}", userId, userId.getClass().getName() );
        if ( principal instanceof io.zaplink.auth.common.security.CustomUserDetails )
        {
            Long authId = ( (io.zaplink.auth.common.security.CustomUserDetails) principal ).getId();
            log.info( "DEBUG: Controller: auth principal id value: {}, type: {}", authId, authId.getClass().getName() );
            log.info( "DEBUG: Controller: Equality check (==): {}", userId == authId );
            log.info( "DEBUG: Controller: Equality check (.equals): {}", userId.equals( authId ) );
        }
        else
        {
            log.info( "DEBUG: Controller: Principal is not CustomUserDetails: {}", principal.getClass().getName() );
        }
        log.info( LogConstants.LOG_GET_USER_REQUEST, userId );
        User user = userService.findById( userId ).orElseThrow( () -> new RuntimeException( "User not found" ) );
        return ResponseEntity.ok( user );
    }

    @PutMapping("/{userId}") //@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<BaseResponse> updateUser( @PathVariable("userId") Long userId, @RequestBody User updatedUser )
    {
        log.info( LogConstants.LOG_UPDATE_USER_REQUEST, userId );
        userService.updateUser( userId, updatedUser );
        return ResponseEntity.ok( BaseResponse.success( ApiConstants.MESSAGE_USER_UPDATED_SUCCESSFULLY ) );
    }

    @PutMapping("/{userId}/deactivate") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> deactivateUser( @PathVariable("userId") Long userId )
    {
        log.info( LogConstants.LOG_DEACTIVATE_USER_REQUEST, userId );
        userService.deactivateUser( userId );
        return ResponseEntity.ok( BaseResponse.success( ApiConstants.MESSAGE_USER_DEACTIVATED_SUCCESSFULLY ) );
    }

    @PutMapping("/{userId}/activate") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> activateUser( @PathVariable("userId") Long userId )
    {
        log.info( LogConstants.LOG_ACTIVATE_USER_REQUEST, userId );
        userService.activateUser( userId );
        return ResponseEntity.ok( BaseResponse.success( ApiConstants.MESSAGE_USER_ACTIVATED_SUCCESSFULLY ) );
    }
}
