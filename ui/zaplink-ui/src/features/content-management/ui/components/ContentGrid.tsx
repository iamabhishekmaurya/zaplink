import { FolderIcon, FileIcon, FileText, Film, FileArchive, FileCode, Star, Trash2 } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Folder, MediaItem, ViewMode } from '../../types';
import { motion, AnimatePresence } from 'framer-motion';
import { EmptyState } from './EmptyState';
import { ItemContextMenu } from './ItemContextMenu';
import { MediaLightbox } from './MediaLightbox';
import { useDropzone } from 'react-dropzone';
import { UploadCloud } from 'lucide-react';
import { useState, useEffect } from 'react';

// Returns the appropriate icon and color class for a given MIME type
function FileTypeIcon({ type, className = 'w-10 h-10' }: { type?: string; className?: string }) {
    if (!type) return <FileIcon className={`${className} text-muted-foreground/50`} />;
    if (type === 'application/pdf') return (
        <div className="flex flex-col items-center justify-center gap-1">
            <FileText className={`${className} text-red-400`} />
            <span className="text-[10px] font-bold text-red-400 uppercase tracking-widest">PDF</span>
        </div>
    );
    if (type.startsWith('video/')) return <Film className={`${className} text-purple-400`} />;
    if (type.includes('zip') || type.includes('rar') || type.includes('tar') || type.includes('gz'))
        return <FileArchive className={`${className} text-yellow-400`} />;
    if (type.includes('javascript') || type.includes('typescript') || type.includes('html') || type.includes('css'))
        return <FileCode className={`${className} text-green-400`} />;
    return <FileIcon className={`${className} text-muted-foreground/50`} />;
}



interface ContentGridProps {
    folders: Folder[];
    media: MediaItem[];
    viewMode: ViewMode;
    isLoading: boolean;
    onFolderClick: (folderId: string, folderName?: string) => void;
    // Actions
    onRenameClick: (item: Folder | MediaItem, isFolder: boolean) => void;
    onDeleteClick: (item: Folder | MediaItem, isFolder: boolean) => void;
    onFavoriteToggle: (item: Folder | MediaItem, isFolder: boolean) => void;
    onMoveClick: (item: Folder | MediaItem, isFolder: boolean) => void;
    onFilesDrop?: (files: File[]) => void;
    onBulkDelete?: (items: { item: Folder | MediaItem; isFolder: boolean }[]) => void;
    isSelectMode?: boolean;
    onExitSelectMode?: () => void;
}

export function ContentGrid({
    folders,
    media,
    viewMode,
    isLoading,
    onFolderClick,
    onRenameClick,
    onDeleteClick,
    onFavoriteToggle,
    onMoveClick,
    onFilesDrop,
    onBulkDelete,
    isSelectMode = false,
    onExitSelectMode,
}: ContentGridProps) {
    const [lightboxIndex, setLightboxIndex] = useState<number | null>(null);
    const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());

    // When "Select All" button is clicked (isSelectMode turns on), immediately select everything
    useEffect(() => {
        if (isSelectMode) {
            setSelectedIds(new Set([...folders.map(f => f.id), ...media.map(m => m.id)]));
        } else {
            setSelectedIds(new Set());
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isSelectMode]);

    const allIds = [...folders.map(f => f.id), ...media.map(m => m.id)];
    const allSelected = allIds.length > 0 && selectedIds.size === allIds.length;
    const hasSelection = selectedIds.size > 0;
    // Show checkboxes when selection mode is active externally OR when user has selected items
    const showCheckboxes = isSelectMode || hasSelection;

    const toggleSelect = (id: string, e: React.MouseEvent) => {
        e.stopPropagation();
        setSelectedIds(prev => {
            const next = new Set(prev);
            next.has(id) ? next.delete(id) : next.add(id);
            return next;
        });
    };

    const selectAll = () => setSelectedIds(new Set(allIds));
    const deselectAll = () => setSelectedIds(new Set());

    const handleBulkDelete = () => {
        if (!onBulkDelete) return;
        const selectedItemObjects = [...selectedIds].map(id => {
            const folder = folders.find(f => f.id === id);
            if (folder) return { item: folder as Folder | MediaItem, isFolder: true };
            const mediaItem = media.find(m => m.id === id);
            return { item: mediaItem as Folder | MediaItem, isFolder: false };
        }).filter(x => x.item != null) as { item: Folder | MediaItem; isFolder: boolean }[];
        onBulkDelete(selectedItemObjects);
        deselectAll();
    };

    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        onDrop: onFilesDrop,
        noClick: true,
        noKeyboard: true,
    });

    // The drag root is applied to the whole container, but pointer-events are
    // disabled on it at all times — clicks pass through to cards normally.
    // Drag events still bubble up and are detected correctly.
    const { ref: dropRef, ...dropProps } = getRootProps();
    void dropProps; // used via ref only
    if (isLoading) {
        return (
            <div className="flex-1 p-6 grid grid-cols-2 md:grid-cols-4 gap-4">
                {[1, 2, 3, 4].map(i => (
                    <div key={i} className="aspect-square bg-muted/20 animate-pulse rounded-xl" />
                ))}
            </div>
        );
    }

    if (folders.length === 0 && media.length === 0) {
        return (
            <div className="flex-1 p-6">
                <EmptyState />
            </div>
        );
    }

    const itemVariants = {
        hidden: { opacity: 0, y: 10, scale: 0.95 },
        visible: { opacity: 1, y: 0, scale: 1 }
    };

    return (
        <div
            ref={dropRef}
            className={`flex-1 p-6 overflow-y-auto relative transition-colors ${isDragActive ? 'bg-primary/5' : ''}`}
            onDragEnter={dropProps.onDragEnter}
            onDragOver={dropProps.onDragOver}
            onDragLeave={dropProps.onDragLeave}
            onDrop={dropProps.onDrop}
        >
            <input {...getInputProps()} />

            {/* Drag Overlay */}
            <AnimatePresence>
                {isDragActive && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        className="absolute inset-0 z-50 flex items-center justify-center bg-background/80 backdrop-blur-sm border-2 border-primary border-dashed rounded-xl m-4"
                    >
                        <div className="flex flex-col items-center justify-center text-center p-6 bg-card rounded-2xl shadow-xl flex flex-col gap-4 max-w-sm">
                            <div className="h-16 w-16 rounded-full bg-primary/10 flex items-center justify-center">
                                <UploadCloud className="w-8 h-8 text-primary" />
                            </div>
                            <div>
                                <h3 className="text-xl font-bold mb-1">Drop to Upload</h3>
                                <p className="text-sm text-muted-foreground">Release your files anywhere here to instantly upload them to the current folder.</p>
                            </div>
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>

            {/* Bulk Selection Action Bar */}
            <AnimatePresence>
                {hasSelection && (
                    <motion.div
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -10 }}
                        className="sticky top-0 z-30 mb-4 flex items-center justify-between gap-3 bg-background/95 backdrop-blur border border-border rounded-xl px-4 py-2.5 shadow-lg"
                    >
                        <div className="flex items-center gap-3">
                            <span className="text-sm font-semibold text-foreground">
                                {selectedIds.size} item{selectedIds.size !== 1 ? 's' : ''} selected
                            </span>
                            <div className="w-px h-4 bg-border" />
                            <Button variant="ghost" size="sm" className="h-7 text-xs" onClick={allSelected ? deselectAll : selectAll}>
                                {allSelected ? 'Deselect All' : `Select All (${allIds.length})`}
                            </Button>
                        </div>
                        <div className="flex items-center gap-2">
                            <Button variant="ghost" size="sm" className="h-7 text-xs" onClick={() => { deselectAll(); onExitSelectMode?.(); }}>
                                Cancel
                            </Button>
                            <Button
                                variant="destructive"
                                size="sm"
                                className="h-7 text-xs px-3 gap-1.5"
                                onClick={handleBulkDelete}
                            >
                                <Trash2 className="w-3.5 h-3.5" />
                                Delete {selectedIds.size} item{selectedIds.size !== 1 ? 's' : ''}
                            </Button>
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>

            <motion.div
                className={viewMode === 'grid' ? "grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4" : "flex flex-col gap-2"}
                initial="hidden"
                animate="visible"
                variants={{
                    visible: { transition: { staggerChildren: 0.05 } }
                }}
            >
                <AnimatePresence>
                    {/* Render Folders */}
                    {folders.map(f => (
                        <motion.div key={f.id} variants={itemVariants} layoutId={`folder-${f.id}`}>
                            <Card
                                className={`group relative overflow-hidden cursor-pointer transition-colors border-2 ${selectedIds.has(f.id) ? 'border-primary' : 'border-transparent hover:border-primary/30'} ${viewMode === 'grid'
                                    ? "h-full flex flex-col aspect-square p-0 gap-0"
                                    : "flex flex-row items-center p-3 gap-3 w-full"
                                    }`}
                                onClick={(e) => showCheckboxes ? toggleSelect(f.id, e) : onFolderClick(f.id, f.name)}
                            >
                                {viewMode === 'grid' ? (
                                    <>
                                        {/* Checkbox - shows on hover or when in selection mode */}
                                        <div
                                            className={`absolute top-2 left-2 z-20 transition-all ${showCheckboxes ? 'opacity-100' : 'opacity-0 group-hover:opacity-100'}`}
                                            onClick={(e) => toggleSelect(f.id, e)}
                                        >
                                            <div className={`w-5 h-5 rounded border-2 flex items-center justify-center cursor-pointer transition-colors ${selectedIds.has(f.id) ? 'bg-primary border-primary' : 'bg-background/80 border-white shadow'}`}>
                                                {selectedIds.has(f.id) && <svg className="w-3 h-3 text-primary-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}><path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" /></svg>}
                                            </div>
                                        </div>
                                        <div className="flex-1 bg-muted/20 flex items-center justify-center relative overflow-hidden h-full">
                                            <FolderIcon className="w-16 h-16 text-blue-500 fill-blue-500/20 transform group-hover:scale-105 transition-transform duration-300" />
                                        </div>
                                        <div className="p-3 bg-card flex items-center justify-between shrink-0 h-14">
                                            <div className="overflow-hidden pr-2">
                                                <p className="text-sm font-medium truncate" title={f.name}>{f.name}</p>
                                                <p className="text-xs text-muted-foreground mt-0.5 truncate">Folder</p>
                                            </div>
                                            <div onClick={(e) => e.stopPropagation()} className="shrink-0 -mr-2">
                                                <ItemContextMenu item={f} isFolder={true} onRenameClick={onRenameClick} onDeleteClick={onDeleteClick} onFavoriteToggle={onFavoriteToggle} onMoveClick={onMoveClick} />
                                            </div>
                                        </div>
                                    </>
                                ) : (
                                    <>
                                        <div onClick={(e) => toggleSelect(f.id, e)} className="shrink-0">
                                            <div className={`w-5 h-5 rounded border-2 flex items-center justify-center cursor-pointer transition-colors ${selectedIds.has(f.id) ? 'bg-primary border-primary' : 'bg-background border-border'}`}>
                                                {selectedIds.has(f.id) && <svg className="w-3 h-3 text-primary-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}><path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" /></svg>}
                                            </div>
                                        </div>
                                        <FolderIcon className="w-8 h-8 text-blue-500 fill-blue-500/20 flex-shrink-0" />
                                        <div className="flex-1 min-w-0" onClick={() => onFolderClick(f.id, f.name)}>
                                            <p className="text-sm font-medium truncate">{f.name}</p>
                                            <p className="text-xs text-muted-foreground mt-0.5">Folder</p>
                                        </div>
                                        <div onClick={(e) => e.stopPropagation()} className="ml-auto flex-shrink-0">
                                            <ItemContextMenu item={f} isFolder={true} onRenameClick={onRenameClick} onDeleteClick={onDeleteClick} onFavoriteToggle={onFavoriteToggle} onMoveClick={onMoveClick} />
                                        </div>
                                    </>
                                )}
                            </Card>
                        </motion.div>
                    ))}


                    {/* Render Media */}
                    {media.map((m, idx) => (
                        <motion.div key={m.id} variants={itemVariants} layoutId={`media-${m.id}`}>
                            <Card
                                className={`group relative overflow-hidden cursor-pointer transition-colors border-2 ${selectedIds.has(m.id) ? 'border-primary' : 'border-transparent hover:border-primary/30'} ${viewMode === 'grid' ? 'h-full flex flex-col aspect-square p-0 gap-0' : 'flex flex-row items-center p-2 w-full'
                                    }`}
                                onClick={(e) => showCheckboxes ? toggleSelect(m.id, e) : setLightboxIndex(idx)}
                            >
                                {viewMode === 'grid' ? (
                                    <>
                                        <div className="flex-1 bg-muted/20 flex items-center justify-center relative overflow-hidden h-full">
                                            {/* Selection Checkbox */}
                                            <div
                                                className={`absolute top-2 left-2 z-20 transition-all ${showCheckboxes ? 'opacity-100' : 'opacity-0 group-hover:opacity-100'}`}
                                                onClick={(e) => toggleSelect(m.id, e)}
                                            >
                                                <div className={`w-5 h-5 rounded border-2 flex items-center justify-center cursor-pointer transition-colors ${selectedIds.has(m.id) ? 'bg-primary border-primary' : 'bg-background/80 border-white shadow'}`}>
                                                    {selectedIds.has(m.id) && <svg className="w-3 h-3 text-primary-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}><path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" /></svg>}
                                                </div>
                                            </div>
                                            {m.type?.startsWith('image/') ? (
                                                <img src={m.url} alt={m.name} className="object-cover w-full h-full absolute inset-0 transform group-hover:scale-105 transition-transform duration-500" />
                                            ) : m.type?.startsWith('video/') ? (
                                                <video
                                                    src={m.url}
                                                    className="object-cover w-full h-full absolute inset-0 transform group-hover:scale-105 transition-transform duration-500"
                                                    preload="metadata"
                                                    muted
                                                    playsInline
                                                    onMouseEnter={(e) => (e.currentTarget as HTMLVideoElement).play()}
                                                    onMouseLeave={(e) => { const v = e.currentTarget as HTMLVideoElement; v.pause(); v.currentTime = 0; }}
                                                />
                                            ) : (
                                                <FileTypeIcon type={m.type} />
                                            )}

                                            {/* Persistent Favorite Icon (Visible if favorited) */}
                                            {m.isFavorite && (
                                                <div className="absolute top-2 right-2 z-10 transition-opacity">
                                                    <Button
                                                        variant="secondary"
                                                        size="icon"
                                                        className="h-8 w-8 bg-yellow-500/20 hover:bg-yellow-500/30 text-white border-0 shadow-none cursor-pointer"
                                                        onClick={(e) => { e.stopPropagation(); onFavoriteToggle(m, false); }}
                                                    >
                                                        <Star className="w-4 h-4 fill-yellow-500 text-yellow-500" />
                                                    </Button>
                                                </div>
                                            )}

                                            {/* Hover Overlay Actions (Visible only on hover) */}
                                            <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-start justify-end p-2 gap-1.5 backdrop-blur-[2px]">
                                                {!m.isFavorite && (
                                                    <Button
                                                        variant="secondary"
                                                        size="icon"
                                                        className="h-8 w-8 bg-white/10 hover:bg-white/20 text-white border-0"
                                                        onClick={(e) => { e.stopPropagation(); onFavoriteToggle(m, false); }}
                                                    >
                                                        <Star className="w-4 h-4" />
                                                    </Button>
                                                )}
                                                <Button
                                                    variant="destructive"
                                                    size="icon"
                                                    className="h-8 w-8 hover:bg-red-500/90 border-0"
                                                    onClick={(e) => { e.stopPropagation(); onDeleteClick(m, false); }}
                                                >
                                                    <Trash2 className="w-4 h-4" />
                                                </Button>
                                            </div>
                                        </div>
                                        <div className="p-3 bg-card flex items-center justify-between shrink-0 h-14">
                                            <div className="overflow-hidden pr-2">
                                                <p className="text-sm font-medium truncate" title={m.name}>{m.name}</p>
                                                <p className="text-xs text-muted-foreground mt-0.5 truncate">{(m.size / 1024 / 1024).toFixed(2)} MB</p>
                                            </div>
                                            <div onClick={(e) => e.stopPropagation()} className="shrink-0 -mr-2">
                                                <ItemContextMenu
                                                    item={m}
                                                    isFolder={false}
                                                    onRenameClick={onRenameClick}
                                                    onDeleteClick={onDeleteClick}
                                                    onFavoriteToggle={onFavoriteToggle}
                                                    onMoveClick={onMoveClick}
                                                />
                                            </div>
                                        </div>
                                    </>
                                ) : (
                                    <>
                                        <div className="w-10 h-10 ml-2 bg-muted/30 rounded-md flex items-center justify-center overflow-hidden shrink-0">
                                            {m.type?.startsWith('image/') ? (
                                                <img src={m.url} alt={m.name} className="object-cover w-full h-full" />
                                            ) : (
                                                <FileIcon className="w-5 h-5 text-muted-foreground/50" />
                                            )}
                                        </div>
                                        <div className="flex-1 min-w-0 py-2 pl-4">
                                            <p className="text-sm font-medium truncate">{m.name}</p>
                                            <p className="text-xs text-muted-foreground truncate">{m.type}</p>
                                        </div>
                                        <div className="w-24 text-right text-xs text-muted-foreground shrink-0 pl-4 py-2 border-l border-border/50">
                                            {(m.size / 1024 / 1024).toFixed(2)} MB
                                        </div>
                                        <div className="shrink-0 p-2 ml-2" onClick={(e) => e.stopPropagation()}>
                                            <ItemContextMenu
                                                item={m}
                                                isFolder={false}
                                                onRenameClick={onRenameClick}
                                                onDeleteClick={onDeleteClick}
                                                onFavoriteToggle={onFavoriteToggle}
                                                onMoveClick={onMoveClick}
                                            />
                                        </div>
                                    </>
                                )}
                            </Card>
                        </motion.div>
                    ))}
                </AnimatePresence>
            </motion.div>

            <MediaLightbox
                media={media}
                initialIndex={lightboxIndex ?? 0}
                open={lightboxIndex !== null}
                onOpenChange={(open) => !open && setLightboxIndex(null)}
                onDelete={(item) => {
                    setLightboxIndex(null);
                    onDeleteClick(item, false);
                }}
                onFavorite={(item) => onFavoriteToggle(item, false)}
            />
        </div>
    );
}
