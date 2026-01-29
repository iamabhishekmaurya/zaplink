package io.zaplink.media.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFolderRequest
{
    @NotBlank(message = "Folder name is required")
    private String name;
    private UUID   parentId;
    @NotNull(message = "Owner ID is required")
    private String ownerId;
}
