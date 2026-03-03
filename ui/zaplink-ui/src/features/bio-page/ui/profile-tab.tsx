"use client";

import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { AvatarUpload } from "@/features/bio-page/ui/avatar-upload";
import { BioPage } from "@/services/bioPageService";
import { motion } from "framer-motion";
import { UserCircle } from "lucide-react";
import { cn } from "@/lib/utils";

interface ProfileTabProps {
    data: BioPage;
    updateField: (field: keyof BioPage, value: any) => void;
}

export function ProfileTab({ data, updateField }: ProfileTabProps) {
    return (
        <div className="space-y-8 pb-8 max-w-4xl mx-auto mt-4 px-2 sm:px-0">
            {/* Header */}
            <motion.div
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                className="flex items-center gap-4 pb-2"
            >
                <div className="p-3 bg-gradient-to-br from-primary/20 to-primary/5 rounded-2xl shadow-sm border border-primary/10">
                    <UserCircle className="w-6 h-6 text-primary" />
                </div>
                <div>
                    <h3 className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70 tracking-tight">
                        Profile & Identity
                    </h3>
                    <p className="text-sm text-muted-foreground mt-1">
                        Personalize how you appear to the world.
                    </p>
                </div>
            </motion.div>

            <motion.div
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                className="space-y-8"
            >
                {/* Unified Card */}
                <div className="rounded-xl border border-border/50 bg-card/40 backdrop-blur-xl shadow-xl overflow-hidden relative">
                    {/* Subtle decorative background for premium feel */}
                    <div className="absolute top-0 inset-x-0 h-40 bg-gradient-to-b from-primary/10 via-primary/5 to-transparent pointer-events-none" />

                    <div className="p-6 sm:p-10 relative z-10 space-y-12">
                        {/* Avatar */}
                        <AvatarUpload
                            currentAvatar={data.avatarUrl}
                            onAvatarChange={(url) => updateField('avatarUrl', url)}
                        />

                        {/* Divider */}
                        <div className="h-px bg-gradient-to-r from-transparent via-border to-transparent opacity-70" />

                        {/* Title & Bio */}
                        <div className="space-y-8">
                            {/* Page Title Field using Floating Label pattern */}
                            <div className="relative group">
                                <Input
                                    id="title"
                                    value={data.title || ''}
                                    onChange={(e) => updateField('title', e.target.value)}
                                    placeholder=" "
                                    className="block px-6 pb-4 pt-8 w-full text-base sm:text-lg bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-lg appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16"
                                />
                                <Label
                                    htmlFor="title"
                                    className="absolute text-sm sm:text-base text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none"
                                >
                                    Page Title / Name
                                </Label>
                                <div className="absolute right-6 top-5 text-[10px] sm:text-xs font-bold text-muted-foreground/50 uppercase tracking-widest hidden sm:block">
                                    Primary Identity
                                </div>
                            </div>

                            {/* Bio Field using Floating Label pattern */}
                            <div className="relative group">
                                <Textarea
                                    id="bio"
                                    value={data.bioText || ''}
                                    onChange={(e) => updateField('bioText', e.target.value)}
                                    placeholder=" "
                                    rows={5}
                                    maxLength={160}
                                    className="block px-6 pb-4 pt-10 w-full text-base sm:text-lg leading-relaxed bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-lg appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm resize-none"
                                />
                                <Label
                                    htmlFor="bio"
                                    className="absolute text-sm sm:text-base text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-6 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none"
                                >
                                    Bio / Introduction
                                </Label>
                                <div className={cn(
                                    "absolute right-6 top-5 text-[10px] sm:text-xs font-bold uppercase tracking-widest transition-colors",
                                    (data.bioText?.length || 0) > 150 ? "text-amber-500" : "text-muted-foreground/50"
                                )}>
                                    {data.bioText?.length || 0}/160
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </motion.div>
        </div>
    );
}
