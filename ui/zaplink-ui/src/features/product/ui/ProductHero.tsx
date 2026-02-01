'use client';

import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { MoveRight, Zap, BarChart2, ShieldCheck, Globe } from 'lucide-react';
import { motion, useScroll, useTransform } from 'motion/react';
import { useRef } from 'react';
import Link from 'next/link'; // Import Link from next/link

export const ProductHero = () => {
    const targetRef = useRef(null);
    const { scrollYProgress } = useScroll({
        target: targetRef,
        offset: ["start start", "end start"]
    });

    const opacity = useTransform(scrollYProgress, [0, 0.5], [1, 0]);
    const scale = useTransform(scrollYProgress, [0, 0.5], [1, 0.9]);
    const y = useTransform(scrollYProgress, [0, 0.5], [0, 50]);

    return (
        <section ref={targetRef} className="relative min-h-[90vh] flex items-center justify-center overflow-hidden pt-30">
            {/* Dynamic Background */}
            <div className="absolute inset-0 bg-background">
                <div className="absolute top-0 -left-40 w-96 h-96 bg-primary/20 rounded-full blur-[128px] animate-pulse" />
                <div className="absolute bottom-0 -right-40 w-96 h-96 bg-[#ff8904]/10 rounded-full blur-[128px] animate-pulse delay-1000" />
                <div className="absolute inset-0 bg-[linear-gradient(to_right,#8080800a_1px,transparent_1px),linear-gradient(to_bottom,#8080800a_1px,transparent_1px)] bg-[size:24px_24px]" />
            </div>

            <div className="container px-4 md:px-6 relative z-10">
                <div className="text-center max-w-4xl mx-auto">
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5 }}
                        className="flex items-center justify-center gap-2 mb-6"
                    >
                        <Badge variant="outline" className="bg-primary/5 text-primary border-primary/20 px-4 py-1.5 text-sm rounded-full backdrop-blur-sm">
                            <Zap className="w-3.5 h-3.5 mr-2 fill-primary/20" />
                            Next Gen Link Management
                        </Badge>
                    </motion.div>

                    <motion.h1
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.1 }}
                        className="text-5xl md:text-7xl font-bold tracking-tight mb-8 bg-clip-text text-transparent bg-gradient-to-b from-foreground to-foreground/70"
                    >
                        Built for <span className="text-primary font-[family-name:var(--font-script)] italic">growth,</span><br />
                        Designed for <span className="text-[#ff8904] font-[family-name:var(--font-script)] italic">scale</span>
                    </motion.h1>

                    <motion.p
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.2 }}
                        className="text-xl text-muted-foreground mb-10 max-w-2xl mx-auto leading-relaxed"
                    >
                        Experience the most powerful link management platform.
                        Advanced analytics, custom QR codes, and enterprise-grade security
                        combined in one intuitive dashboard.
                    </motion.p>

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.3 }}
                        className="flex flex-col sm:flex-row items-center justify-center gap-4"
                    >
                        <Button size="lg" className="rounded-full px-8 h-12 text-base bg-primary hover:bg-primary/90 text-primary-foreground shadow-lg hover:shadow-primary/25 transition-all">
                            Start Free Trial
                            <MoveRight className="w-4 h-4 ml-2" />
                        </Button>
                        <Button size="lg" variant="outline" className="rounded-full px-8 h-12 text-base border-border hover:bg-muted/50">
                            View Documentation
                        </Button>
                    </motion.div>
                </div>

                {/* Floating UI Elements Animation */}
                <motion.div
                    style={{ opacity, scale, y }}
                    className="mt-20 relative h-[400px] w-full max-w-5xl mx-auto hidden md:block"
                >
                    {/* Central Dashboard Card */}
                    <div className="absolute inset-x-0 mx-auto w-3/4 h-full bg-card border border-border/50 rounded-t-3xl shadow-2xl overflow-hidden z-20">
                        <div className="h-full bg-gradient-to-br from-background to-muted/50 p-6">
                            <div className="flex items-center gap-4 mb-8">
                                <div className="w-3 h-3 rounded-full bg-rose-500" />
                                <div className="w-3 h-3 rounded-full bg-amber-500" />
                                <div className="w-3 h-3 rounded-full bg-emerald-500" />
                            </div>
                            <div className="space-y-4">
                                <div className="h-32 bg-primary/5 rounded-xl border border-primary/10 w-full animate-pulse" />
                                <div className="grid grid-cols-3 gap-4">
                                    <div className="h-24 bg-card rounded-xl border border-border/50 shadow-sm" />
                                    <div className="h-24 bg-card rounded-xl border border-border/50 shadow-sm" />
                                    <div className="h-24 bg-card rounded-xl border border-border/50 shadow-sm" />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Left Floating Card - Analytics */}
                    <motion.div
                        initial={{ x: -100, opacity: 0 }}
                        animate={{ x: 0, opacity: 1 }}
                        transition={{ delay: 0.5, duration: 0.8 }}
                        className="absolute left-0 bottom-10 w-64 bg-card/90 backdrop-blur-xl border border-border rounded-2xl shadow-xl p-4 z-30"
                    >
                        <div className="flex items-center gap-3 mb-3">
                            <div className="p-2 bg-primary/10 rounded-lg">
                                <BarChart2 className="w-5 h-5 text-primary" />
                            </div>
                            <div>
                                <p className="text-sm font-medium">Analytics</p>
                                <p className="text-xs text-emerald-500">+24.5% Growth</p>
                            </div>
                        </div>
                        <div className="h-1.5 w-full bg-muted rounded-full overflow-hidden">
                            <div className="h-full bg-primary w-[75%]" />
                        </div>
                    </motion.div>

                    {/* Right Floating Card - Security */}
                    <motion.div
                        initial={{ x: 100, opacity: 0 }}
                        animate={{ x: 0, opacity: 1 }}
                        transition={{ delay: 0.7, duration: 0.8 }}
                        className="absolute right-0 top-10 w-64 bg-card/90 backdrop-blur-xl border border-border rounded-2xl shadow-xl p-4 z-30"
                    >
                        <div className="flex items-center gap-3">
                            <div className="p-2 bg-[#ff8904]/10 rounded-lg">
                                <ShieldCheck className="w-5 h-5 text-[#ff8904]" />
                            </div>
                            <div>
                                <p className="text-sm font-medium">Enterprise Security</p>
                                <p className="text-xs text-muted-foreground">SOC2 Compliant</p>
                            </div>
                        </div>
                    </motion.div>
                </motion.div>
            </div>
        </section>
    );
};
