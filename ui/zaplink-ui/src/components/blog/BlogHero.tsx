'use client';

import { motion } from 'framer-motion';
import { PenTool } from 'lucide-react';

export default function BlogHero() {
    return (
        <section className="relative pt-32 pb-16 overflow-hidden">
            {/* Background Gradients */}
            <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-primary/10 via-background to-background pointer-events-none" />

            <div className="max-w-7xl mx-auto px-4 sm:px-6 relative z-10 text-center">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium mb-6 border border-primary/20"
                >
                    <PenTool className="w-3.5 h-3.5" />
                    <span>The Zaplink Blog</span>
                </motion.div>

                <motion.h1
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: 0.1 }}
                    className="text-4xl md:text-6xl font-bold tracking-tight mb-6"
                >
                    Insights, updates, and <br />
                    stories for <span className="text-primary font-[family-name:var(--font-script)]">builders</span>.
                </motion.h1>

                <motion.p
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: 0.2 }}
                    className="text-lg text-muted-foreground max-w-2xl mx-auto"
                >
                    Discover the latest trends in link management, digital marketing, and product growth directly from our team.
                </motion.p>
            </div>

            {/* Decorative */}
            <motion.div
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 0.5, scale: 1 }}
                transition={{ duration: 1, delay: 0.4 }}
                className="absolute top-1/2 left-0 w-64 h-64 bg-violet-500/10 rounded-full blur-3xl -z-10"
            />
            <motion.div
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 0.5, scale: 1 }}
                transition={{ duration: 1, delay: 0.5 }}
                className="absolute bottom-0 right-0 w-64 h-64 bg-[#ff8904]/10 rounded-full blur-3xl -z-10"
            />
        </section>
    );
}
