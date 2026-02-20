# Content Analysis

## Frontend (`zaplink-ui`) Legacy Code
Legacy code elements related to the outdated media and folder structures to be replaced:
1. `src/features/media`: Whole directory containing outdated media management components.
2. `src/app/dashboard/media`: Outdated route for the media dashboard.
3. Any existing API hooks or queries inside `src/features/media` for `Folder` and `Media` operations.

## Backend (`zaplink-media-service`) Legacy Code
Outdated backend endpoints, entities, and services that don't match the new feature set requirements:
1. **Controllers**: 
   - `src/main/java/io/zaplink/media/controller/FolderController.java`
   - `src/main/java/io/zaplink/media/controller/MediaController.java`
2. **Services**:
   - `src/main/java/io/zaplink/media/service/FolderService.java`
   - `src/main/java/io/zaplink/media/service/MediaService.java`
   - `src/main/java/io/zaplink/media/service/MinioStorageService.java`
   - `src/main/java/io/zaplink/media/service/StorageService.java`
3. **Entities**:
   - `src/main/java/io/zaplink/media/entity/Folder.java`
   - `src/main/java/io/zaplink/media/entity/Asset.java`
   - `src/main/java/io/zaplink/media/entity/AssetTag.java`

These files will be either significantly modified or completely replaced to support the new `Folder`, `MediaItem`, and `ActivityLog` schema, along with Advanced Features (Trash, Favorites, Sharing, Versioning).
