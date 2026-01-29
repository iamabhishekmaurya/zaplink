package io.zaplink.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "asset_tags")
public class AssetTag
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID   id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "asset_id", nullable = false)
    private Asset  asset;
    @Column(nullable = false)
    private String tag;
}
