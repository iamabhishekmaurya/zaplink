import { v4 as uuidv4 } from 'uuid';

export interface MediaAsset {
    id: string;
    url: string;
    type: 'image' | 'video';
    thumbnail?: string; // For videos
}

export interface ScheduledPost {
    id: string;
    mediaUrl: string;
    mediaType: 'image' | 'video';
    caption: string;
    scheduledAt: Date; // ISO string
    platforms: ('instagram' | 'linkedin' | 'twitter' | 'facebook')[];
    status: 'scheduled' | 'published' | 'failed';
}

// Mock Data
const MOCK_MEDIA: MediaAsset[] = [
    { id: '1', type: 'image', url: 'https://images.unsplash.com/photo-1706606362402-9a0077cd1b14?q=80&w=600&auto=format&fit=crop' },
    { id: '2', type: 'image', url: 'https://images.unsplash.com/photo-1706548177423-289529329062?q=80&w=600&auto=format&fit=crop' },
    { id: '3', type: 'image', url: 'https://images.unsplash.com/photo-1682687982501-1e58ab814714?q=80&w=600&auto=format&fit=crop' },
    { id: '4', type: 'image', url: 'https://images.unsplash.com/photo-1706463629335-d92264bbfd6f?q=80&w=600&auto=format&fit=crop' },
    { id: '5', type: 'image', url: 'https://images.unsplash.com/photo-1682686581854-5e71f58e7e3f?q=80&w=600&auto=format&fit=crop' },
    { id: '6', type: 'image', url: 'https://plus.unsplash.com/premium_photo-1673264933212-d788d743264d?q=80&w=600&auto=format&fit=crop' },
    { id: '7', type: 'image', url: 'https://images.unsplash.com/photo-1706067757962-d045d61f1810?q=80&w=600&auto=format&fit=crop' },
    { id: '8', type: 'image', url: 'https://images.unsplash.com/photo-1705645524673-455b932d432b?q=80&w=600&auto=format&fit=crop' },
];

let MOCK_POSTS: ScheduledPost[] = [
    {
        id: 'post-1',
        mediaUrl: 'https://images.unsplash.com/photo-1706606362402-9a0077cd1b14?q=80&w=600&auto=format&fit=crop',
        mediaType: 'image',
        caption: 'Loving this view! 🌄 #nature #sunset',
        scheduledAt: new Date(new Date().setHours(10, 0, 0, 0)), // Today at 10 AM
        platforms: ['instagram', 'twitter'],
        status: 'scheduled',
    },
    {
        id: 'post-2',
        mediaUrl: 'https://images.unsplash.com/photo-1706548177423-289529329062?q=80&w=600&auto=format&fit=crop',
        mediaType: 'image',
        caption: 'New product drop coming soon... 👀',
        scheduledAt: new Date(new Date().setDate(new Date().getDate() + 2)), // 2 days from now
        platforms: ['linkedin'],
        status: 'scheduled',
    }
];

// Helper to simulate network delay
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const schedulerApi = {
    fetchMedia: async (): Promise<MediaAsset[]> => {
        await delay(500);
        return [...MOCK_MEDIA];
    },

    fetchScheduledPosts: async (startDate: Date, endDate: Date): Promise<ScheduledPost[]> => {
        await delay(500);
        // In a real app, we'd filter by date range. For now return all.
        return [...MOCK_POSTS];
    },

    createPost: async (post: Omit<ScheduledPost, 'id' | 'status'>): Promise<ScheduledPost> => {
        await delay(800);
        const newPost: ScheduledPost = {
            ...post,
            id: uuidv4(),
            status: 'scheduled',
        };
        MOCK_POSTS.push(newPost);
        return newPost;
    },

    updatePost: async (id: string, updates: Partial<ScheduledPost>): Promise<ScheduledPost> => {
        await delay(500);
        const index = MOCK_POSTS.findIndex(p => p.id === id);
        if (index === -1) throw new Error('Post not found');

        MOCK_POSTS[index] = { ...MOCK_POSTS[index], ...updates };
        return MOCK_POSTS[index];
    },

    deletePost: async (id: string): Promise<void> => {
        await delay(500);
        MOCK_POSTS = MOCK_POSTS.filter(p => p.id !== id);
    }
};
