'use client';

import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { ArrowRight, FileText } from 'lucide-react';
import Image from 'next/image';

export default function FeaturedResource() {
    return (
        <section className="py-12 px-4">
            <div className="max-w-7xl mx-auto">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    className="relative rounded-3xl bg-muted/30 border border-border/50 overflow-hidden"
                >
                    <div className="grid lg:grid-cols-2 gap-8 items-center p-8 md:p-12">
                        <div className="space-y-6">
                            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-[#ff8904]/10 text-[#ff8904] text-sm font-medium border border-[#ff8904]/20">
                                <FileText className="w-4 h-4" />
                                <span>Latest Guide</span>
                            </div>
                            <h2 className="text-3xl md:text-4xl font-bold">The Ultimate Guide to Link Management Strategies in 2026</h2>
                            <p className="text-lg text-muted-foreground">
                                Discover how top brands comprise their URL strategies to drive engagement and boost conversion rates. This comprehensive guide covers everything from basic shortening to advanced analytics.
                            </p>
                            <div className="flex items-center gap-4 pt-2">
                                <Button size="lg" className="rounded-xl px-8 bg-gradient-to-r from-[#ff8904] to-orange-600 hover:opacity-90">
                                    Read Guide <ArrowRight className="w-4 h-4 ml-2" />
                                </Button>
                                <span className="text-sm text-muted-foreground">15 min read</span>
                            </div>
                        </div>
                        <div className="relative h-[300px] lg:h-[400px] rounded-2xl overflow-hidden bg-gradient-to-br from-slate-900 to-slate-800 flex items-center justify-center group">
                            {/* Placeholder for an actual image */}
                            <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,_var(--tw-gradient-stops))] from-primary/20 to-transparent opacity-50 group-hover:opacity-100 transition-opacity duration-500" />
                            <div className="relative z-10 text-center p-6">
                                <div className="w-24 h-32 mx-auto bg-white/5 backdrop-blur-md border border-white/10 rounded-lg shadow-2xl mb-4 group-hover:-translate-y-2 transition-transform duration-500 flex items-center justify-center">
                                    <span className="text-4xl">📚</span>
                                </div>
                                <p className="text-white/70 font-medium">Zaplink Originals</p>
                            </div>
                        </div>
                    </div>
                </motion.div>
            </div>
        </section>
    );
}
