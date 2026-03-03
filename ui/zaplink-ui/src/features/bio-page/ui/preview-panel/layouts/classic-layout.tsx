"use client";

import { motion, AnimatePresence } from "framer-motion";
import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { Sparkles, Zap, ExternalLink } from "lucide-react";

import { LayoutProps } from "./types";

export function ClassicLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    return (
        <div className="w-full max-w-2xl mx-auto relative z-10">
            {/* Profile Header */}
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
                        className="text-center py-12 border-2 border-dashed rounded-2xl"
                        style={{ borderColor: 'color-mix(in srgb, var(--theme-text), transparent 80%)' }}
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
                className="mt-16 pt-8 text-center"
            >
                <div
                    className="inline-flex items-center gap-2 px-4 py-2 rounded-full backdrop-blur-sm"
                    style={{ backgroundColor: 'color-mix(in srgb, var(--theme-text), transparent 90%)' }}
                >
                    <Zap className="w-4 h-4" />
                    <span className="text-sm font-medium">Powered by</span>
                    <a
                        href="https://zap.link"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-sm font-bold hover:underline inline-flex items-center gap-1"
                    >
                        Zaplink
                        <ExternalLink className="w-3 h-3" />
                    </a>
                </div>
            </motion.div>
        </div>
    );
}
