import { useState, useCallback, useEffect, useMemo } from 'react';
import { toast } from 'sonner';
import { ContentApi } from '@/services/contentApi';
import { Folder, MediaItem, ViewMode, ViewTab } from '../types';

export function useContentManager() {
    const [folders, setFolders] = useState<Folder[]>([]);
    const [media, setMedia] = useState<MediaItem[]>([]);
    const [currentFolderId, setCurrentFolderId] = useState<string | undefined>(undefined);
    const [allTreeFolders, setAllTreeFolders] = useState<Folder[]>([]);
    const [allTreeMedia, setAllTreeMedia] = useState<MediaItem[]>([]);

    // UI State
    const [viewMode, setViewMode] = useState<ViewMode>('grid');
    const [activeTab, setActiveTab] = useState<ViewTab>('all');
    const [searchQuery, setSearchQuery] = useState('');
    const [isLoading, setIsLoading] = useState(true);

    const loadContent = useCallback(async () => {
        setIsLoading(true);
        const mapMediaUrls = (items: MediaItem[]) => items.map(m => ({
            ...m,
            url: m.url.startsWith('http') ? m.url : `http://localhost:9000/zaplink-media/${m.url}`
        }));

        try {
            if (activeTab === 'favorites') {
                const [fRes, mRes] = await Promise.all([
                    ContentApi.listFavoriteFolders(),
                    ContentApi.listFavoriteMedia()
                ]);
                setFolders(fRes.data || []);
                setMedia(mapMediaUrls(mRes.data || []));
            } else if (activeTab === 'trash') {
                const [fRes, mRes] = await Promise.all([
                    ContentApi.listTrashFolders(),
                    ContentApi.listTrashMedia()
                ]);
                setFolders(fRes.data || []);
                setMedia(mapMediaUrls(mRes.data || []));
            } else {
                // Normal view
                // Fetch current folder contents
                const [fRes, mRes] = await Promise.all([
                    ContentApi.listFolders(currentFolderId),
                    ContentApi.listMedia(currentFolderId)
                ]);
                const loadedFolders = fRes.data || [];
                setFolders(loadedFolders);
                setMedia(mapMediaUrls(mRes.data || []));
            }

            // Also fetch the entire folder tree for the sidebar recursively
            try {
                const [allFRes, allMRes] = await Promise.all([
                    ContentApi.listAllFolders(),
                    ContentApi.listAllMedia()
                ]);
                setAllTreeFolders(allFRes.data || []);
                setAllTreeMedia(mapMediaUrls(allMRes.data || []));
            } catch (e) {
                console.error("Failed to load all folder tree", e);
            }
        } catch (error) {
            console.error("Failed to load content", error);
            toast.error("Failed to load content. Please try again.");
        } finally {
            setIsLoading(false);
        }
    }, [currentFolderId, activeTab]);

    // Initial Load & subsequent changes
    useEffect(() => {
        loadContent();
    }, [loadContent]);

    // Derived `folderPath` calculated directly from the tree based on currentFolderId
    const folderPath = useMemo(() => {
        if (!currentFolderId || allTreeFolders.length === 0) return [];

        const path: { id: string, name: string }[] = [];
        let currId: string | undefined = currentFolderId;

        // Traverse upwards to root
        while (currId) {
            const folder = allTreeFolders.find(f => f.id === currId);
            if (folder) {
                path.unshift({ id: folder.id, name: folder.name });
                // The backend returns a parent object, not just a parentId string
                currId = (folder as any).parent?.id;
            } else {
                break;
            }
        }
        return path;
    }, [currentFolderId, allTreeFolders]);

    // Derived state for searching
    const filteredFolders = folders.filter(f => f.name.toLowerCase().includes(searchQuery.toLowerCase()));
    const filteredMedia = media.filter(m => m.name.toLowerCase().includes(searchQuery.toLowerCase()));

    const handleNavigate = (folderId: string | undefined, folderName?: string) => {
        if (activeTab !== 'all') {
            setActiveTab('all'); // Reset to all when navigating from special views
        }

        setCurrentFolderId(folderId);
        setSearchQuery(""); // Clear search on navigation
    };

    return {
        folders: filteredFolders,
        allTreeFolders,
        allTreeMedia,
        media: filteredMedia,
        currentFolderId,
        folderPath,
        viewMode,
        activeTab,
        searchQuery,
        isLoading,

        // Setters
        setViewMode,
        setActiveTab,
        setSearchQuery,
        handleNavigate,
        reload: loadContent
    };
}
