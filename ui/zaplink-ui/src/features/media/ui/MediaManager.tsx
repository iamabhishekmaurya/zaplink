"use client"

import { useState } from 'react'
import { useSelector } from 'react-redux'
import { RootState } from '@/store'
import { useFolders, useAssets, useDeleteAsset, useDeleteFolder } from '@/hooks/useMedia'
import { FolderNavigation } from '@/features/media/ui/FolderNavigation'
import { FolderItem } from '@/features/media/ui/FolderItem'
import { FileItem } from '@/features/media/ui/FileItem'
import { UploadZone } from '@/features/media/ui/UploadZone'
import { CreateFolderDialog } from '@/features/media/ui/CreateFolderDialog'
import { Skeleton } from '@/components/ui/skeleton'
import { toast } from 'sonner'
import { LayoutGrid, List, UploadCloud } from 'lucide-react'
import { Button } from '@/components/ui/button'

export default function MediaManager() {
    // Auth State
    const { user } = useSelector((state: RootState) => state.auth);

    // Navigation State
    const [currentPath, setCurrentPath] = useState<{ id: string, name: string }[]>([]);
    // currentFolderId is the ID of the last folder in the path, or undefined if root
    const currentFolderId = currentPath.length > 0 ? currentPath[currentPath.length - 1].id : undefined;

    // View State
    const [viewMode, setViewMode] = useState<"grid" | "list">("grid");

    // Queries
    const { data: folders, isLoading: foldersLoading } = useFolders(user?.id || "", currentFolderId);
    const { data: assets, isLoading: assetsLoading } = useAssets(user?.id || "", currentFolderId);

    // Mutations
    const deleteAsset = useDeleteAsset();
    const deleteFolder = useDeleteFolder();

    const handleNavigate = (folderId: string | undefined) => {
        if (!folderId) {
            setCurrentPath([]); // Go to root
            return;
        }

        // Logic to construct new path
        // If we are clicking a folder in the current view, add it to path
        // If we are clicking a breadcrumb, truncate path

        // This simple logic assumes we only click folders available in the current view
        const targetFolder = folders?.find(f => f.id === folderId);
        if (targetFolder) {
            setCurrentPath(prev => [...prev, { id: targetFolder.id, name: targetFolder.name }]);
        } else {
            // Find in current path (breadcrumb navigation)
            const index = currentPath.findIndex(p => p.id === folderId);
            if (index !== -1) {
                setCurrentPath(prev => prev.slice(0, index + 1));
            }
        }
    };

    const handleDeleteAsset = (id: string) => {
        toast.promise(deleteAsset.mutateAsync(id), {
            loading: 'Deleting file...',
            success: 'File deleted',
            error: 'Failed to delete file'
        });
    };

    const handleDeleteFolder = (id: string) => {
        toast.promise(deleteFolder.mutateAsync(id), {
            loading: 'Deleting folder...',
            success: 'Folder deleted',
            error: 'Failed to delete folder (must be empty)'
        });
    };

    if (!user) {
        return <div className="p-8 text-center text-muted-foreground">Please log in to manage media.</div>;
    }

    const isLoading = foldersLoading || assetsLoading;
    const isEmpty = !isLoading && folders?.length === 0 && assets?.length === 0;

    return (
        <div className="flex flex-col h-full space-y-4 p-6">
            <div className="flex items-center justify-between">
                <FolderNavigation
                    path={currentPath}
                    onNavigate={(id) => {
                        if (!id) setCurrentPath([]);
                        else {
                            const index = currentPath.findIndex(p => p.id === id);
                            if (index !== -1) setCurrentPath(prev => prev.slice(0, index + 1));
                        }
                    }}
                />
                <div className="flex items-center gap-2">
                    <div className="flex bg-muted rounded-lg p-1">
                        <Button
                            variant={viewMode === 'grid' ? 'secondary' : 'ghost'}
                            size="icon"
                            className="h-8 w-8"
                            onClick={() => setViewMode('grid')}
                        >
                            <LayoutGrid className="h-4 w-4" />
                        </Button>
                        <Button
                            variant={viewMode === 'list' ? 'secondary' : 'ghost'}
                            size="icon"
                            className="h-8 w-8"
                            onClick={() => setViewMode('list')}
                        >
                            <List className="h-4 w-4" />
                        </Button>
                    </div>
                    <CreateFolderDialog ownerId={user.id} parentId={currentFolderId} />
                    <UploadZone ownerId={user.id} folderId={currentFolderId} />
                </div>
            </div>

            <div className="flex-1 overflow-y-auto min-h-[500px] border rounded-lg bg-background p-4 shadow-sm">
                {isLoading ? (
                    <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
                        {[1, 2, 3, 4].map(i => <Skeleton key={i} className="aspect-square rounded-lg" />)}
                    </div>
                ) : isEmpty ? (
                    <div className="flex flex-col items-center justify-center h-full text-muted-foreground animate-in fade-in-50">
                        <UploadCloud className="h-16 w-16 mb-4 opacity-20" />
                        <p>This folder is empty</p>
                    </div>
                ) : (
                    <div className={viewMode === 'grid' ? "grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4" : "flex flex-col gap-2"}>
                        {folders?.map(folder => (
                            <FolderItem
                                key={folder.id}
                                folder={folder}
                                onNavigate={handleNavigate}
                                onDelete={handleDeleteFolder}
                            />
                        ))}
                        {assets?.map(asset => (
                            <FileItem
                                key={asset.id}
                                file={asset}
                                onDelete={handleDeleteAsset}
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    )
}
