'use client';

import React from 'react';
import { useDroppable } from '@dnd-kit/core';
import { ScheduledPost } from '@/lib/api/scheduler-mock';
import { ScheduledPostCard } from './ScheduledPostCard';
import { cn } from '@/lib/utils';
import { format, startOfWeek, endOfWeek, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isSameDay, isToday } from 'date-fns';

interface SchedulerCalendarProps {
    currentDate: Date;
    posts: ScheduledPost[];
}

export const SchedulerCalendar = ({ currentDate, posts }: SchedulerCalendarProps) => {
    const monthStart = startOfMonth(currentDate);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart);
    const endDate = endOfWeek(monthEnd);

    const days = eachDayOfInterval({
        start: startDate,
        end: endDate,
    });

    const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    return (
        <div className="flex h-full flex-col bg-background">
            {/* Header */}
            <div className="flex items-center justify-between border-b p-4">
                <h2 className="text-xl font-semibold">
                    {format(currentDate, 'MMMM yyyy')}
                </h2>
                {/* Navigation buttons could go here */}
            </div>

            {/* Days Header */}
            <div className="grid grid-cols-7 border-b bg-muted/40 text-center text-sm font-medium text-muted-foreground">
                {weekDays.map((day) => (
                    <div key={day} className="py-2">
                        {day}
                    </div>
                ))}
            </div>

            {/* Calendar Grid */}
            <div className="flex-1 grid grid-cols-7 grid-rows-5 lg:grid-rows-6">
                {days.map((day, dayIdx) => (
                    <CalendarDay
                        key={day.toISOString()}
                        day={day}
                        currentMonth={monthStart}
                        posts={posts.filter((post) => isSameDay(new Date(post.scheduledAt), day))}
                    />
                ))}
            </div>
        </div>
    );
};

interface CalendarDayProps {
    day: Date;
    currentMonth: Date;
    posts: ScheduledPost[];
}

const CalendarDay = ({ day, currentMonth, posts }: CalendarDayProps) => {
    const { setNodeRef, isOver } = useDroppable({
        id: day.toISOString(), // The ID is the Date string, which makes it easy to parse on drop
        data: { date: day },
    });

    return (
        <div
            ref={setNodeRef}
            className={cn(
                "relative min-h-[100px] flex flex-col border-b border-r p-2 transition-colors",
                !isSameMonth(day, currentMonth) && "bg-muted/10 text-muted-foreground",
                isOver && "bg-primary/10 ring-2 ring-inset ring-primary",
                isToday(day) && "bg-accent/5"
            )}
        >
            <span
                className={cn(
                    "mb-1 flex h-6 w-6 items-center justify-center text-xs rounded-full font-medium ml-auto",
                    isToday(day) && "bg-primary text-primary-foreground"
                )}
            >
                {format(day, 'd')}
            </span>

            <div className="flex flex-col gap-1 overflow-y-auto max-h-[120px]">
                {posts.map((post) => (
                    <ScheduledPostCard key={post.id} post={post} />
                ))}
            </div>
        </div>
    );
};
