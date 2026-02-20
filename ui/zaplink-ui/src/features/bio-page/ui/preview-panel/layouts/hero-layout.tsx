"use client";

import { motion } from "framer-motion";
import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { Sparkles, Zap, ExternalLink } from "lucide-react";
import { AnimatePresence } from "framer-motion";
import { cn } from "@/lib/utils";

interface LayoutProps {
    page: BioPageWithTheme;
    previewMode?: boolean;
    socialLinks: any[];
    regularLinks: any[];
    portalLinks: any[];
    itemVariants: any;
}

export function HeroLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    // Use background image as cover if available, otherwise a gradient/solid color
    const coverStyle = {
        backgroundImage: theme.effects?.backgroundImage ? `url(${theme.effects.backgroundImage})` : undefined,
        background: !theme.effects?.backgroundImage ? (theme.effects?.backgroundGradient || 'var(--theme-primary)') : undefined,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
    };

    return (
        <div className="w-full flex flex-col min-h-full">
            {/* Hero Cover */}
            <div className="h-48 sm:h-64 w-full relative shrink-0" style={coverStyle}>
                <div className="absolute inset-0 bg-black/20" /> {/* Slight overlay for text contrast if needed */}
                {theme.effects?.particles && (
                    <div className="absolute inset-0 pointer-events-none overflow-hidden">
                        <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_50%,rgba(255,255,255,0.2)_0%,transparent_50%)]" />
                    </div>
                )}
            </div>

            {/* Content Container - Overlapping the cover */}
            <div className="flex-1 w-full max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 -mt-20 relative z-10 pb-12">
                <div className={cn(
                    "rounded-3xl p-6 shadow-xl backdrop-blur-sm",
                    theme.layout?.cardStyle === 'glass' ? "bg-white/10 border border-white/20" : "bg-[var(--theme-card-bg)]"
                )}>

                    {/* Profile Header (Avatar will need custom handling here or standard header adapted) */}
                    <motion.div variants={itemVariants}>
                        <ProfileHeader
                            avatarUrl={page.avatarUrl}
                            title={page.title}
                            username={page.username}
                            bioText={page.bioText}
                            verified={page.isPublic}
                            theme={theme}
                        />
                    </motion.div>

                    {/* Social Media Icons */}
                    {socialLinks.length > 0 && (
                        <motion.div variants={itemVariants} className="mt-6">
                            <SocialMediaSection links={socialLinks} theme={theme} />
                        </motion.div>
                    )}

                    {/* Portals/Sections */}
                    {portalLinks.length > 0 && (
                        <motion.div variants={itemVariants} className="mt-8">
                            <PortalsSection links={portalLinks} theme={theme} previewMode={previewMode} />
                        </motion.div>
                    )}

                    {/* Main Links */}
                    <motion.div variants={itemVariants} className="mt-8 space-y-3">
                        <AnimatePresence mode="popLayout">
                            {regularLinks.map((link, index) => (
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
                                <p className="text-sm opacity-50 mt-1">Add your first link to get started</p>
                            </motion.div>
                        )}
                    </motion.div>

                    {/* Footer */}
                    <motion.div
                        variants={itemVariants}
                        className="mt-12 pt-6 text-center border-t border-zinc-500/10"
                    >
                        <div
                            className="inline-flex items-center gap-2 px-3 py-1 rounded-full"
                            style={{ backgroundColor: 'color-mix(in srgb, var(--theme-text), transparent 90%)' }}
                        >
                            <Zap className="w-3 h-3" />
                            <span className="text-xs font-medium">Powered by</span>
                            <a
                                href="https://zap.link"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-xs font-bold hover:underline inline-flex items-center gap-0.5"
                            >
                                Zaplink
                                <ExternalLink className="w-2.5 h-2.5" />
                            </a>
                        </div>
                    </motion.div>
                </div>
            </div>
        </div>
    );
}
