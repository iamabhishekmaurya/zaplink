# Advanced Folder & Content Management System

## Overview
The Advanced Folder & Content Management System replaces the legacy media management with a robust, hierarchical folder structure that supports nested folders, advanced media metdata, tagging, favorites, sharing (future RBAC), and soft-deletes (Trash). The system aligns visually with the modern Zaplink portal theme.

## Architecture

### Backend (`zaplink-media-service`)
- **Entities**: 
  - `Folder`: Hierarchical folder structure with `parent_id`.
  - `MediaItem`: Represents files (images, videos, docs) stored in S3/MinIO, linking to a `Folder`.
  - `ActivityLog`: Tracks actions (upload, rename, delete) for audit purposes.
- **REST APIs (`ContentManagementController`)**:
  - `POST /api/content/folders` - Create folder
  - `GET /api/content/folders` - List folders (optionally by parentId)
  - `PUT /api/content/folders/{id}/rename`, `.../move`
  - `DELETE /api/content/folders/{id}` - Soft delete to trash.
  - `POST /api/content/folders/{id}/restore`, `.../favorite`
  - Similar methods for `MediaItem` operations.
- **Storage**: Uses `MinioStorageService` for seamless S3 object storage integration.

### Frontend (`zaplink-ui`)
- **Routes**: `/dashboard/content` serves as the core dashboard.
- **Components**: 
  - `ContentManager`: Main dashboard wrapper providing Tree view (Folders) and Grid/List views (Media).
- **Service Integration**: Managed through `ContentApi` wrapper standardizing REST calls to the backend via Axios.
- **UI/UX**: Features glassmorphism, responsive grid layouts, and smooth filtering capabilities using Lucide icons.

## Usage
Users can access Content Management from the primary Sidebar. They can structure their media library similarly to modern OS file managers, star important assets, and safely delete/restore content.

## Future Extensibility
- RBAC permissions logic per `Collaborator` record on `Folder` and `MediaItem`.
- Batch operations (multi-select drag-and-drop).
- AI auto-tagging integration.
