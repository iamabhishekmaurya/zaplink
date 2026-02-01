import api from "@/services/client";
import { API_BASE_URLS } from '@/lib/constants/apiConstant';

// --- Interfaces ---

export interface MediaAsset {
    id: string;
    url: string;
    type: 'image' | 'video';
    mimeType?: string;
    thumbnail?: string; // specific for videos
    width?: number;     // for aspect ratio check
    height?: number;
}

export interface SocialPlatformConfig {
    platform: 'instagram' | 'linkedin' | 'twitter' | 'facebook' | 'tiktok';
    enabled: boolean;
}

export interface ScheduledPost {
    id: string;
    mediaUrl: string;
    mediaType: 'image' | 'video';
    caption: string;
    scheduledAt: string; // ISO string for backend
    platforms: ('instagram' | 'linkedin' | 'twitter' | 'facebook' | 'tiktok')[];
    status: 'SCHEDULED' | 'PUBLISHED' | 'FAILED' | 'DRAFT';
    createdAt?: string;
    updatedAt?: string;
}

export interface CreatePostRequest {
    mediaUrl: string;
    mediaType: 'image' | 'video';
    caption: string;
    scheduledAt: string;
    platforms: string[];
    status?: 'SCHEDULED' | 'DRAFT';
}

export interface UpdatePostRequest {
    caption?: string;
    scheduledAt?: string;
    platforms?: string[];
    status?: 'SCHEDULED' | 'DRAFT';
}

// --- API Service ---

export const schedulerApi = {

    // 1. Fetch Media from Media Service
    getMediaItems: async (): Promise<MediaAsset[]> => {
        // Gateway Route: /api/media
        const response = await api.get('/api/media');
        const assets = response.data?.content || response.data || [];

        // Map Asset response to MediaAsset interface
        return assets.map((asset: {
            id: string;
            url: string;
            mimeType?: string;
            thumbnailPath?: string;
        }) => ({
            id: asset.id,
            url: asset.url,
            type: asset.mimeType?.startsWith('video/') ? 'video' as const : 'image' as const,
            mimeType: asset.mimeType,
            thumbnail: asset.thumbnailPath,
        }));
    },

    // 2. Fetch Scheduled Posts
    getScheduledPosts: async (startDate: string, endDate: string): Promise<ScheduledPost[]> => {
        // Gateway Route: /api/schedule
        const response = await api.get('/api/schedule', {
            params: { start: startDate, end: endDate }
        });
        return response.data;
    },

    // 3. Create Post
    createPost: async (data: CreatePostRequest): Promise<ScheduledPost> => {
        const response = await api.post('/api/schedule', data);
        return response.data;
    },

    // 4. Update Post (Reschedule only supported now via Drag)
    updatePost: async (id: string, data: UpdatePostRequest): Promise<ScheduledPost> => {
        // Backend currently only supports updating time via /drag endpoint
        if (data.scheduledAt) {
            const response = await api.patch(`/api/schedule/${id}/drag`, { newTime: data.scheduledAt });
            return response.data;
        }
        throw new Error("Only rescheduling is supported by the backend currently.");
    },

    // 5. Delete Post
    deletePost: async (id: string): Promise<void> => {
        await api.delete(`/schedule/${id}`);
    }

};
