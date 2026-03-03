export interface Folder {
    id: string;
    name: string;
    parent?: { id: string; name: string };
    ownerId: string;
    isFavorite: boolean;
    isDeleted: boolean;
    createdAt: string;
    updatedAt: string;
}

export interface MediaItem {
    id: string;
    name: string;
    originalFilename: string;
    url: string;
    type: string;
    size: number;
    folderId?: string;
    ownerId: string;
    tags: string[];
    isFavorite: boolean;
    isDeleted: boolean;
    createdAt: string;
    updatedAt: string;
}

export type ViewMode = 'grid' | 'list';
export type ViewTab = 'all' | 'favorites' | 'trash';
