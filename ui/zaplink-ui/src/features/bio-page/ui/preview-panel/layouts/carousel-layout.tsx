"use client";

import { motion, AnimatePresence } from "framer-motion";
import { Sparkles, Zap, ChevronLeft, ChevronRight, ExternalLink } from "lucide-react";
import { LayoutProps } from "./types";
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { useRef } from "react";

export function CarouselLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;
    const scrollContainerRef = useRef<HTMLDivElement>(null);

    const scroll = (direction: 'left' | 'right') => {
        if (scrollContainerRef.current) {
            const scrollAmount = direction === 'left' ? -300 : 300;
            scrollContainerRef.current.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        }
    };

    return (
        <div className="w-full min-h-screen relative z-10 flex flex-col justify-center items-center py-12 px-4 overflow-hidden">

            <motion.div variants={itemVariants} className="w-full max-w-xl mx-auto mb-12">
                <ProfileHeader
                    avatarUrl={page.avatarUrl}
                    title={page.title}
                    username={page.username}
                    bioText={page.bioText}
                    verified={page.isPublic}
                    theme={theme}
                />

                {socialLinks.length > 0 && (
                    <div className="mt-8">
                        <SocialMediaSection links={socialLinks} theme={theme} />
                    </div>
                )}
            </motion.div>

            {/* Horizontal Scroll Area */}
            <motion.div variants={itemVariants} className="w-full max-w-5xl mx-auto relative group">

                {regularLinks.length > 2 && (
                    <>
                        <button
                            onClick={() => scroll('left')}
                            className="absolute left-0 top-1/2 -translate-y-1/2 -translate-x-4 md:-translate-x-12 z-20 w-12 h-12 rounded-full backdrop-blur-md bg-[var(--theme-card-bg)] border border-white/20 shadow-lg flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity disabled:opacity-0"
                            style={{ color: 'var(--theme-text)' }}
                        >
                            <ChevronLeft className="w-6 h-6" />
                        </button>

                        <button
                            onClick={() => scroll('right')}
                            className="absolute right-0 top-1/2 -translate-y-1/2 translate-x-4 md:translate-x-12 z-20 w-12 h-12 rounded-full backdrop-blur-md bg-[var(--theme-card-bg)] border border-white/20 shadow-lg flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity disabled:opacity-0"
                            style={{ color: 'var(--theme-text)' }}
                        >
                            <ChevronRight className="w-6 h-6" />
                        </button>
                    </>
                )}

                <div
                    ref={scrollContainerRef}
                    className="flex overflow-x-auto gap-4 md:gap-6 pb-8 pt-4 px-4 snap-x snap-mandatory scrollbar-hide no-scrollbar"
                    style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
                >
                    <AnimatePresence>
                        {regularLinks.map((link: any, index: number) => (
                            <motion.div
                                key={link.id}
                                className="min-w-[85vw] sm:min-w-[320px] max-w-[400px] shrink-0 snap-center transition-transform hover:scale-[1.02] duration-300"
                            >
                                <div className="h-full bg-[var(--theme-card-bg)] rounded-[2rem] p-6 shadow-xl border border-white/10 backdrop-blur-md flex flex-col justify-between">
                                    <div className="mb-6">
                                        {link.thumbnailUrl ? (
                                            <div className="w-20 h-20 rounded-2xl overflow-hidden mb-6 shadow-md">
                                                <img src={link.thumbnailUrl} alt="" className="w-full h-full object-cover" />
                                            </div>
                                        ) : link.iconUrl ? (
                                            <div className="w-16 h-16 rounded-2xl bg-black/5 flex items-center justify-center mb-6 shadow-sm">
                                                <img src={link.iconUrl} alt="" className="w-8 h-8 object-contain" />
                                            </div>
                                        ) : (
                                            <div className="w-12 h-12 rounded-full border-2 border-dashed border-[var(--theme-primary)] flex items-center justify-center mb-6 opacity-50">
                                                <Zap className="w-5 h-5" style={{ color: 'var(--theme-primary)' }} />
                                            </div>
                                        )}

                                        <h3 className="text-2xl font-bold mb-2 line-clamp-2" style={{ color: 'var(--theme-text)' }}>{link.title}</h3>
                                        <p className="text-sm opacity-60 line-clamp-3" style={{ color: 'var(--theme-text)' }}>
                                            {link.metadata?.description || "Check out this link for more information."}
                                        </p>
                                    </div>

                                    <a
                                        href={link.url}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="w-full py-4 rounded-xl flex items-center justify-center font-bold relative overflow-hidden group/btn"
                                        style={{
                                            backgroundColor: 'var(--theme-btn-bg)',
                                            color: 'var(--theme-btn-text)',
                                            borderRadius: 'var(--theme-btn-radius)'
                                        }}
                                    >
                                        <span className="relative z-10 flex items-center gap-2">
                                            {link.price ? `Buy for ${link.currency}${link.price}` : 'Open Link'}
                                            <ExternalLink className="w-4 h-4 ml-1" />
                                        </span>
                                        <div className="absolute inset-0 bg-white/20 translate-y-full group-hover/btn:translate-y-0 transition-transform duration-300" />
                                    </a>
                                </div>
                            </motion.div>
                        ))}
                    </AnimatePresence>

                    {regularLinks.length === 0 && previewMode && (
                        <div className="w-full min-w-[300px] h-64 border-2 border-dashed rounded-3xl flex flex-col items-center justify-center" style={{ borderColor: 'color-mix(in srgb, var(--theme-text), transparent 80%)' }}>
                            <Sparkles className="w-8 h-8 opacity-40 mb-2" />
                            <span className="font-medium opacity-60">Add some links to spin the carousel</span>
                        </div>
                    )}
                </div>
            </motion.div>

        </div>
    );
}
