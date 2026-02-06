package io.zaplink.media.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFolderRequest( @NotBlank(message = "Folder name is required") String name,
                                   UUID parentId,
                                   @NotNull(message = "Owner ID is required") String ownerId )
{
}
