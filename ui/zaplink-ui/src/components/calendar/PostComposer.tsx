'use client';

import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { ScheduledPost, CreatePostRequest, UpdatePostRequest } from '@/lib/api/scheduler';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Input } from '@/components/ui/input';
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetFooter, SheetDescription } from '@/components/ui/sheet';
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group';
import { FaInstagram, FaLinkedin, FaTwitter, FaFacebook, FaTiktok } from "react-icons/fa";
import { format, isPast } from 'date-fns';
import Image from 'next/image';
import { AlertCircle } from 'lucide-react';
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";

interface PostComposerProps {
    isOpen: boolean;
    onClose: () => void;
    initialData?: Partial<ScheduledPost>;
    onSave: (data: CreatePostRequest | UpdatePostRequest) => void;
}

const PLATFORM_LIMITS = {
    twitter: 280,
    linkedin: 3000,
    instagram: 2200,
    facebook: 63206,
    tiktok: 2200
};

export const PostComposer = ({ isOpen, onClose, initialData, onSave }: PostComposerProps) => {
    const { register, handleSubmit, setValue, watch, reset, formState: { errors } } = useForm<Partial<ScheduledPost>>({
        defaultValues: {
            platforms: [],
            caption: '',
            status: 'SCHEDULED',
            ...initialData
        }
    });

    const [aspectRatioWarning, setAspectRatioWarning] = useState<string | null>(null);

    // Reset form when initialData changes or modal opens
    useEffect(() => {
        if (isOpen && initialData) {
            reset({
                ...initialData,
                scheduledAt: initialData.scheduledAt
            });
            // Check Aspect Ratio (Mock logic for now)
            if (initialData.mediaType === 'image') {
                // In real app, we would check naturalWidth / naturalHeight
                // Here we just simulate a warning for demonstration if needed
            }
        }
    }, [isOpen, initialData, reset]);

    const platforms = watch("platforms") || [];
    const caption = watch("caption") || "";
    const scheduledAt = watch("scheduledAt");

    // Validation Logic
    const getValidationErrors = () => {
        const errs: string[] = [];
        platforms.forEach(p => {
            const limit = PLATFORM_LIMITS[p as keyof typeof PLATFORM_LIMITS];
            if (caption.length > limit) {
                errs.push(`${p} limit exceeded (${caption.length}/${limit})`);
            }
        });
        return errs;
    };

    const validationErrors = getValidationErrors();
    const isDateInvalid = scheduledAt && isPast(new Date(scheduledAt));

    const onSubmit = (data: Partial<ScheduledPost>, e?: React.BaseSyntheticEvent) => {
        // Determine if Save or Draft based on button clicked (we can track via state or multiple submit handlers)
        // For simplicity, we'll assume the status is set by the button before submit

        // Formatting for API
        const payload: any = {
            ...initialData,
            ...data,
            // API expects ISO string. If local input is used, ensure it is converted correctly.
            scheduledAt: data.scheduledAt ? new Date(data.scheduledAt).toISOString() : new Date().toISOString(),
        };
        onSave(payload);
        onClose();
    };

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

                <form className="mt-6 flex flex-col gap-6">
                    {/* Media Preview */}
                    {initialData?.mediaUrl && (
                        <div className="relative aspect-video w-full overflow-hidden rounded-md border bg-muted group">
                            <Image
                                src={initialData.mediaUrl}
                                fill
                                className="object-contain"
                                alt="Post media"
                            />
                            {/* Aspect Ratio Warning Placeholder */}
                            {aspectRatioWarning && (
                                <div className="absolute bottom-2 left-2 right-2 bg-yellow-100 text-yellow-800 text-xs p-2 rounded flex items-center gap-2 opacity-90">
                                    <AlertCircle size={14} />
                                    {aspectRatioWarning}
                                </div>
                            )}
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
                            className="justify-start flex-wrap gap-2"
                        >
                            <ToggleGroupItem value="instagram" aria-label="Toggle Instagram" className="gap-2 data-[state=on]:bg-pink-100 data-[state=on]:text-pink-700">
                                <FaInstagram /> Instagram
                            </ToggleGroupItem>
                            <ToggleGroupItem value="linkedin" aria-label="Toggle LinkedIn" className="gap-2 data-[state=on]:bg-blue-100 data-[state=on]:text-blue-700">
                                <FaLinkedin /> LinkedIn
                            </ToggleGroupItem>
                            <ToggleGroupItem value="twitter" aria-label="Toggle Twitter" className="gap-2 data-[state=on]:bg-sky-100 data-[state=on]:text-sky-700">
                                <FaTwitter /> Twitter
                            </ToggleGroupItem>
                            <ToggleGroupItem value="facebook" aria-label="Toggle Facebook" className="gap-2 data-[state=on]:bg-blue-100 data-[state=on]:text-blue-800">
                                <FaFacebook /> Facebook
                            </ToggleGroupItem>
                            <ToggleGroupItem value="tiktok" aria-label="Toggle TikTok" className="gap-2 data-[state=on]:bg-gray-100 data-[state=on]:text-black">
                                <FaTiktok /> TikTok
                            </ToggleGroupItem>
                        </ToggleGroup>
                        {platforms.length === 0 && <p className="text-xs text-destructive">Select at least one platform.</p>}
                    </div>

                    {/* Caption */}
                    <div className="space-y-2">
                        <div className="flex justify-between">
                            <Label>Caption</Label>
                            <span className="text-xs text-muted-foreground">{caption.length} chars</span>
                        </div>
                        <Textarea
                            placeholder="Write an amazing caption..."
                            className="min-h-[150px]"
                            {...register("caption")}
                        />
                        {validationErrors.length > 0 && (
                            <div className="flex flex-col gap-1">
                                {validationErrors.map((err, i) => (
                                    <p key={i} className="text-xs text-destructive">{err}</p>
                                ))}
                            </div>
                        )}
                    </div>

                    {/* Schedule Time */}
                    <div className="space-y-2">
                        <Label>Scheduled Time</Label>
                        <Input
                            type="datetime-local"
                            {...register("scheduledAt")}
                            className={isDateInvalid ? "border-destructive" : ""}
                        />
                        {isDateInvalid && <p className="text-xs text-destructive">Cannot schedule in the past.</p>}
                    </div>

                    <SheetFooter className="flex-col sm:flex-row gap-2 sm:gap-0">
                        <Button
                            type="button"
                            variant="outline"
                            onClick={() => {
                                setValue("status", "DRAFT");
                                handleSubmit(onSubmit)();
                            }}
                        >
                            Save as Draft
                        </Button>
                        <Button
                            type="button"
                            disabled={platforms.length === 0 || validationErrors.length > 0 || !!isDateInvalid}
                            onClick={() => {
                                setValue("status", "SCHEDULED");
                                handleSubmit(onSubmit)();
                            }}
                        >
                            Schedule Post
                        </Button>
                    </SheetFooter>
                </form>
            </SheetContent>
        </Sheet>
    );
};
