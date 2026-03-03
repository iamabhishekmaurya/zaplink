"use client";

import { motion, AnimatePresence } from "framer-motion";
import { Sparkles, FileText, CheckCircle } from "lucide-react";
import { LayoutProps } from "./types";

export function NotionLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
}: LayoutProps) {
    const theme = page.parsedTheme;

    return (
        <div className="w-full max-w-3xl mx-auto bg-stone-50 dark:bg-[#191919] min-h-screen relative z-10 px-8 py-12 sm:px-16 sm:py-20 font-serif text-stone-800 dark:text-stone-300">

            {/* Notion Cover Image (Optional) */}
            {theme.effects?.backgroundImage && (
                <div className="absolute top-0 left-0 w-full h-48 sm:h-64 overflow-hidden z-0">
                    <img src={theme.effects.backgroundImage} alt="Cover" className="w-full h-full object-cover" />
                </div>
            )}

            <div className={`relative z-10 ${theme.effects?.backgroundImage ? 'mt-32 sm:mt-44' : ''}`}>
                {/* Avatar / Icon */}
                <motion.div variants={itemVariants} className="mb-4">
                    {page.avatarUrl ? (
                        <img
                            src={page.avatarUrl}
                            alt={page.username}
                            className="w-20 h-20 sm:w-24 sm:h-24 rounded object-cover shadow-sm bg-white"
                        />
                    ) : (
                        <div className="w-20 h-20 sm:w-24 sm:h-24 rounded bg-stone-200 dark:bg-stone-800 flex items-center justify-center text-3xl">
                            📄
                        </div>
                    )}
                </motion.div>

                {/* Notion Header */}
                <motion.div variants={itemVariants} className="mb-8">
                    <h1 className="text-4xl sm:text-5xl font-bold mb-4 tracking-tight flex items-center gap-3">
                        {page.title || page.username}
                        {page.isPublic && <CheckCircle className="w-6 h-6 text-stone-400" />}
                    </h1>

                    {page.bioText && (
                        <p className="text-lg text-stone-600 dark:text-stone-400 max-w-2xl leading-relaxed">
                            {page.bioText}
                        </p>
                    )}
                </motion.div>

                <hr className="border-stone-200 dark:border-stone-800 mb-8" />

                {/* Socials - Notion Callout style */}
                {socialLinks.length > 0 && (
                    <motion.div variants={itemVariants} className="mb-8 flex flex-wrap gap-2">
                        {socialLinks.map(link => (
                            <a
                                key={link.id}
                                href={link.url}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center gap-2 px-3 py-1.5 rounded bg-stone-100 dark:bg-stone-800 hover:bg-stone-200 dark:hover:bg-stone-700 transition-colors text-sm font-medium"
                            >
                                {link.iconUrl && <img src={link.iconUrl} className="w-4 h-4" alt="" />}
                                {link.title}
                            </a>
                        ))}
                    </motion.div>
                )}

                {/* Main Links - Notion List items */}
                <motion.div variants={itemVariants} className="space-y-1 mb-16">
                    <AnimatePresence mode="popLayout">
                        {regularLinks.map((link) => (
                            <motion.a
                                key={link.id}
                                href={link.url}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="group flex items-center justify-between p-2 rounded hover:bg-stone-100 dark:hover:bg-[#252525] transition-colors"
                            >
                                <div className="flex items-center gap-3">
                                    <div className="w-6 flex justify-center text-stone-400">
                                        <FileText className="w-4 h-4" />
                                    </div>
                                    <span className="font-medium underline decoration-stone-300 dark:decoration-stone-700 underline-offset-4 group-hover:decoration-stone-500 transition-colors">
                                        {link.title}
                                    </span>
                                </div>
                                {link.price && (
                                    <span className="text-xs bg-stone-200 dark:bg-stone-800 px-2 py-1 rounded">
                                        {link.currency} {link.price}
                                    </span>
                                )}
                            </motion.a>
                        ))}
                    </AnimatePresence>

                    {regularLinks.length === 0 && previewMode && (
                        <div className="flex items-center gap-3 p-2 text-stone-400 italic">
                            No links added yet. Click &quot;Add Link&quot; to start.
                        </div>
                    )}
                </motion.div>
            </div>
        </div>
    );
}
