"use client";

import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { AvatarUpload } from "@/features/bio-page/ui/avatar-upload";
import { BioPage } from "@/services/bioPageService";
import { motion } from "framer-motion";
import { UserCircle } from "lucide-react";

interface ProfileTabProps {
    data: BioPage;
    updateField: (field: keyof BioPage, value: any) => void;
}

export function ProfileTab({ data, updateField }: ProfileTabProps) {
    return (
        <div className="space-y-6 pb-20">
            {/* Header */}
            <motion.div
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                className="flex items-center gap-3 pb-4 border-b"
            >
                <div className="p-2 bg-primary/10 rounded-xl">
                    <UserCircle className="w-5 h-5 text-primary" />
                </div>
                <div>
                    <h3 className="text-lg font-semibold text-foreground">
                        Profile Information
                    </h3>
                    <p className="text-sm text-muted-foreground">
                        Manage your public profile details
                    </p>
                </div>
            </motion.div>

            {/* Content */}
            <motion.div
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                className="space-y-5 border rounded-xl p-5 bg-card shadow-sm"
            >
                <div className="grid gap-5">
                    <div className="space-y-2.5">
                        <Label htmlFor="title" className="text-sm font-medium text-foreground/80 flex items-center gap-2">
                            Page Title
                            <span className="text-xs text-muted-foreground">(Shown at top of your page)</span>
                        </Label>
                        <Input
                            id="title"
                            value={data.title || ''}
                            onChange={(e) => updateField('title', e.target.value)}
                            placeholder="@username or Your Name"
                            className="h-11 transition-all focus:ring-2 focus:ring-primary/20"
                        />
                    </div>

                    <div className="space-y-2.5">
                        <Label htmlFor="bio" className="text-sm font-medium text-foreground/80 flex items-center gap-2">
                            Bio
                            <span className="text-xs text-muted-foreground">(Tell visitors about yourself)</span>
                        </Label>
                        <Textarea
                            id="bio"
                            value={data.bioText || ''}
                            onChange={(e) => updateField('bioText', e.target.value)}
                            placeholder="Share your story, what you do, or what visitors can find here..."
                            rows={4}
                            className="resize-none transition-all focus:ring-2 focus:ring-primary/20"
                        />
                    </div>

                    {/* Avatar Upload */}
                    <div className="pt-2">
                        <AvatarUpload
                            currentAvatar={data.avatarUrl}
                            onAvatarChange={(url) => updateField('avatarUrl', url)}
                        />
                    </div>
                </div>
            </motion.div>
        </div>
    );
}
