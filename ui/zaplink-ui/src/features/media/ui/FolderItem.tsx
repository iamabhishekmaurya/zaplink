"use client"

import { Folder, FolderOpen, Trash2 } from 'lucide-react'
import { ContextMenu, ContextMenuContent, ContextMenuItem, ContextMenuTrigger } from '@/components/ui/context-menu'
import { cn } from '@/lib/utils'

interface FolderItemProps {
    folder: {
        id: string;
        name: string;
    }
    onNavigate: (id: string) => void;
    onDelete: (id: string) => void;
}

export function FolderItem({ folder, onNavigate, onDelete }: FolderItemProps) {
    return (
        <ContextMenu>
            <ContextMenuTrigger>
                <div
                    onClick={() => onNavigate(folder.id)}
                    className={cn(
                        "flex flex-col items-center justify-center p-4 rounded-lg border bg-card hover:bg-accent/50 cursor-pointer transition-colors space-y-2 aspect-square"
                    )}
                >
                    <Folder className="h-12 w-12 text-blue-500 fill-blue-500/20" />
                    <span className="text-sm font-medium truncate w-full text-center select-none">
                        {folder.name}
                    </span>
                </div>
            </ContextMenuTrigger>
            <ContextMenuContent>
                <ContextMenuItem onClick={() => onNavigate(folder.id)}>
                    <FolderOpen className="mr-2 h-4 w-4" /> Open
                </ContextMenuItem>
                <ContextMenuItem onClick={() => onDelete(folder.id)} className="text-red-500 focus:text-red-500">
                    <Trash2 className="mr-2 h-4 w-4" /> Delete
                </ContextMenuItem>
            </ContextMenuContent>
        </ContextMenu>
    )
}
