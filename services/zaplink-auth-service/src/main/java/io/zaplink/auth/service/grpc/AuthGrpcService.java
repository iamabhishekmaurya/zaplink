package io.zaplink.auth.service.grpc;

import io.grpc.stub.StreamObserver;
import io.zaplink.auth.grpc.AuthServiceGrpc;
import io.zaplink.auth.grpc.AuthServiceProto;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.common.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void getUserByEmail(AuthServiceProto.GetUserByEmailRequest request,
                              StreamObserver<AuthServiceProto.GetUserByEmailResponse> responseObserver) {
        try {
            log.info("gRPC: Getting user by email: {}", request.getEmail());
            
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                AuthServiceProto.UserData userData = mapToUserData(user);
                
                AuthServiceProto.GetUserByEmailResponse response = AuthServiceProto.GetUserByEmailResponse.newBuilder()
                        .setUser(userData)
                        .build();
                
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                
                log.info("gRPC: Retrieved user: {}", user.getEmail());
            } else {
                responseObserver.onError(new RuntimeException("User not found: " + request.getEmail()));
            }
            
        } catch (Exception e) {
            log.error("gRPC: Error getting user by email: {}", request.getEmail(), e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getUserById(AuthServiceProto.GetUserByIdRequest request,
                           StreamObserver<AuthServiceProto.GetUserByIdResponse> responseObserver) {
        try {
            log.info("gRPC: Getting user by ID: {}", request.getUserId());
            
            User user = userRepository.findById(Long.parseLong(request.getUserId()))
                    .orElseThrow(() -> new RuntimeException("User not found: " + request.getUserId()));
            
            AuthServiceProto.UserData userData = mapToUserData(user);
            
            AuthServiceProto.GetUserByIdResponse response = AuthServiceProto.GetUserByIdResponse.newBuilder()
                    .setUser(userData)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC: Retrieved user: {}", user.getEmail());
            
        } catch (Exception e) {
            log.error("gRPC: Error getting user by ID: {}", request.getUserId(), e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void validateToken(AuthServiceProto.ValidateTokenRequest request,
                             StreamObserver<AuthServiceProto.ValidateTokenResponse> responseObserver) {
        try {
            log.info("gRPC: Validating token");
            
            // For now, we'll implement a simple token validation
            // In a real implementation, you would validate the JWT token
            String token = request.getToken();
            boolean isValid = token != null && !token.isEmpty();
            
            AuthServiceProto.ValidateTokenResponse.Builder responseBuilder = AuthServiceProto.ValidateTokenResponse.newBuilder()
                    .setValid(isValid);
            
            // For now, we won't extract user from token
            // This would require proper JWT parsing implementation
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            
            log.info("gRPC: Token validation result: {}", isValid);
            
        } catch (Exception e) {
            log.error("gRPC: Error validating token", e);
            responseObserver.onError(e);
        }
    }

    private AuthServiceProto.UserData mapToUserData(User user) {
        return AuthServiceProto.UserData.newBuilder()
                .setId(user.getId().toString())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setFirstName(user.getFirstName() != null ? user.getFirstName() : "")
                .setLastName(user.getLastName() != null ? user.getLastName() : "")
                .setRole("USER") // Simplified role since entity uses Set<Role>
                .setOrgId("") // User entity doesn't have orgId field
                .setActive(user.isActive())
                .setCreatedAt(user.getCreatedAt().toString()) // Instant to string conversion
                .setUpdatedAt(user.getUpdatedAt().toString()) // Instant to string conversion
                .build();
    }
}
