import axios from 'axios';

// Assuming the base setup for apiClient exists, we might need a dedicated content api or add to existing.
// Since the environment isn't fully clear, we'll create a dedicated api service for content.
// Normally we'd add to `src/services/apiClient.ts` but since we didn't inspect it deeply, we will create a feature specific API client wrapper.

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api';

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
    config.headers['X-API-Version'] = '1';
    return config;
});

export const ContentApi = {
    // Folders
    createFolder: (name: string, parentId?: string) =>
        apiClient.post(`/folders?name=${encodeURIComponent(name)}${parentId ? `&parentId=${parentId}` : ''}`),

    listFolders: (parentId?: string) =>
        apiClient.get(`/folders${parentId ? `?parentId=${parentId}` : ''}`),

    listAllFolders: () =>
        apiClient.get('/folders/all'),

    renameFolder: (id: string, newName: string) =>
        apiClient.put(`/folders/${id}/rename?newName=${encodeURIComponent(newName)}`),

    moveFolder: (id: string, newParentId?: string) =>
        apiClient.put(`/folders/${id}/move${newParentId ? `?newParentId=${newParentId}` : ''}`),

    deleteFolder: (id: string, permanent?: boolean) =>
        apiClient.delete(`/folders/${id}${permanent ? '/hard' : ''}`),

    restoreFolder: (id: string) =>
        apiClient.post(`/folders/${id}/restore`),

    toggleFolderFavorite: (id: string) =>
        apiClient.post(`/folders/${id}/favorite`),

    // Media
    uploadMedia: (file: File, folderId?: string, tags?: string[]) => {
        const formData = new FormData();
        formData.append('file', file);
        if (folderId) formData.append('folderId', folderId);
        if (tags && tags.length > 0) formData.append('tags', tags.join(','));
        return apiClient.post('/medias', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    listMedia: (folderId?: string) =>
        apiClient.get(`/medias${folderId ? `?folderId=${folderId}` : ''}`),

    listAllMedia: () =>
        apiClient.get('/medias/all'),

    updateMediaMetadata: (id: string, newName?: string, newTags?: string[]) => {
        const params = new URLSearchParams();
        if (newName) params.append('newName', newName);
        if (newTags && newTags.length > 0) params.append('newTags', newTags.join(','));
        const qs = params.toString();
        return apiClient.put(`/medias/${id}/metadata${qs ? `?${qs}` : ''}`);
    },

    moveMedia: (id: string, newFolderId?: string) =>
        apiClient.put(`/medias/${id}/move${newFolderId ? `?newFolderId=${newFolderId}` : ''}`),

    deleteMedia: (id: string, permanent?: boolean) =>
        apiClient.delete(`/medias/${id}${permanent ? '/hard' : ''}`),

    restoreMedia: (id: string) =>
        apiClient.post(`/medias/${id}/restore`),

    toggleMediaFavorite: (id: string) =>
        apiClient.post(`/medias/${id}/favorite`),

    // Views
    listFavoriteFolders: () => apiClient.get('/folders/favorites'),
    listFavoriteMedia: () => apiClient.get('/medias/favorites'),
    listTrashFolders: () => apiClient.get('/folders/trash'),
    listTrashMedia: () => apiClient.get('/medias/trash'),
};
