import { useState, useRef } from 'react';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { UploadCloud, X } from 'lucide-react';
import { ContentApi } from '@/services/contentApi';
import { toast } from 'sonner';
import { Progress } from '@/components/ui/progress';

interface UploadMediaDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    folderId?: string;
    folderPath?: { id: string, name: string }[];
    onSuccess: () => void;
}

export function UploadMediaDialog({ open, onOpenChange, folderId, folderPath = [], onSuccess }: UploadMediaDialogProps) {
    const [isUploading, setIsUploading] = useState(false);
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [uploadProgress, setUploadProgress] = useState(0);
    const [isDragging, setIsDragging] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setSelectedFiles(prev => [...prev, ...Array.from(e.target.files!)]);
        }
    };

    const handleRemoveFile = (index: number) => {
        setSelectedFiles(prev => prev.filter((_, i) => i !== index));
    };

    const handleDragOver = (e: React.DragEvent) => {
        e.preventDefault();
        setIsDragging(true);
    };

    const handleDragLeave = (e: React.DragEvent) => {
        e.preventDefault();
        setIsDragging(false);
    };

    const handleDrop = (e: React.DragEvent) => {
        e.preventDefault();
        setIsDragging(false);
        if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            setSelectedFiles(prev => [...prev, ...Array.from(e.dataTransfer.files)]);
        }
    };

    const handleUpload = async () => {
        if (selectedFiles.length === 0) return;

        setIsUploading(true);
        setUploadProgress(0);

        let uploadedCount = 0;
        try {
            for (let i = 0; i < selectedFiles.length; i++) {
                // Show progressive update before request finishes
                setUploadProgress(((i) / selectedFiles.length) * 100 + 5);
                await ContentApi.uploadMedia(selectedFiles[i], folderId);
                uploadedCount++;
                setUploadProgress((uploadedCount / selectedFiles.length) * 100);
            }

            toast.success(`Successfully uploaded ${uploadedCount} file(s)`);

            setTimeout(() => {
                resetAndClose();
                onSuccess();
            }, 500);

        } catch (error) {
            console.error("Failed to upload some files", error);
            toast.error(`Uploaded ${uploadedCount}/${selectedFiles.length} files. Some failed.`);
            setIsUploading(false);
        }
    };

    const resetAndClose = () => {
        setSelectedFiles([]);
        setUploadProgress(0);
        setIsUploading(false);
        setIsDragging(false);
        if (fileInputRef.current) fileInputRef.current.value = "";
        onOpenChange(false);
    };

    return (
        <Dialog open={open} onOpenChange={(val) => {
            if (!val && !isUploading) resetAndClose();
            if (val) onOpenChange(val);
        }}>
            <DialogContent className="sm:max-w-2xl overflow-hidden flex flex-col max-h-[90vh]">
                <DialogHeader>
                    <DialogTitle className="text-2xl">Upload Media</DialogTitle>
                    <DialogDescription className="flex items-center gap-1.5 flex-wrap">
                        <span>Uploading to:</span>
                        <span className="font-medium text-foreground bg-muted/50 px-2 py-0.5 rounded text-xs flex items-center gap-1 max-w-full overflow-hidden">
                            <span className="shrink-0 text-muted-foreground">Root</span>
                            {folderPath.map((folder, i) => (
                                <span key={folder.id} className="flex items-center gap-1 shrink-0">
                                    <span className="text-muted-foreground/50 text-[10px] mx-1">/</span>
                                    <span className="truncate max-w-[120px]">{folder.name}</span>
                                </span>
                            ))}
                        </span>
                    </DialogDescription>
                </DialogHeader>

                <div className="py-2 flex flex-col gap-4 overflow-hidden flex-1">
                    <div
                        className={`border-2 border-dashed rounded-xl p-8 transition-all flex flex-col items-center justify-center cursor-pointer ${isDragging ? 'border-primary bg-primary/10 scale-[1.02]' : 'border-border/50 hover:bg-muted/20'
                            }`}
                        onClick={() => fileInputRef.current?.click()}
                        onDragOver={handleDragOver}
                        onDragLeave={handleDragLeave}
                        onDrop={handleDrop}
                    >
                        <div className="bg-primary/10 p-4 rounded-full mb-4">
                            <UploadCloud className="w-8 h-8 text-primary" />
                        </div>
                        <h3 className="text-base font-semibold mb-1">Click or drag to browse</h3>
                        <p className="text-sm text-muted-foreground text-center max-w-xs">
                            Drop images, PDFs, or documents here. Max 10MB per file.
                        </p>
                    </div>

                    {selectedFiles.length > 0 && (
                        <div className="flex flex-col gap-2 overflow-y-auto pr-2 min-h-0 flex-1 no-scrollbar pb-2">
                            <div className="flex justify-between items-center mb-1 sticky top-0 bg-background z-10 py-1">
                                <span className="text-sm font-medium text-foreground">
                                    Selected Files ({selectedFiles.length})
                                </span>
                                {!isUploading && (
                                    <Button variant="ghost" size="sm" onClick={() => setSelectedFiles([])} className="h-8 text-xs hover:bg-destructive/10 hover:text-destructive">
                                        Clear All
                                    </Button>
                                )}
                            </div>
                            {selectedFiles.map((file, idx) => (
                                <div key={`${file.name}-${idx}`} className="border border-border/50 rounded-lg p-3 bg-muted/20 w-full relative group transition-colors hover:border-border">
                                    {!isUploading && (
                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            className="absolute top-1/2 -translate-y-1/2 right-2 h-8 w-8 rounded-full opacity-0 group-hover:opacity-100 transition-opacity bg-background hover:bg-destructive hover:text-destructive-foreground z-10"
                                            onClick={(e) => { e.stopPropagation(); handleRemoveFile(idx); }}
                                        >
                                            <X className="w-4 h-4" />
                                        </Button>
                                    )}
                                    <div className="grid grid-cols-[auto_1fr] items-center gap-3 pr-8 w-full min-w-0">
                                        <div className="p-2 bg-primary/10 rounded flex-shrink-0">
                                            <UploadCloud className="w-5 h-5 text-primary" />
                                        </div>
                                        <div className="min-w-0 overflow-hidden">
                                            <p className="text-sm font-medium truncate" title={file.name}>{file.name}</p>
                                            <p className="text-xs text-muted-foreground mt-0.5">{(file.size / 1024 / 1024).toFixed(2)} MB</p>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}

                    {isUploading && (
                        <div className="w-full mt-2 bg-muted/30 p-4 rounded-lg">
                            <Progress value={uploadProgress} className="h-2 w-full mb-2" />
                            <p className="text-xs text-center text-muted-foreground font-medium">Uploading... {Math.round(uploadProgress)}%</p>
                        </div>
                    )}

                    <input
                        type="file"
                        className="hidden"
                        ref={fileInputRef}
                        onChange={handleFileSelect}
                        accept="image/*,.pdf,.doc,.docx"
                        multiple
                    />
                </div>

                <DialogFooter className="bg-muted/20 -mx-6 -mb-6 p-4 sm:p-6 sm:justify-between items-center rounded-b-lg border-t border-border/50 mt-4">
                    <p className="text-sm text-muted-foreground hidden sm:block font-medium">
                        {selectedFiles.length > 0 ? `${selectedFiles.length} file(s) ready` : "No files selected"}
                    </p>
                    <div className="flex gap-3 w-full sm:w-auto">
                        <Button type="button" variant="outline" onClick={resetAndClose} disabled={isUploading} className="flex-1 sm:flex-none">
                            Cancel
                        </Button>
                        <Button type="button" onClick={handleUpload} disabled={selectedFiles.length === 0 || isUploading} className="flex-1 sm:flex-none px-8">
                            {isUploading ? "Uploading..." : `Upload ${selectedFiles.length > 0 ? selectedFiles.length + ' ' : ''}Files`}
                        </Button>
                    </div>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
