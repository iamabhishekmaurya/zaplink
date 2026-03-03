"use client";

import { motion } from "framer-motion";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { Sparkles, Zap } from "lucide-react";
import { AnimatePresence } from "framer-motion";
import { LayoutProps } from "./types";

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
        <div className="w-full h-full min-h-screen flex flex-col items-center justify-center p-4 sm:p-8">

            <div
                className="w-full max-w-xl rounded-[2.5rem] p-6 sm:p-8 relative overflow-hidden backdrop-blur-xl transition-all duration-500 my-auto"
                style={{
                    background: theme.colors.cardBg || 'rgba(255, 255, 255, 0.8)',
                    boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25), 0 0 100px rgba(0,0,0,0.1)',
                    border: '1px solid rgba(255, 255, 255, 0.2)'
                }}
            >
                {/* Decorative glowing edge */}
                <div
                    className="absolute top-0 left-0 w-full h-2 opacity-50"
                    style={{ background: `linear-gradient(90deg, transparent, var(--theme-primary), transparent)` }}
                />

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

                {socialLinks.length > 0 && (
                    <motion.div variants={itemVariants} className="mt-8">
                        <SocialMediaSection links={socialLinks} theme={theme} />
                    </motion.div>
                )}

                {portalLinks.length > 0 && (
                    <motion.div variants={itemVariants} className="mt-8">
                        <PortalsSection links={portalLinks} theme={theme} previewMode={previewMode} />
                    </motion.div>
                )}

                <motion.div variants={itemVariants} className="mt-8 space-y-3">
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
                            className="text-center py-10 border-2 border-dashed border-current/20 rounded-2xl opacity-50"
                        >
                            <Sparkles className="w-10 h-10 mx-auto mb-2" />
                            <p className="font-medium">Empty Frame</p>
                        </motion.div>
                    )}
                </motion.div>

                <motion.div variants={itemVariants} className="mt-10 pt-6 text-center">
                    <div
                        className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold backdrop-blur-sm"
                        style={{ backgroundColor: 'color-mix(in srgb, var(--theme-text), transparent 90%)', color: 'var(--theme-text)' }}
                    >
                        <Zap className="w-3 h-3" />
                        Zaplink
                    </div>
                </motion.div>
            </div>
        </div>
    );
}
