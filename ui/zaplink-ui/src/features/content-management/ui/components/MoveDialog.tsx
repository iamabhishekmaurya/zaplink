import { useState, useEffect } from 'react';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Folder, MediaItem } from '../../types';
import { ContentApi } from '@/services/contentApi';
import { FolderIcon, ChevronRight } from 'lucide-react';
import { ScrollArea } from '@/components/ui/scroll-area';

interface MoveDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    item: Folder | MediaItem | null;
    isFolder: boolean;
    currentFolderId?: string;
    onMove: (newFolderId: string | undefined) => Promise<void>;
}

export function MoveDialog({ open, onOpenChange, item, isFolder, currentFolderId, onMove }: MoveDialogProps) {
    const [isMoving, setIsMoving] = useState(false);
    const [folders, setFolders] = useState<Folder[]>([]);
    const [selectedFolderId, setSelectedFolderId] = useState<string | undefined>(currentFolderId);

    // Fetch all folders to build the tree (simplified to flat list for Phase 3)
    useEffect(() => {
        if (open) {
            setSelectedFolderId(currentFolderId);
            ContentApi.listFolders() // Fetch root folders
                .then(res => setFolders(res.data))
                .catch(err => console.error("Failed to fetch folders for move dialog", err));
        }
    }, [open, currentFolderId]);

    const handleConfirm = async () => {
        setIsMoving(true);
        try {
            await onMove(selectedFolderId);
            onOpenChange(false);
        } finally {
            setIsMoving(false);
        }
    };

    if (!item) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Move "{item.name}"</DialogTitle>
                    <DialogDescription>
                        Select a destination folder to move this item into.
                    </DialogDescription>
                </DialogHeader>

                <ScrollArea className="h-[200px] w-full rounded-md border p-4 my-4 bg-muted/20">
                    <div className="space-y-1">
                        <Button
                            variant={selectedFolderId === undefined ? "secondary" : "ghost"}
                            className="w-full justify-start h-9"
                            onClick={() => setSelectedFolderId(undefined)}
                        >
                            <FolderIcon className="w-4 h-4 mr-2" />
                            Root Directory
                        </Button>

                        {folders
                            .filter(f => isFolder ? f.id !== item.id : true) // Prevent moving folder into itself
                            .map(folder => (
                                <Button
                                    key={folder.id}
                                    variant={selectedFolderId === folder.id ? "secondary" : "ghost"}
                                    className="w-full justify-start h-9 pl-6"
                                    onClick={() => setSelectedFolderId(folder.id)}
                                >
                                    <ChevronRight className="w-3 h-3 mr-2 opacity-50" />
                                    <FolderIcon className="w-4 h-4 mr-2 text-blue-500" />
                                    {folder.name}
                                </Button>
                            ))}

                        {folders.length === 0 && (
                            <p className="text-sm text-muted-foreground italic text-center py-4">
                                No subfolders available.
                            </p>
                        )}
                    </div>
                </ScrollArea>

                <DialogFooter>
                    <Button type="button" variant="outline" onClick={() => onOpenChange(false)} disabled={isMoving}>
                        Cancel
                    </Button>
                    <Button
                        type="button"
                        onClick={handleConfirm}
                        disabled={isMoving || selectedFolderId === currentFolderId}
                    >
                        {isMoving ? "Moving..." : "Move Here"}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
