package io.zaplink.manager.service.grpc;

import io.grpc.StatusRuntimeException;
import io.zaplink.core.grpc.CoreServiceGrpc;
import io.zaplink.core.grpc.CoreServiceProto;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.common.enums.UrlStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoreGrpcClient {

    private final CoreServiceGrpc.CoreServiceBlockingStub coreServiceStub;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<LinkResponse> getUrlsByUser(String userEmail) {
        try {
            log.info("Calling Core service via gRPC to get URLs for user: {}", userEmail);
            
            CoreServiceProto.GetUrlByUserRequest request = CoreServiceProto.GetUrlByUserRequest.newBuilder()
                    .setUserEmail(userEmail)
                    .setPage(0)
                    .setSize(1000) // Get all URLs for now
                    .build();

            CoreServiceProto.GetUrlByUserResponse response = coreServiceStub.getUrlByUser(request);
            
            List<LinkResponse> links = response.getUrlsList().stream()
                    .map(this::mapToLinkResponse)
                    .collect(java.util.stream.Collectors.toList());
            
            log.info("Retrieved {} URLs from Core service for user: {}", links.size(), userEmail);
            return links;
            
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for getUrlsByUser: {}", e.getStatus());
            throw new RuntimeException("Failed to fetch URLs from Core service", e);
        }
    }

    public LinkResponse getUrlById(String urlId) {
        try {
            log.info("Calling Core service via gRPC to get URL by ID: {}", urlId);
            
            CoreServiceProto.GetUrlByIdRequest request = CoreServiceProto.GetUrlByIdRequest.newBuilder()
                    .setUrlId(urlId)
                    .build();

            CoreServiceProto.GetUrlByIdResponse response = coreServiceStub.getUrlById(request);
            
            LinkResponse link = mapToLinkResponse(response.getUrl());
            log.info("Retrieved URL from Core service: {}", link.shortUrl());
            return link;
            
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for getUrlById: {}", e.getStatus());
            throw new RuntimeException("Failed to fetch URL from Core service", e);
        }
    }

    private LinkResponse mapToLinkResponse(CoreServiceProto.UrlData urlData) {
        LocalDateTime createdAt = java.time.LocalDateTime.parse(urlData.getCreatedAt(), FORMATTER);
        return new LinkResponse(
            Long.parseLong(urlData.getId()),
            urlData.getShortUrl(), // Using shortUrl as shortUrlKey for now
            urlData.getOriginalUrl(),
            urlData.getShortUrl(),
            createdAt,
            urlData.getClickCount(),
            UrlStatusEnum.valueOf(urlData.getStatus()),
            Collections.emptyList(), // No rules for now
            Collections.emptyList()  // No tags for now
        );
    }
}
