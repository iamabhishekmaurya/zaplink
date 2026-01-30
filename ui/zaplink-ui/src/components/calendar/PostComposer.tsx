'use client';

import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { ScheduledPost } from '@/lib/api/scheduler-mock';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Input } from '@/components/ui/input';
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetFooter, SheetDescription } from '@/components/ui/sheet';
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group';
import { FaInstagram, FaLinkedin, FaTwitter, FaFacebook } from "react-icons/fa";
import { format } from 'date-fns';
import Image from 'next/image';

interface PostComposerProps {
    isOpen: boolean;
    onClose: () => void;
    initialData?: Partial<ScheduledPost>;
    onSave: (post: Partial<ScheduledPost>) => void;
}

export const PostComposer = ({ isOpen, onClose, initialData, onSave }: PostComposerProps) => {
    const { register, handleSubmit, setValue, watch, reset } = useForm<Partial<ScheduledPost>>({
        defaultValues: {
            platforms: [],
            caption: '',
            ...initialData
        }
    });

    // Reset form when initialData changes or modal opens
    useEffect(() => {
        if (isOpen && initialData) {
            reset({
                ...initialData,
                scheduledAt: initialData.scheduledAt ? new Date(initialData.scheduledAt) : undefined
            });
        }
    }, [isOpen, initialData, reset]);

    const onSubmit = (data: Partial<ScheduledPost>) => {
        onSave({
            ...initialData,
            ...data,
            scheduledAt: data.scheduledAt ? new Date(data.scheduledAt) : new Date(),
        });
        onClose();
    };

    const platforms = watch("platforms") || [];

    return (
        <Sheet open={isOpen} onOpenChange={onClose}>
            <SheetContent className="w-[400px] sm:w-[540px] overflow-y-auto">
                <SheetHeader>
                    <SheetTitle>{initialData?.id ? 'Edit Post' : 'Schedule Post'}</SheetTitle>
                    <SheetDescription>
                        {initialData?.scheduledAt ?
                            `Scheduled for ${format(new Date(initialData.scheduledAt), 'MMMM do, yyyy h:mm a')}` : 'Create a new post'}
                    </SheetDescription>
                </SheetHeader>

                <form onSubmit={handleSubmit(onSubmit)} className="mt-6 flex flex-col gap-6">
                    {/* Media Preview */}
                    {initialData?.mediaUrl && (
                        <div className="relative aspect-video w-full overflow-hidden rounded-md border bg-muted">
                            <Image
                                src={initialData.mediaUrl}
                                fill
                                className="object-contain"
                                alt="Post media"
                            />
                        </div>
                    )}

                    {/* Social Channels */}
                    <div className="space-y-2">
                        <Label>Select Channels</Label>
                        <ToggleGroup
                            type="multiple"
                            value={platforms}
                            onValueChange={(val) => {
                                setValue("platforms", val as any);
                            }}
                            className="justify-start"
                        >
                            <ToggleGroupItem value="instagram" aria-label="Toggle Instagram">
                                <FaInstagram className="h-4 w-4 text-pink-600 mr-2" /> Instagram
                            </ToggleGroupItem>
                            <ToggleGroupItem value="linkedin" aria-label="Toggle LinkedIn">
                                <FaLinkedin className="h-4 w-4 text-blue-700 mr-2" /> LinkedIn
                            </ToggleGroupItem>
                            <ToggleGroupItem value="twitter" aria-label="Toggle Twitter">
                                <FaTwitter className="h-4 w-4 text-sky-500 mr-2" /> Twitter
                            </ToggleGroupItem>
                        </ToggleGroup>
                    </div>

                    {/* Caption */}
                    <div className="space-y-2">
                        <Label>Caption</Label>
                        <Textarea
                            placeholder="Write an amazing caption..."
                            className="min-h-[150px]"
                            {...register("caption")}
                        />
                    </div>

                    {/* Time & Date Overrides could go here */}

                    <SheetFooter>
                        <Button type="button" variant="outline" onClick={onClose}>Cancel</Button>
                        <Button type="submit">Save Post</Button>
                    </SheetFooter>
                </form>
            </SheetContent>
        </Sheet>
    );
};
