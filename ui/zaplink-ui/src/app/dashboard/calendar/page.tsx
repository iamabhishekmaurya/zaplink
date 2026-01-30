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
import { ScheduledPost, schedulerApi, MediaAsset } from '@/lib/api/scheduler-mock';
import { createPortal } from 'react-dom';
import Image from 'next/image';
import { ScheduledPostCard } from '@/components/calendar/ScheduledPostCard';

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
    const [posts, setPosts] = useState<ScheduledPost[]>([]);
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

    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
                distance: 8,
            },
        })
    );

    useEffect(() => {
        // Initial fetch
        schedulerApi.fetchScheduledPosts(new Date(), new Date()).then(setPosts);
    }, []);

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
        const dragData = active.data.current;

        // SCENARIO 1: New Post (Media dropped on Day)
        if (dragData?.type === 'media') {
            const media = dragData.media as MediaAsset;

            // Open Composer with pre-filled data
            setComposerData({
                mediaUrl: media.url,
                mediaType: media.type,
                scheduledAt: dropDate,
                platforms: ['instagram'], // Default
            });
            setIsComposerOpen(true);
        }

        // SCENARIO 2: Rescheduling (Post dropped on Day)
        else if (dragData?.type === 'post') {
            const post = dragData.post as ScheduledPost;

            // Optimistic Update
            const updatedPost = { ...post, scheduledAt: dropDate };
            setPosts(prev => prev.map(p => p.id === post.id ? updatedPost : p));

            try {
                await schedulerApi.updatePost(post.id, { scheduledAt: dropDate });
            } catch (error) {
                console.error("Failed to reschedule", error);
                // Revert on failure
                setPosts(prev => prev.map(p => p.id === post.id ? post : p));
            }
        }
    };

    const handleSavePost = async (data: Partial<ScheduledPost>) => {
        if (data.id) {
            // Update existing
            const updated = await schedulerApi.updatePost(data.id, data);
            setPosts(prev => prev.map(p => p.id === data.id ? updated : p));
        } else {
            // Create new
            const newPost = await schedulerApi.createPost(data as any); // Cast for mock
            setPosts(prev => [...prev, newPost]);
        }
        setIsComposerOpen(false);
    };

    return (
        <DndContext
            sensors={sensors}
            onDragStart={handleDragStart}
            onDragEnd={handleDragEnd}
        >
            <div className="flex h-screen overflow-hidden">
                {/* Helper to verify DndContext is mounted */}
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
