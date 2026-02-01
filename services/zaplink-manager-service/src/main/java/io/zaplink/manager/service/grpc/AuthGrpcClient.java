package io.zaplink.manager.service.grpc;

import io.grpc.StatusRuntimeException;
import io.zaplink.auth.grpc.AuthServiceGrpc;
import io.zaplink.auth.grpc.AuthServiceProto;
import io.zaplink.manager.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthGrpcClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;

    public UserDto getUserByEmail(String email) {
        try {
            log.info("Calling Auth service via gRPC to get user by email: {}", email);
            
            AuthServiceProto.GetUserByEmailRequest request = AuthServiceProto.GetUserByEmailRequest.newBuilder()
                    .setEmail(email)
                    .build();

            AuthServiceProto.GetUserByEmailResponse response = authServiceStub.getUserByEmail(request);
            
            UserDto user = mapToUserDto(response.getUser());
            log.info("Retrieved user from Auth service: {}", user.email());
            return user;
            
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for getUserByEmail: {}", e.getStatus());
            throw new RuntimeException("Failed to fetch user from Auth service", e);
        }
    }

    public UserDto getUserById(String userId) {
        try {
            log.info("Calling Auth service via gRPC to get user by ID: {}", userId);
            
            AuthServiceProto.GetUserByIdRequest request = AuthServiceProto.GetUserByIdRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            AuthServiceProto.GetUserByIdResponse response = authServiceStub.getUserById(request);
            
            UserDto user = mapToUserDto(response.getUser());
            log.info("Retrieved user from Auth service: {}", user.email());
            return user;
            
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for getUserById: {}", e.getStatus());
            throw new RuntimeException("Failed to fetch user from Auth service", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            log.info("Calling Auth service via gRPC to validate token");
            
            AuthServiceProto.ValidateTokenRequest request = AuthServiceProto.ValidateTokenRequest.newBuilder()
                    .setToken(token)
                    .build();

            AuthServiceProto.ValidateTokenResponse response = authServiceStub.validateToken(request);
            
            boolean isValid = response.getValid();
            log.info("Token validation result: {}", isValid);
            return isValid;
            
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for validateToken: {}", e.getStatus());
            return false;
        }
    }

    private UserDto mapToUserDto(AuthServiceProto.UserData userData) {
        return new UserDto(
            Long.parseLong(userData.getId()),
            userData.getEmail(),
            userData.getUsername(),
            userData.getFirstName(),
            userData.getLastName(),
            java.time.LocalDateTime.parse(userData.getCreatedAt()),
            java.time.LocalDateTime.parse(userData.getUpdatedAt())
        );
    }
}
