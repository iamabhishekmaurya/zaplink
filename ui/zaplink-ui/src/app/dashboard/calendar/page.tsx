'use client';

import React, { useState, useEffect } from 'react';
import {
    DndContext,
    DragOverlay,
    useSensor,
    useSensors,
    PointerSensor,
    DragStartEvent,
    DragEndEvent,
    defaultDropAnimationSideEffects,
    DropAnimation
} from '@dnd-kit/core';
import { MediaSidebar } from '@/components/calendar/MediaSidebar';
import { SchedulerCalendar } from '@/components/calendar/SchedulerCalendar';
import { PostComposer } from '@/components/calendar/PostComposer';
import { ScheduledPost, schedulerApi, MediaAsset, CreatePostRequest, UpdatePostRequest } from '@/lib/api/scheduler';
import { createPortal } from 'react-dom';
import Image from 'next/image';
import { ScheduledPostCard } from '@/components/calendar/ScheduledPostCard';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

const dropAnimation: DropAnimation = {
    sideEffects: defaultDropAnimationSideEffects({
        styles: {
            active: {
                opacity: '0.5',
            },
        },
    }),
};

export default function CalendarPage() {
    const queryClient = useQueryClient();
    const [currentDate, setCurrentDate] = useState(new Date());

    // Drag State
    const [activeId, setActiveId] = useState<string | null>(null);
    const [activeDragItem, setActiveDragItem] = useState<any>(null); // MediaAsset or ScheduledPost

    // Composer State
    const [isComposerOpen, setIsComposerOpen] = useState(false);
    const [composerData, setComposerData] = useState<Partial<ScheduledPost> | undefined>(undefined);
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(true);
    }, []);

    // 1. Fetch Posts
    const { data: posts = [] } = useQuery({
        queryKey: ['scheduled-posts', currentDate.toISOString().slice(0, 7)], // Key by Month
        queryFn: async () => {
            // Fetch +/- 1 month to cover grid edges if needed, for now just current month approx
            const start = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1).toISOString();
            const end = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).toISOString();
            return schedulerApi.getScheduledPosts(start, end);
        }
    });

    // 2. Mutations
    const createPostMutation = useMutation({
        mutationFn: schedulerApi.createPost,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['scheduled-posts'] });
            toast.success("Post scheduled!");
            setIsComposerOpen(false);
        },
        onError: () => toast.error("Failed to schedule post")
    });

    const updatePostMutation = useMutation({
        mutationFn: ({ id, data }: { id: string, data: UpdatePostRequest }) => schedulerApi.updatePost(id, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['scheduled-posts'] });
            toast.success("Post updated!");
            setIsComposerOpen(false);
        },
        onError: () => toast.error("Failed to update post")
    });

    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
                distance: 8,
            },
        })
    );

    const handleDragStart = (event: DragStartEvent) => {
        const { active } = event;
        setActiveId(active.id as string);
        setActiveDragItem(active.data.current);
    };

    const handleDragEnd = async (event: DragEndEvent) => {
        const { active, over } = event;
        setActiveId(null);
        setActiveDragItem(null);

        if (!over) return;

        const dropDate = new Date(over.id as string); // Droppable ID is ISO date string

        // Ensure dropDate has a valid time (e.g., maintain 9 AM default or current time if today)
        dropDate.setHours(10, 0, 0, 0);

        const dragData = active.data.current;

        // SCENARIO 1: New Post (Media dropped on Day)
        if (dragData?.type === 'media') {
            const media = dragData.media as MediaAsset;

            // Open Composer with pre-filled data
            setComposerData({
                mediaUrl: media.url,
                mediaType: media.type,
                scheduledAt: dropDate.toISOString(),
                platforms: ['instagram'], // Default
            });
            setIsComposerOpen(true);
        }

        // SCENARIO 2: Rescheduling (Post dropped on Day)
        else if (dragData?.type === 'post') {
            const post = dragData.post as ScheduledPost;

            // Call API directly for rescheduling
            updatePostMutation.mutate({
                id: post.id,
                data: { scheduledAt: dropDate.toISOString() }
            });
        }
    };

    const handleSavePost = async (data: any) => {
        if (data.id) {
            updatePostMutation.mutate({ id: data.id, data });
        } else {
            createPostMutation.mutate(data);
        }
    };

    return (
        <DndContext
            sensors={sensors}
            onDragStart={handleDragStart}
            onDragEnd={handleDragEnd}
        >
            <div className="flex h-screen overflow-hidden">
                <MediaSidebar />

                <div className="flex-1 overflow-hidden">
                    <SchedulerCalendar
                        currentDate={currentDate}
                        posts={posts}
                    />
                </div>

                <PostComposer
                    isOpen={isComposerOpen}
                    onClose={() => setIsComposerOpen(false)}
                    initialData={composerData}
                    onSave={handleSavePost}
                />

                {/* Drag Overlay Portal */}
                {mounted && createPortal(
                    <DragOverlay dropAnimation={dropAnimation}>
                        {activeId && activeDragItem?.type === 'media' ? (
                            <div className="h-24 w-24 overflow-hidden rounded-md border-2 border-primary shadow-xl">
                                <Image
                                    src={activeDragItem.media.url}
                                    alt="Dragging"
                                    width={96}
                                    height={96}
                                    className="h-full w-full object-cover"
                                />
                            </div>
                        ) : activeId && activeDragItem?.type === 'post' ? (
                            <div className="w-[180px]">
                                <ScheduledPostCard post={activeDragItem.post} />
                            </div>
                        ) : null}
                    </DragOverlay>,
                    document.body
                )}
            </div>
        </DndContext>
    );
}
