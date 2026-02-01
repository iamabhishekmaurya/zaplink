"use client"

import { Button } from '@/components/ui/button';
import { useUploadAsset } from '@/hooks/useMedia';
import { UploadCloud } from 'lucide-react';
import { useRef } from 'react';
import { toast } from 'sonner';

interface UploadZoneProps {
    ownerId: string;
    folderId?: string;
}

export function UploadZone({ ownerId, folderId }: UploadZoneProps) {
    const inputRef = useRef<HTMLInputElement>(null);
    const uploadAsset = useUploadAsset();

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const toastId = toast.loading(`Uploading ${file.name}...`);

        try {
            await uploadAsset.mutateAsync({ file, ownerId, folderId });
            toast.success("Upload successful", { id: toastId });
        } catch (error) {
            console.error(error);
            toast.error("Upload failed", { id: toastId });
        } finally {
            // Reset input
            if (inputRef.current) inputRef.current.value = "";
        }
    };

    return (
        <div>
            <input
                type="file"
                className="hidden"
                ref={inputRef}
                onChange={handleFileChange}
            />
            <Button onClick={() => inputRef.current?.click()}>
                <UploadCloud className="mr-2 h-4 w-4" /> Upload File
            </Button>
        </div>
    );
}
