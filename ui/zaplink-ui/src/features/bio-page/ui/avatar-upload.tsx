"use client"

import { useState, useRef } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Upload, X, Camera } from "lucide-react";
import { handleApiError, showSuccessToast } from "@/lib/error-handler";

interface AvatarUploadProps {
  currentAvatar?: string;
  onAvatarChange: (avatarUrl: string) => void;
  className?: string;
}

export function AvatarUpload({ currentAvatar, onAvatarChange, className }: AvatarUploadProps) {
  const [isUploading, setIsUploading] = useState(false);
  const [previewUrl, setPreviewUrl] = useState<string>(currentAvatar || "");
  const fileInputRef = useRef<HTMLInputElement>(null);

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

    // Create preview
    const reader = new FileReader();
    reader.onload = (e) => {
      setPreviewUrl(e.target?.result as string);
    };
    reader.readAsDataURL(file);

    // Upload file
    await uploadAvatar(file);
  };

  const uploadAvatar = async (file: File) => {
    setIsUploading(true);
    try {
      const formData = new FormData();
      formData.append('avatar', file);

      // Replace with your actual upload endpoint
      const response = await fetch('/api/upload/avatar', {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        throw new Error('Upload failed');
      }

      const data = await response.json();
      onAvatarChange(data.url);
      showSuccessToast('Avatar uploaded successfully');
    } catch (error) {
      handleApiError(error, 'Failed to upload avatar');
      // Reset preview on error
      setPreviewUrl(currentAvatar || "");
    } finally {
      setIsUploading(false);
    }
  };

  const handleRemoveAvatar = () => {
    setPreviewUrl("");
    onAvatarChange("");
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleUrlInput = (url: string) => {
    setPreviewUrl(url);
    onAvatarChange(url);
  };

  return (
    <Card className={className}>
      <CardHeader>
        <CardTitle className="text-base">Profile Avatar</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {/* Preview */}
        <div className="flex justify-center">
          <div className="relative">
            {previewUrl ? (
              <img
                src={previewUrl}
                alt="Avatar preview"
                className="w-24 h-24 rounded-full object-cover border-4 border-background shadow-lg"
              />
            ) : (
              <div className="w-24 h-24 rounded-full bg-muted flex items-center justify-center border-2 border-dashed border-muted-foreground">
                <Camera className="w-8 h-8 text-muted-foreground" />
              </div>
            )}
            
            {previewUrl && (
              <Button
                type="button"
                variant="destructive"
                size="sm"
                className="absolute -top-2 -right-2 h-6 w-6 rounded-full p-0"
                onClick={handleRemoveAvatar}
              >
                <X className="h-3 w-3" />
              </Button>
            )}
          </div>
        </div>

        {/* Upload Button */}
        <div className="space-y-2">
          <input
            ref={fileInputRef}
            type="file"
            accept="image/*"
            onChange={handleFileSelect}
            className="hidden"
            id="avatar-upload"
          />
          <Button
            type="button"
            variant="outline"
            className="w-full"
            onClick={() => fileInputRef.current?.click()}
            disabled={isUploading}
          >
            <Upload className="w-4 h-4 mr-2" />
            {isUploading ? "Uploading..." : "Upload Image"}
          </Button>
        </div>

        {/* URL Input */}
        <div className="space-y-2">
          <Label htmlFor="avatar-url">Or enter image URL</Label>
          <Input
            id="avatar-url"
            type="url"
            placeholder="https://example.com/avatar.jpg"
            value={previewUrl}
            onChange={(e) => handleUrlInput(e.target.value)}
          />
        </div>

        {/* Help Text */}
        <div className="text-xs text-muted-foreground space-y-1">
          <p>• Upload: JPG, PNG, GIF up to 5MB</p>
          <p>• Recommended: 400x400px square image</p>
          <p>• URL must be publicly accessible</p>
        </div>
      </CardContent>
    </Card>
  );
}
