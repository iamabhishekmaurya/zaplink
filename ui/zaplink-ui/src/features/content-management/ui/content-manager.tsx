"use client"

import React from 'react';
import { motion } from 'framer-motion';
import { useContentManager } from '../hooks/useContentManager';
import { Sidebar } from './components/Sidebar';
import { Toolbar } from './components/Toolbar';
import { ContentGrid } from './components/ContentGrid';
import { CreateFolderDialog } from './components/CreateFolderDialog';
import { UploadMediaDialog } from './components/UploadMediaDialog';
import { RenameDialog } from './components/RenameDialog';
import { DeleteConfirmationDialog } from './components/DeleteConfirmationDialog';
import { MoveDialog } from './components/MoveDialog';
import { Folder, MediaItem } from '../types';
import { ContentApi } from '@/services/contentApi';
import { toast } from 'sonner';

export default function ContentManager() {
    const [isCreateFolderOpen, setIsCreateFolderOpen] = React.useState(false);
    const [isUploadOpen, setIsUploadOpen] = React.useState(false);

    // Advanced Action States
    const [renameItem, setRenameItem] = React.useState<{ item: Folder | MediaItem, isFolder: boolean } | null>(null);
    const [deleteItem, setDeleteItem] = React.useState<{ item: Folder | MediaItem, isFolder: boolean } | null>(null);
    const [moveItem, setMoveItem] = React.useState<{ item: Folder | MediaItem, isFolder: boolean } | null>(null);
    const [isUploadingDrop, setIsUploadingDrop] = React.useState(false);
    const [isSelectMode, setIsSelectMode] = React.useState(false);

    const {
        folders,
        allTreeFolders,
        allTreeMedia,
        media,
        currentFolderId,
        folderPath,
        viewMode,
        activeTab,
        searchQuery,
        isLoading,
        setViewMode,
        setActiveTab,
        setSearchQuery,
        handleNavigate,
        reload
    } = useContentManager();

    const handleUploadClick = () => setIsUploadOpen(true);
    const handleCreateFolderClick = () => setIsCreateFolderOpen(true);

    const handleRenameClick = (item: Folder | MediaItem, isFolder: boolean) => setRenameItem({ item, isFolder });
    const handleDeleteClick = (item: Folder | MediaItem, isFolder: boolean) => setDeleteItem({ item, isFolder });
    const handleMoveClick = (item: Folder | MediaItem, isFolder: boolean) => setMoveItem({ item, isFolder });

    const handleRenameSubmit = async (newName: string) => {
        if (!renameItem) return;
        try {
            if (renameItem.isFolder) {
                await ContentApi.renameFolder(renameItem.item.id, newName);
            } else {
                await ContentApi.updateMediaMetadata(renameItem.item.id, newName, (renameItem.item as MediaItem).tags);
            }
            toast.success("Renamed successfully");
            reload();
        } catch (error) {
            console.error("Failed to rename", error);
            toast.error("Failed to rename item");
        }
    };

    const handleDeleteSubmit = async () => {
        if (!deleteItem) return;
        try {
            const isPermanent = deleteItem.item.isDeleted;
            if (deleteItem.isFolder) {
                await ContentApi.deleteFolder(deleteItem.item.id, isPermanent);
            } else {
                await ContentApi.deleteMedia(deleteItem.item.id, isPermanent);
            }
            toast.success(isPermanent ? "Permanently deleted" : "Moved to trash");
            reload();
        } catch (error) {
            console.error("Failed to delete", error);
            toast.error("Failed to delete item");
        }
    };

    const handleFavoriteToggle = async (item: Folder | MediaItem, isFolder: boolean) => {
        try {
            if (isFolder) {
                await ContentApi.toggleFolderFavorite(item.id);
            } else {
                await ContentApi.toggleMediaFavorite(item.id);
            }
            toast.success(item.isFavorite ? "Removed from favorites" : "Added to favorites");
            reload();
        } catch (error) {
            console.error("Failed to toggle favorite", error);
            toast.error("Action failed");
        }
    };

    const handleMoveSubmit = async (newFolderId: string | undefined) => {
        if (!moveItem) return;
        try {
            if (moveItem.isFolder) {
                await ContentApi.moveFolder(moveItem.item.id, newFolderId);
            } else {
                await ContentApi.moveMedia(moveItem.item.id, newFolderId);
            }
            toast.success("Moved successfully");
            reload();
        } catch (error) {
            console.error("Failed to move item", error);
            toast.error("Failed to move item");
        }
    };

    // Handle file drops
    const handleFilesDrop = async (files: File[]) => {
        if (files.length === 0) return;
        setIsUploadingDrop(true);
        // Simple sequential upload for Phase 4 to avoid overwhelming the server
        let uploadedCount = 0;
        try {
            toast.loading(`Uploading ${files.length} files...`, { id: 'drop-upload' });
            for (const file of files) {
                await ContentApi.uploadMedia(file, currentFolderId);
                uploadedCount++;
            }
            toast.success(`Successfully uploaded ${uploadedCount} files`, { id: 'drop-upload' });
            reload();
        } catch (error) {
            console.error("Failed to upload dropped files", error);
            toast.error(`Uploaded ${uploadedCount}/${files.length} files. Some failed.`, { id: 'drop-upload' });
            reload(); // Reload to show the ones that succeeded
        } finally {
            setIsUploadingDrop(false);
        }
    };

    // Handle bulk delete of multiple items
    const handleBulkDelete = async (selectedItems: { item: Folder | MediaItem; isFolder: boolean }[]) => {
        if (selectedItems.length === 0) return;
        try {
            toast.loading(`Deleting ${selectedItems.length} item${selectedItems.length !== 1 ? 's' : ''}...`, { id: 'bulk-delete' });
            await Promise.all(
                selectedItems.map(({ item, isFolder }) => {
                    const isPermanent = item.isDeleted;
                    return isFolder
                        ? ContentApi.deleteFolder(item.id, isPermanent)
                        : ContentApi.deleteMedia(item.id, isPermanent);
                })
            );
            const allPermanent = selectedItems.every(({ item }) => item.isDeleted);
            toast.success(
                allPermanent
                    ? `Permanently deleted ${selectedItems.length} item${selectedItems.length !== 1 ? 's' : ''}`
                    : `Moved ${selectedItems.length} item${selectedItems.length !== 1 ? 's' : ''} to trash`,
                { id: 'bulk-delete' }
            );
            reload();
        } catch (error) {
            console.error('Bulk delete failed', error);
            toast.error('Some items could not be deleted', { id: 'bulk-delete' });
            reload();
        }
    };

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="flex flex-col md:flex-row h-full gap-6"
        >
            {/* Folder Navigation Tree */}
            <Sidebar
                folders={allTreeFolders}
                media={allTreeMedia}
                currentFolderId={currentFolderId}
                activeTab={activeTab}
                onNavigate={handleNavigate}
                onTabChange={setActiveTab}
            />

            {/* Main Workspace Area */}
            <motion.div
                layout
                className="flex-1 flex flex-col bg-card/50 backdrop-blur border border-border/50 overflow-hidden rounded-xl shadow-sm"
            >


                <Toolbar
                    searchQuery={searchQuery}
                    onSearchChange={setSearchQuery}
                    viewMode={viewMode}
                    onViewModeChange={setViewMode}
                    onUploadClick={handleUploadClick}
                    onCreateFolderClick={handleCreateFolderClick}
                    isReadOnly={activeTab === 'trash'}
                    folderPath={folderPath}
                    onNavigate={handleNavigate}
                    isSelectMode={isSelectMode}
                    onToggleSelectMode={() => setIsSelectMode(v => !v)}
                />

                <ContentGrid
                    folders={folders}
                    media={media}
                    viewMode={viewMode}
                    isLoading={isLoading}
                    onFolderClick={handleNavigate}
                    onRenameClick={handleRenameClick}
                    onDeleteClick={handleDeleteClick}
                    onFavoriteToggle={handleFavoriteToggle}
                    onMoveClick={handleMoveClick}
                    onFilesDrop={handleFilesDrop}
                    onBulkDelete={handleBulkDelete}
                    isSelectMode={isSelectMode}
                    onExitSelectMode={() => setIsSelectMode(false)}
                />
            </motion.div>

            {/* Dialogs */}
            <CreateFolderDialog
                open={isCreateFolderOpen}
                onOpenChange={setIsCreateFolderOpen}
                parentId={currentFolderId}
                onSuccess={reload}
            />

            <UploadMediaDialog
                open={isUploadOpen}
                onOpenChange={setIsUploadOpen}
                folderId={currentFolderId}
                folderPath={folderPath}
                onSuccess={reload}
            />

            <RenameDialog
                open={!!renameItem}
                onOpenChange={(open) => !open && setRenameItem(null)}
                currentName={renameItem?.item.name || ""}
                onRename={handleRenameSubmit}
            />

            <DeleteConfirmationDialog
                open={!!deleteItem}
                onOpenChange={(open) => !open && setDeleteItem(null)}
                itemName={deleteItem?.item.name || ""}
                isFolder={deleteItem?.isFolder}
                onConfirm={handleDeleteSubmit}
            />

            <MoveDialog
                open={!!moveItem}
                onOpenChange={(open) => !open && setMoveItem(null)}
                item={moveItem?.item || null}
                isFolder={moveItem?.isFolder || false}
                currentFolderId={currentFolderId}
                onMove={handleMoveSubmit}
            />
        </motion.div>
    );
}
