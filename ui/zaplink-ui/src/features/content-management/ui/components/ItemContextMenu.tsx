import { MoreVertical, Edit2, FolderInput, Star, Trash2, Link, Download } from 'lucide-react';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Button } from '@/components/ui/button';
import { Folder, MediaItem } from '../../types';

interface ItemContextMenuProps {
    item: Folder | MediaItem;
    isFolder?: boolean;
    onRenameClick: (item: Folder | MediaItem, isFolder: boolean) => void;
    onDeleteClick: (item: Folder | MediaItem, isFolder: boolean) => void;
    onMoveClick?: (item: Folder | MediaItem, isFolder: boolean) => void;
    onFavoriteToggle?: (item: Folder | MediaItem, isFolder: boolean) => void;
}

export function ItemContextMenu({
    item,
    isFolder = false,
    onRenameClick,
    onDeleteClick,
    onMoveClick,
    onFavoriteToggle
}: ItemContextMenuProps) {

    // Copy URL to clipboard for media items
    const handleCopyLink = (e: React.MouseEvent) => {
        e.stopPropagation();
        if (!isFolder && 'url' in item) {
            navigator.clipboard.writeText((item as MediaItem).url);
            // In a real app we would use toast here, but we pass the responsibility up or
            // just let the user see the visual feedback if any
        }
    };

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild onClick={(e) => e.stopPropagation()}>
                <Button variant="ghost" className="h-8 w-8 p-0" aria-label="Open menu">
                    <MoreVertical className="h-4 w-4" />
                </Button>
            </DropdownMenuTrigger>

            <DropdownMenuContent align="end" className="w-[180px]">
                <DropdownMenuLabel className="truncate max-w-[160px]" title={item.name}>
                    {item.name}
                </DropdownMenuLabel>
                <DropdownMenuSeparator />

                {!isFolder && (
                    <>
                        <DropdownMenuItem onClick={handleCopyLink} className="cursor-pointer">
                            <Link className="mr-2 h-4 w-4" />
                            Copy Link
                        </DropdownMenuItem>
                        <DropdownMenuItem className="cursor-pointer" onClick={(e) => {
                            e.stopPropagation();
                            window.open((item as MediaItem).url, '_blank');
                        }}>
                            <Download className="mr-2 h-4 w-4" />
                            Download
                        </DropdownMenuItem>
                        <DropdownMenuSeparator />
                    </>
                )}

                <DropdownMenuItem
                    onClick={(e) => { e.stopPropagation(); onRenameClick(item, isFolder); }}
                    className="cursor-pointer"
                >
                    <Edit2 className="mr-2 h-4 w-4" />
                    Rename
                </DropdownMenuItem>

                {onMoveClick && (
                    <DropdownMenuItem
                        onClick={(e) => { e.stopPropagation(); onMoveClick(item, isFolder); }}
                        className="cursor-pointer"
                    >
                        <FolderInput className="mr-2 h-4 w-4" />
                        Move to...
                    </DropdownMenuItem>
                )}

                {onFavoriteToggle && (
                    <DropdownMenuItem
                        onClick={(e) => { e.stopPropagation(); onFavoriteToggle(item, isFolder); }}
                        className="cursor-pointer"
                    >
                        <Star className={`mr-2 h-4 w-4 ${item.isFavorite ? 'fill-yellow-500 text-yellow-500' : ''}`} />
                        {item.isFavorite ? 'Unfavorite' : 'Favorite'}
                    </DropdownMenuItem>
                )}

                <DropdownMenuSeparator />

                <DropdownMenuItem
                    onClick={(e) => { e.stopPropagation(); onDeleteClick(item, isFolder); }}
                    className="cursor-pointer text-destructive focus:bg-destructive/10 focus:text-destructive"
                >
                    <Trash2 className="mr-2 h-4 w-4" />
                    {item.isDeleted ? 'Delete Permanently' : 'Move to Trash'}
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}
