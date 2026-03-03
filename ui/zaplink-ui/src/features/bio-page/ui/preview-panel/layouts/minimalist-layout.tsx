"use client";

import { motion, AnimatePresence, Variants } from "framer-motion";
import { Sparkles, Zap, ExternalLink, CheckCircle } from "lucide-react";
import { LayoutProps } from "./types";

export function MinimalistLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    // Minimalist specific item variant with smooth fade
    const minimalistVariants: Variants = {
        hidden: { opacity: 0, y: 10 },
        visible: { opacity: 1, y: 0, transition: { duration: 0.8, ease: "easeOut" } }
    };

    return (
        <div className="w-full max-w-xl mx-auto px-6 py-16 md:py-24 relative z-10 flex flex-col items-center text-center font-sans tracking-tight">

            {/* Minimalist Profile */}
            <motion.div variants={minimalistVariants} className="flex flex-col items-center mb-12">
                {page.avatarUrl ? (
                    <img
                        src={page.avatarUrl}
                        alt={page.username}
                        className="w-16 h-16 rounded-full object-cover mb-6 shadow-sm grayscale hover:grayscale-0 transition-all duration-500"
                    />
                ) : (
                    <div className="w-16 h-16 rounded-full bg-zinc-200 dark:bg-zinc-800 mb-6" />
                )}

                <h1 className="text-3xl md:text-4xl font-light mb-2 flex items-center justify-center gap-2" style={{ color: 'var(--theme-text)' }}>
                    {page.title || `@${page.username}`}
                    {page.isPublic && <CheckCircle className="w-5 h-5 opacity-50" />}
                </h1>

                {page.bioText && (
                    <p className="text-base font-normal max-w-md opacity-60 leading-relaxed" style={{ color: 'var(--theme-text)' }}>
                        {page.bioText}
                    </p>
                )}
            </motion.div>

            {/* Social Links - Minimal Dots or tiny icons */}
            {socialLinks.length > 0 && (
                <motion.div variants={minimalistVariants} className="flex gap-4 justify-center mb-16">
                    {socialLinks.map(link => (
                        <a
                            key={link.id}
                            href={link.url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="w-10 h-10 flex items-center justify-center rounded-full border border-current opacity-40 hover:opacity-100 transition-opacity"
                        >
                            {/* In a real app, map platform to icon. We just use a dot if no icon is specified for minimalist */}
                            {link.iconUrl ? <img src={link.iconUrl} className="w-4 h-4 object-contain" alt="" /> : <span className="text-xs">{link.title[0]}</span>}
                        </a>
                    ))}
                </motion.div>
            )}

            {/* Main Links - Elegant Typography List */}
            <motion.div variants={minimalistVariants} className="w-full space-y-2 mb-20">
                <AnimatePresence mode="popLayout">
                    {regularLinks.map((link) => (
                        <motion.a
                            key={link.id}
                            href={link.url}
                            target="_blank"
                            rel="noopener noreferrer"
                            initial={{ opacity: 0 }}
                            animate={{ opacity: 1 }}
                            className="group flex flex-col items-center py-4 border-b border-zinc-200 dark:border-zinc-800 hover:border-zinc-400 dark:hover:border-zinc-600 transition-colors"
                        >
                            <span className="text-xl font-light tracking-wide group-hover:tracking-widest transition-all duration-300" style={{ color: 'var(--theme-text)' }}>
                                {link.title}
                            </span>
                            {link.price && (
                                <span className="text-xs opacity-50 mt-1 uppercase tracking-widest">
                                    {link.currency} {link.price}
                                </span>
                            )}
                        </motion.a>
                    ))}
                </AnimatePresence>

                {regularLinks.length === 0 && previewMode && (
                    <div className="text-center py-12 opacity-30">
                        <Sparkles className="w-6 h-6 mx-auto mb-2" />
                        <p className="text-sm tracking-widest uppercase">Empty</p>
                    </div>
                )}
            </motion.div>

            {/* Minimalist Footer */}
            <motion.div variants={minimalistVariants} className="mt-auto opacity-30 hover:opacity-100 transition-opacity">
                <a href="https://zap.link" target="_blank" rel="noopener noreferrer" className="text-xs uppercase tracking-[0.2em] font-light flex items-center gap-2">
                    <Zap className="w-3 h-3" /> ZAPLINK
                </a>
            </motion.div>
        </div>
    );
}
