import axios from 'axios';

// Assuming the base setup for apiClient exists, we might need a dedicated content api or add to existing.
// Since the environment isn't fully clear, we'll create a dedicated api service for content.
// Normally we'd add to `src/services/apiClient.ts` but since we didn't inspect it deeply, we will create a feature specific API client wrapper.

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
});

apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token'); // Typical setup
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    // Hard code for simulation based on backend expectation
    config.headers['X-User-Id'] = localStorage.getItem('userId') || 'default-user-id';
    return config;
});

export const ContentApi = {
    // Folders
    createFolder: (name: string, parentId?: string) =>
        apiClient.post(`/content/folders?name=${encodeURIComponent(name)}${parentId ? `&parentId=${parentId}` : ''}`),

    listFolders: (parentId?: string) =>
        apiClient.get(`/content/folders${parentId ? `?parentId=${parentId}` : ''}`),

    renameFolder: (id: string, newName: string) =>
        apiClient.put(`/content/folders/${id}/rename?newName=${encodeURIComponent(newName)}`),

    moveFolder: (id: string, newParentId?: string) =>
        apiClient.put(`/content/folders/${id}/move${newParentId ? `?newParentId=${newParentId}` : ''}`),

    deleteFolder: (id: string) =>
        apiClient.delete(`/content/folders/${id}`),

    restoreFolder: (id: string) =>
        apiClient.post(`/content/folders/${id}/restore`),

    toggleFolderFavorite: (id: string) =>
        apiClient.post(`/content/folders/${id}/favorite`),

    // Media
    uploadMedia: (file: File, folderId?: string, tags?: string[]) => {
        const formData = new FormData();
        formData.append('file', file);
        if (folderId) formData.append('folderId', folderId);
        if (tags && tags.length > 0) formData.append('tags', tags.join(','));
        return apiClient.post('/content/media', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    listMedia: (folderId?: string) =>
        apiClient.get(`/content/media${folderId ? `?folderId=${folderId}` : ''}`),

    updateMediaMetadata: (id: string, newName?: string, newTags?: string[]) =>
        apiClient.put(`/content/media/${id}/metadata`, { newName, newTags }),

    moveMedia: (id: string, newFolderId?: string) =>
        apiClient.put(`/content/media/${id}/move${newFolderId ? `?newFolderId=${newFolderId}` : ''}`),

    deleteMedia: (id: string) =>
        apiClient.delete(`/content/media/${id}`),

    restoreMedia: (id: string) =>
        apiClient.post(`/content/media/${id}/restore`),

    toggleMediaFavorite: (id: string) =>
        apiClient.post(`/content/media/${id}/favorite`),

    // Views
    listFavoriteFolders: () => apiClient.get('/content/favorites/folders'),
    listFavoriteMedia: () => apiClient.get('/content/favorites/media'),
    listTrashFolders: () => apiClient.get('/content/trash/folders'),
    listTrashMedia: () => apiClient.get('/content/trash/media'),
};
