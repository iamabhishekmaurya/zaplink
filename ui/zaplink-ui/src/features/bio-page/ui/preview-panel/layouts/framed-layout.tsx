"use client";

import { motion } from "framer-motion";
import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { Sparkles, Zap, ExternalLink } from "lucide-react";
import { AnimatePresence } from "framer-motion";

interface LayoutProps {
    page: BioPageWithTheme;
    previewMode?: boolean;
    socialLinks: any[];
    regularLinks: any[];
    portalLinks: any[];
    itemVariants: any;
}

export function FramedLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    return (
        <div className="w-full flex flex-col items-center justify-center min-h-full px-4 py-8">

            {/* Framed Card Container */}
            <div
                className="w-full max-w-xl rounded-[2.5rem] p-6 sm:p-8 shadow-2xl relative overflow-hidden backdrop-blur-xl"
                style={{
                    background: theme.colors.cardBg || 'rgba(255, 255, 255, 0.8)',
                    boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
                    border: '1px solid rgba(255, 255, 255, 0.2)'
                }}
            >
                {/* Decorative elements */}
                <div className="absolute top-0 left-0 w-full h-2 bg-gradient-to-r from-transparent via-[var(--theme-primary)] to-transparent opacity-50" />

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
                            className="text-center py-12 border-2 border-dashed border-current/20 rounded-2xl opacity-50"
                        >
                            <Sparkles className="w-12 h-12 mx-auto mb-4" />
                            <p className="text-lg font-medium">No links added yet</p>
                        </motion.div>
                    )}
                </motion.div>

                {/* Footer */}
                <motion.div
                    variants={itemVariants}
                    className="mt-10 pt-6 text-center"
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
                        </a>
                    </div>
                </motion.div>
            </div>
        </div>
    );
}
