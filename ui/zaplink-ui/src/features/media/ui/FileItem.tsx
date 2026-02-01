"use client"

import { FileIcon, Trash2, ExternalLink, ImageIcon } from 'lucide-react'
import { ContextMenu, ContextMenuContent, ContextMenuItem, ContextMenuTrigger } from '@/components/ui/context-menu'
import { cn } from '@/lib/utils'
import Image from 'next/image'

interface FileItemProps {
    file: {
        id: string;
        filename: string;
        url: string;
        mimeType: string;
        thumbnailPath?: string;
    }
    onDelete: (id: string) => void;
}

export function FileItem({ file, onDelete }: FileItemProps) {
    const isImage = file.mimeType.startsWith("image/");

    return (
        <ContextMenu>
            <ContextMenuTrigger>
                <div className="group relative flex flex-col items-center justify-center p-2 rounded-lg border bg-card hover:bg-accent/50 cursor-pointer transition-colors aspect-square">
                    <div className="relative w-full h-3/4 flex items-center justify-center overflow-hidden rounded-md mb-2 bg-muted/20">
                        {isImage ? (
                            <img
                                src={file.url}
                                alt={file.filename}
                                className="object-cover w-full h-full"
                                loading="lazy"
                                onError={(e) => {
                                    e.currentTarget.style.display = 'none';
                                    e.currentTarget.parentElement?.querySelector('.fallback-icon')?.classList.remove('hidden');
                                }}
                            />
                        ) : file.mimeType.startsWith("video/") ? (
                            <video
                                src={file.url}
                                className="object-cover w-full h-full"
                                controls={false}
                                muted
                                onMouseOver={(e) => e.currentTarget.play()}
                                onMouseOut={(e) => {
                                    e.currentTarget.pause();
                                    e.currentTarget.currentTime = 0;
                                }}
                            />
                        ) : (
                            <FileIcon className="h-12 w-12 text-muted-foreground" />
                        )}
                        {/* Fallback icon for broken images - hidden by default */}
                        {isImage && (
                            <FileIcon className="fallback-icon hidden h-12 w-12 text-muted-foreground absolute" />
                        )}
                    </div>
                    <span className="text-xs font-medium truncate w-full text-center select-none text-muted-foreground group-hover:text-foreground">
                        {file.filename}
                    </span>
                </div>
            </ContextMenuTrigger>
            <ContextMenuContent>
                <ContextMenuItem onClick={() => window.open(file.url, '_blank')}>
                    <ExternalLink className="mr-2 h-4 w-4" /> Open/Download
                </ContextMenuItem>
                <ContextMenuItem onClick={() => onDelete(file.id)} className="text-red-500 focus:text-red-500">
                    <Trash2 className="mr-2 h-4 w-4" /> Delete
                </ContextMenuItem>
            </ContextMenuContent>
        </ContextMenu>
    )
}
