"use client";

import { motion } from "framer-motion";
import { Sparkles, Zap, ExternalLink } from "lucide-react";
import { LayoutProps } from "./types";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';

export function BentoGridLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    return (
        <div className="w-full max-w-4xl mx-auto px-4 py-8 relative z-10 flex flex-col gap-4">

            {/* Bento Grid Container */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 auto-rows-[minmax(120px,auto)]">

                {/* 1. Profile Block - Spans 2 cols on Desktop */}
                <motion.div
                    variants={itemVariants}
                    className="col-span-1 sm:col-span-2 row-span-2 bg-[var(--theme-card-bg)] rounded-3xl p-6 shadow-md border border-white/10 flex flex-col sm:flex-row items-center sm:items-start gap-6 backdrop-blur-md"
                >
                    {page.avatarUrl ? (
                        <img
                            src={page.avatarUrl}
                            alt={page.username}
                            className="w-24 h-24 sm:w-32 sm:h-32 object-cover rounded-2xl shrink-0"
                            style={{ borderRadius: 'var(--theme-btn-radius)' }}
                        />
                    ) : (
                        <div
                            className="w-24 h-24 sm:w-32 sm:h-32 bg-primary/20 shrink-0"
                            style={{ borderRadius: 'var(--theme-btn-radius)' }}
                        />
                    )}

                    <div className="flex-1 text-center sm:text-left h-full flex flex-col justify-center">
                        <h1 className="text-3xl font-bold tracking-tight mb-2" style={{ color: 'var(--theme-text)' }}>
                            {page.title || page.username}
                        </h1>
                        <p className="text-sm opacity-80" style={{ color: 'var(--theme-text)' }}>
                            {page.bioText}
                        </p>
                    </div>
                </motion.div>

                {/* 2. Socials Block */}
                {socialLinks.length > 0 && (
                    <motion.div
                        variants={itemVariants}
                        className="bg-[var(--theme-card-bg)] rounded-3xl p-6 shadow-md border border-white/10 flex items-center justify-center backdrop-blur-md h-full min-h-[160px]"
                    >
                        <div className="scale-110">
                            <SocialMediaSection links={socialLinks} theme={theme} />
                        </div>
                    </motion.div>
                )}

                {/* 3. Portals Block (if any, takes full width) */}
                {portalLinks.length > 0 && (
                    <motion.div
                        variants={itemVariants}
                        className="col-span-1 sm:col-span-2 lg:col-span-3 bg-[var(--theme-card-bg)] rounded-3xl p-6 shadow-md border border-white/10 backdrop-blur-md"
                    >
                        <PortalsSection links={portalLinks} theme={theme} previewMode={previewMode} />
                    </motion.div>
                )}

                {/* 4. Main Links Grid */}
                {regularLinks.map((link: any, index: number) => {
                    // Make the first few links span 2 columns if not many, otherwise just 1
                    const spanClass = index === 0 && regularLinks.length % 2 !== 0 ? 'sm:col-span-2' : 'col-span-1';

                    return (
                        <motion.div
                            key={link.id}
                            variants={itemVariants}
                            className={`${spanClass} h-full min-h-[100px] flex group relative`}
                        >
                            <div className="w-full h-full [&>a]:h-full [&>button]:h-full [&>div]:h-full">
                                <AdvancedLinkCard
                                    link={link}
                                    theme={{ ...theme, layout: { ...theme.layout, cardStyle: 'flat' } }} // Override card style to fit bento
                                    previewMode={previewMode}
                                    index={index}
                                />
                            </div>
                        </motion.div>
                    );
                })}

                {/* Empty State */}
                {regularLinks.length === 0 && previewMode && (
                    <motion.div
                        variants={itemVariants}
                        className="col-span-1 sm:col-span-2 lg:col-span-3 bg-[var(--theme-card-bg)] rounded-3xl p-12 border-2 border-dashed flex flex-col items-center justify-center backdrop-blur-md"
                        style={{ borderColor: 'color-mix(in srgb, var(--theme-text), transparent 80%)' }}
                    >
                        <Sparkles className="w-8 h-8 opacity-40 mb-2" />
                        <span className="font-medium opacity-60">Add links to fill the grid</span>
                    </motion.div>
                )}
            </div>

            <motion.div variants={itemVariants} className="mt-8 text-center flex justify-center">
                <a href="https://zap.link" target="_blank" rel="noopener noreferrer" className="flex items-center gap-1.5 opacity-50 hover:opacity-100 transition-opacity">
                    <Zap className="w-3.5 h-3.5" />
                    <span className="text-xs font-semibold tracking-wider uppercase">Zaplink</span>
                </a>
            </motion.div>
        </div>
    );
}
