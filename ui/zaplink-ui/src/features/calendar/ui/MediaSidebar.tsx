'use client';

import React, { useEffect, useState } from 'react';
import { useDraggable } from '@dnd-kit/core';
import { useQuery } from '@tanstack/react-query';
import { MediaAsset, schedulerApi } from '@/services/scheduler';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Skeleton } from '@/components/ui/skeleton';
import Image from 'next/image';
import { cn } from '@/lib/utils';
import { GripVertical, Image as ImageIcon, Video } from 'lucide-react';

export const MediaSidebar = () => {
    const { data: media, isLoading, isError } = useQuery({
        queryKey: ['media-assets'],
        queryFn: schedulerApi.getMediaItems,
    });

    if (isError) {
        return (
            <div className="flex w-64 flex-col border-r bg-background items-center justify-center p-4 text-center">
                <p className="text-sm text-destructive">Failed to load media library.</p>
            </div>
        );
    }

    return (
        <div className="flex w-64 flex-col border-r bg-background">
            <div className="flex h-14 items-center border-b px-4">
                <h2 className="text-sm font-semibold">Media Library</h2>
            </div>
            <ScrollArea className="flex-1">
                <div className="grid grid-cols-2 gap-2 p-4">
                    {isLoading ? (
                        Array.from({ length: 8 }).map((_, i) => (
                            <Skeleton key={i} className="aspect-square w-full rounded-md" />
                        ))
                    ) : (
                        media?.map((item) => (
                            <DraggableMediaItem key={item.id} item={item} />
                        ))
                    )}
                </div>
            </ScrollArea>
        </div>
    );
};

const DraggableMediaItem = ({ item }: { item: MediaAsset }) => {
    const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
        id: `media-${item.id}`,
        data: {
            type: 'media',
            media: item,
        },
    });

    const style = transform ? {
        transform: `translate3d(${transform.x}px, ${transform.y}px, 0)`,
    } : undefined;

    return (
        <div
            ref={setNodeRef}
            style={style}
            {...listeners}
            {...attributes}
            className={cn(
                "group relative aspect-square cursor-grab overflow-hidden rounded-md border bg-muted transition-all hover:ring-2 hover:ring-primary/50",
                isDragging && "z-50 opacity-50 ring-2 ring-primary"
            )}
        >
            <Image
                src={item.type === 'video' ? (item.thumbnail || item.url) : item.url}
                alt="Media asset"
                fill
                unoptimized
                className="object-cover transition-transform group-hover:scale-105"
                sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
            />

            {/* Type Icon Overlay */}
            <div className="absolute left-1 top-1 rounded bg-black/50 p-1 text-white backdrop-blur-sm">
                {item.type === 'video' ? <Video size={12} /> : <ImageIcon size={12} />}
            </div>

            {/* Drag Handle Overlay */}
            <div className="absolute inset-0 flex items-center justify-center bg-black/0 text-white opacity-0 transition-all group-hover:bg-black/20 group-hover:opacity-100">
                <GripVertical className="h-6 w-6" />
            </div>
        </div>
    );
};
