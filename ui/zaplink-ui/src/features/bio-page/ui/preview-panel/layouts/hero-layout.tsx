"use client";

import { motion } from "framer-motion";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { Sparkles, Zap, ExternalLink } from "lucide-react";
import { AnimatePresence } from "framer-motion";
import { cn } from "@/lib/utils";
import { LayoutProps } from "./types";

export function HeroLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    const coverStyle = {
        backgroundImage: theme.effects?.backgroundImage ? `url(${theme.effects.backgroundImage})` : undefined,
        background: !theme.effects?.backgroundImage ? (theme.effects?.backgroundGradient || 'var(--theme-primary)') : undefined,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
    };

    return (
        <div className="w-full flex flex-col min-h-full pb-12">
            {/* Hero Cover */}
            <div className="h-48 sm:h-64 w-full relative shrink-0" style={coverStyle}>
                <div className="absolute inset-0 bg-black/20" />
            </div>

            {/* Content Container - Overlapping the cover */}
            <div className="flex-1 w-full max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 -mt-20 relative z-10 pb-12">
                <div className={cn(
                    "rounded-3xl p-6 md:p-8 shadow-xl backdrop-blur-md",
                    theme.layout?.cardStyle === 'glass' ? "bg-white/10 border border-white/20" : "bg-[var(--theme-card-bg)]"
                )}>

                    <motion.div variants={itemVariants}>
                        <ProfileHeader
                            avatarUrl={page.avatarUrl}
                            title={page.title}
                            username={page.username}
                            bioText={page.bioText}
                            verified={page.isPublic}
                            theme={{ ...theme, layout: { ...theme.layout, contentAlignment: 'center' } }}
                        />
                    </motion.div>

                    {socialLinks.length > 0 && (
                        <motion.div variants={itemVariants} className="mt-8 border-t border-[color-mix(in_srgb,var(--theme-text),transparent_90%)] pt-6">
                            <SocialMediaSection links={socialLinks} theme={theme} />
                        </motion.div>
                    )}

                    {portalLinks.length > 0 && (
                        <motion.div variants={itemVariants} className="mt-8">
                            <PortalsSection links={portalLinks} theme={theme} previewMode={previewMode} />
                        </motion.div>
                    )}

                    <motion.div variants={itemVariants} className="mt-8 space-y-4">
                        <AnimatePresence mode="popLayout">
                            {regularLinks.map((link: any, index: number) => (
                                <AdvancedLinkCard
                                    key={link.id}
                                    link={link}
                                    theme={theme}
                                    previewMode={previewMode}
                                    index={index}
                                />
                            ))}
                        </AnimatePresence>

                        {regularLinks.length === 0 && previewMode && (
                            <motion.div
                                initial={{ opacity: 0, scale: 0.9 }}
                                animate={{ opacity: 1, scale: 1 }}
                                className="text-center py-12 border-2 border-dashed border-zinc-500/20 rounded-2xl"
                            >
                                <Sparkles className="w-12 h-12 mx-auto mb-4 opacity-50" />
                                <p className="text-lg font-medium opacity-70">No links added yet</p>
                            </motion.div>
                        )}
                    </motion.div>

                    <motion.div variants={itemVariants} className="mt-12 pt-6 text-center border-t border-zinc-500/10">
                        <div
                            className="inline-flex items-center gap-2 px-3 py-1 rounded-full"
                            style={{ backgroundColor: 'color-mix(in srgb, var(--theme-text), transparent 90%)' }}
                        >
                            <Zap className="w-3 h-3" />
                            <span className="text-xs font-medium">Powered by</span>
                            <a href="https://zap.link" target="_blank" rel="noopener noreferrer" className="text-xs font-bold hover:underline inline-flex items-center gap-0.5">
                                Zaplink
                            </a>
                        </div>
                    </motion.div>
                </div>
            </div>
        </div>
    );
}
