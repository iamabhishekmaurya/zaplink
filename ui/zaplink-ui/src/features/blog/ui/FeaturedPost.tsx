'use client';

import { motion } from 'framer-motion';
import { ArrowRight, Calendar, User, Clock } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';

export default function FeaturedPost() {
    return (
        <section className="py-12 px-4">
            <div className="max-w-7xl mx-auto">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6 }}
                    className="group relative rounded-3xl overflow-hidden bg-card border border-border/50 hover:border-primary/20 transition-all duration-500 shadow-lg hover:shadow-2xl"
                >
                    <div className="grid lg:grid-cols-2">

                        {/* Image Side */}
                        <div className="relative h-[300px] lg:h-auto min-h-[400px] overflow-hidden bg-muted">
                            <div className="absolute inset-0 bg-gradient-to-br from-violet-600 to-indigo-900 group-hover:scale-105 transition-transform duration-700" />

                            {/* Overlay Text/Decor (optional placeholder for real image) */}
                            <div className="absolute inset-0 flex items-center justify-center">
                                <span className="text-9xl opacity-10 select-none">🔥</span>
                            </div>
                        </div>

                        {/* Content Side */}
                        <div className="p-8 md:p-12 lg:p-16 flex flex-col justify-center">
                            <div className="flex items-center gap-3 mb-6">
                                <Badge variant="secondary" className="bg-primary/10 text-primary hover:bg-primary/20">
                                    Product Update
                                </Badge>
                                <span className="text-sm text-muted-foreground flex items-center gap-1">
                                    <Calendar className="w-3.5 h-3.5" /> Jan 24, 2026
                                </span>
                                <span className="text-sm text-muted-foreground flex items-center gap-1">
                                    <Clock className="w-3.5 h-3.5" /> 5 min read
                                </span>
                            </div>

                            <h2 className="text-3xl md:text-4xl font-bold mb-6 group-hover:text-primary transition-colors">
                                Introducing Analytics 2.0: Deeper Insights, Faster Decisions
                            </h2>

                            <p className="text-lg text-muted-foreground mb-8 leading-relaxed">
                                We've completely rebuilt our analytics engine from the ground up. Real-time data processing, granular device breakdowns, and cleaner visualizations are just the beginning.
                            </p>

                            <div className="flex items-center gap-4 mt-auto">
                                <div className="flex items-center gap-3">
                                    <div className="w-10 h-10 rounded-full bg-gradient-to-br from-orange-400 to-red-500 flex items-center justify-center text-white font-bold text-sm">
                                        AM
                                    </div>
                                    <div className="text-sm">
                                        <p className="font-semibold text-foreground">Abhishek Maurya</p>
                                        <p className="text-muted-foreground">Founder & CEO</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </motion.div>
            </div>
        </section>
    );
}
