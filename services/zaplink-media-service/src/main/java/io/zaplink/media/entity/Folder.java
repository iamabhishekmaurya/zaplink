package io.zaplink.media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "folders")
public class Folder
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID   id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_id")
    private Folder parent;
    @Column(name = "owner_id", nullable = false)
    private UUID   ownerId;
}
