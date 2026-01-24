'use client';

import { motion } from 'framer-motion';
import { BookOpen, Sparkles } from 'lucide-react';

export default function ResourcesHero() {
    return (
        <section className="relative pt-32 pb-20 overflow-hidden">
            {/* Background Gradients */}
            <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-primary/20 via-background to-background pointer-events-none" />
            <div className="absolute top-0 right-0 w-[500px] h-[500px] bg-primary/10 rounded-full blur-[100px] pointer-events-none" />
            <div className="absolute bottom-0 left-0 w-[500px] h-[500px] bg-[#ff8904]/10 rounded-full blur-[100px] pointer-events-none" />

            <div className="max-w-7xl mx-auto px-4 sm:px-6 relative z-10 text-center">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium mb-6 border border-primary/20"
                >
                    <BookOpen className="w-4 h-4" />
                    <span>Resources & Knowledge</span>
                </motion.div>

                <motion.h1
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: 0.1 }}
                    className="text-5xl md:text-7xl font-bold tracking-tight mb-8"
                >
                    Empower your <br />
                    <span className="text-primary font-[family-name:var(--font-script)]">growth</span> with knowledge
                </motion.h1>

                <motion.p
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: 0.2 }}
                    className="text-xl text-muted-foreground max-w-2xl mx-auto mb-10"
                >
                    Explore our guides, tutorials, and articles to master Zaplink and supercharge your marketing campaigns.
                </motion.p>
            </div>

            {/* Floating Elements (Decorative) */}
            <motion.div
                animate={{ y: [0, -20, 0], rotate: [0, 5, 0] }}
                transition={{ duration: 4, repeat: Infinity, ease: "easeInOut" }}
                className="absolute top-1/3 left-10 md:left-20 w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-violet-600 blur-xl opacity-40"
            />
            <motion.div
                animate={{ y: [0, 20, 0], rotate: [0, -5, 0] }}
                transition={{ duration: 5, repeat: Infinity, ease: "easeInOut", delay: 1 }}
                className="absolute top-1/4 right-10 md:right-20 w-20 h-20 rounded-full bg-gradient-to-br from-[#ff8904] to-red-500 blur-xl opacity-30"
            />
        </section>
    );
}
