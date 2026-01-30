package io.zaplink.social.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.social.entity.SocialAccount;
import io.zaplink.social.service.SocialService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/social") @RequiredArgsConstructor
public class SocialController
{
    private final SocialService socialService;
    @PostMapping("/connect")
    public ResponseEntity<SocialAccount> connectAccount( @RequestParam String provider,
                                                         @RequestParam String code,
                                                         @RequestHeader("X-User-Id") UUID userId )
    {
        // Assuming GateWay passes user ID
        return ResponseEntity.ok( socialService.connectAccount( provider, code, userId ) );
    }

    @PostMapping("/publish")
    public ResponseEntity<Void> publishPost( @RequestBody PublishRequest request )
    {
        socialService.publishPost( request.accountId(), request.caption(), request.mediaUrl() );
        return ResponseEntity.ok().build();
    }
    public record PublishRequest( UUID accountId, String caption, String mediaUrl )
    {
    }
}
