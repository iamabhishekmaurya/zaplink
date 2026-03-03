"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { handleApiError, showSuccessToast } from "@/lib/error-handler";
import { cn } from "@/lib/utils";
import { Camera, Loader2, Upload, X } from "lucide-react";
import { useRef, useState } from "react";
import { ContentApi } from "@/services/contentApi";

interface AvatarUploadProps {
  currentAvatar?: string;
  onAvatarChange: (avatarUrl: string) => void;
  className?: string;
}

export function AvatarUpload({ currentAvatar, onAvatarChange, className }: AvatarUploadProps) {
  const [isUploading, setIsUploading] = useState(false);
  // localPreview is ONLY for the image circle display (can be base64 during upload)
  const [localPreview, setLocalPreview] = useState<string>("");
  // serverUrl is the actual URL from the server that gets saved
  const [serverUrl, setServerUrl] = useState<string>(currentAvatar || "");
  const fileInputRef = useRef<HTMLInputElement>(null);

  // The display URL for the avatar circle: show local preview if uploading, otherwise server URL
  const displayUrl = localPreview || serverUrl;

  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith('image/')) {
      handleApiError({ message: 'Please select an image file' }, 'Avatar upload');
      return;
    }

    // Validate file size (5MB max)
    if (file.size > 5 * 1024 * 1024) {
      handleApiError({ message: 'Image size must be less than 5MB' }, 'Avatar upload');
      return;
    }

    // Create local preview (base64) for instant visual feedback only
    const reader = new FileReader();
    reader.onload = (e) => {
      setLocalPreview(e.target?.result as string);
    };
    reader.readAsDataURL(file);

    // Upload file to media service
    await uploadAvatar(file);
  };

  const uploadAvatar = async (file: File) => {
    setIsUploading(true);
    try {
      const response = await ContentApi.uploadMedia(file);
      const data = response.data;
      // The media service returns the uploaded item with a url field
      const uploadedUrl = data.url || data.fileUrl || data.thumbnailUrl || "";
      setServerUrl(uploadedUrl);
      setLocalPreview(""); // Clear local preview, use server URL now
      onAvatarChange(uploadedUrl);
      showSuccessToast('Avatar uploaded successfully');
    } catch (error) {
      handleApiError(error, 'Failed to upload avatar');
      // Reset local preview on error
      setLocalPreview("");
    } finally {
      setIsUploading(false);
    }
  };

  const handleRemoveAvatar = () => {
    setLocalPreview("");
    setServerUrl("");
    onAvatarChange("");
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleUrlInput = (url: string) => {
    setServerUrl(url);
    setLocalPreview("");
    onAvatarChange(url);
  };

  return (
    <div className={cn("flex flex-col sm:flex-row gap-6 sm:gap-8 items-start sm:items-center", className)}>
      {/* Avatar Circle */}
      <div className="relative group cursor-pointer shrink-0" onClick={() => !isUploading && fileInputRef.current?.click()}>
        {/* Glowing backdrop effect on hover */}
        <div className="absolute -inset-0.5 bg-gradient-to-tr from-primary to-primary/30 rounded-full blur-md opacity-0 transition duration-500"></div>

        <div className="relative w-28 h-28 sm:w-32 sm:h-32 rounded-full overflow-hidden border-4 border-background bg-muted/50 flex items-center justify-center shadow-lg transition-transform duration-300">
          {displayUrl ? (
            <img src={displayUrl} className="w-full h-full object-cover" alt="Profile" />
          ) : (
            <Camera className="w-10 h-10 text-muted-foreground/50 group-hover:text-primary transition-colors duration-300" />
          )}

          {/* Upload Overlay */}
          <div className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 flex flex-col items-center justify-center transition-opacity duration-300">
            {isUploading ? (
              <Loader2 className="w-6 h-6 text-white animate-spin" />
            ) : (
              <>
                <Upload className="w-6 h-6 text-white mb-1" />
                <span className="text-[10px] font-bold text-white uppercase tracking-widest">{displayUrl ? 'Change' : 'Upload'}</span>
              </>
            )}
          </div>
        </div>

        {/* Hidden File Input */}
        <input
          ref={fileInputRef}
          type="file"
          accept="image/*"
          onChange={handleFileSelect}
          className="hidden"
          id="avatar-upload"
        />
      </div>

      {/* Right side controls */}
      <div className="flex-1 space-y-4 w-full">
        <div>
          <h4 className="text-base font-semibold text-foreground tracking-tight">Profile Picture</h4>
          <p className="text-xs text-muted-foreground mt-1 max-w-sm leading-relaxed">Recommended: Square image, 400x400px. Supports JPG, PNG, or GIF up to 5MB.</p>
        </div>

        <div className="flex gap-2 items-center max-w-md">
          <div className="relative flex-1">
            <Input
              placeholder="Or paste an image URL..."
              value={serverUrl}
              onChange={(e) => handleUrlInput(e.target.value)}
              disabled={isUploading}
              className="h-10 text-sm bg-background/40 hover:bg-background/80 border-border/50 focus:bg-background rounded-lg transition-all"
            />
          </div>
          {displayUrl && (
            <Button
              variant="ghost"
              className="h-10 px-3 text-destructive hover:bg-destructive/10 hover:text-destructive rounded-lg transition-colors"
              onClick={handleRemoveAvatar}
              disabled={isUploading}
            >
              <X className="w-4 h-4 mr-1 lg:mr-2" />
              <span className="hidden lg:inline">Remove</span>
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
