"use client";

import { motion, AnimatePresence } from "framer-motion";
import { Sparkles, Zap, ExternalLink } from "lucide-react";
import { LayoutProps } from "./types";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';

export function SplitLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    return (
        <div className="w-full max-w-5xl mx-auto flex flex-col md:flex-row gap-8 px-4 py-8 md:py-12 relative z-10 h-full min-h-[calc(100vh-4rem)]">

            {/* Left Column (Sticky Profile) */}
            <div className="w-full md:w-1/3 flex flex-col items-center md:items-start md:sticky md:top-24 h-fit">
                <motion.div variants={itemVariants} className="w-full">
                    <div className="bg-[var(--theme-card-bg)] backdrop-blur-md rounded-3xl p-8 shadow-xl border border-white/10 w-full flex flex-col items-center text-center">
                        <ProfileHeader
                            avatarUrl={page.avatarUrl}
                            title={page.title}
                            username={page.username}
                            bioText={page.bioText}
                            verified={page.isPublic}
                            theme={{ ...theme, layout: { ...theme.layout, contentAlignment: 'center' } }}
                        />

                        {socialLinks.length > 0 && (
                            <div className="mt-8 border-t border-[color-mix(in_srgb,var(--theme-text),transparent_80%)] pt-6 w-full">
                                <SocialMediaSection links={socialLinks} theme={theme} />
                            </div>
                        )}

                        <div className="mt-8 opacity-50 text-xs uppercase tracking-widest flex items-center justify-center gap-1">
                            <Zap className="w-3 h-3" /> Zaplink
                        </div>
                    </div>
                </motion.div>
            </div>

            {/* Right Column (Scrollable Links) */}
            <div className="w-full md:w-2/3 flex flex-col">
                {portalLinks.length > 0 && (
                    <motion.div variants={itemVariants} className="mb-8">
                        <PortalsSection links={portalLinks} theme={theme} previewMode={previewMode} />
                    </motion.div>
                )}

                <motion.div variants={itemVariants} className="space-y-4">
                    <AnimatePresence mode="popLayout">
                        {regularLinks.map((link: any, index: number) => (
                            <div key={link.id} className="transform transition-transform hover:-translate-y-1">
                                <AdvancedLinkCard
                                    link={link}
                                    theme={theme}
                                    previewMode={previewMode}
                                    index={index}
                                />
                            </div>
                        ))}
                    </AnimatePresence>

                    {regularLinks.length === 0 && previewMode && (
                        <motion.div
                            initial={{ opacity: 0, scale: 0.9 }}
                            animate={{ opacity: 1, scale: 1 }}
                            className="text-center py-20 border-2 border-dashed rounded-3xl"
                            style={{ borderColor: 'color-mix(in srgb, var(--theme-text), transparent 80%)' }}
                        >
                            <Sparkles className="w-12 h-12 mx-auto mb-4 opacity-30" />
                            <p className="text-xl font-medium opacity-50">Content goes here</p>
                        </motion.div>
                    )}
                </motion.div>
            </div>
        </div>
    );
}
