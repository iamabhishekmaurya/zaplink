'use client';

import { useRef } from 'react';
import { motion, useScroll, useTransform } from 'motion/react';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button'; // Import Button

export const FeatureShowcase = () => {
    const containerRef = useRef(null);
    const { scrollYProgress } = useScroll({
        target: containerRef,
        offset: ["start end", "end start"]
    });

    const y = useTransform(scrollYProgress, [0, 1], [100, -100]);
    const opacity = useTransform(scrollYProgress, [0, 0.2, 0.8, 1], [0, 1, 1, 0]);

    return (
        <section ref={containerRef} className="py-32 relative overflow-hidden">
            <div className="max-w-7xl mx-auto px-4 md:px-6">
                <div className="text-center max-w-3xl mx-auto mb-20">
                    <span className="text-primary font-medium tracking-wider text-sm uppercase">Powerful Features</span>
                    <h2 className="text-3xl md:text-5xl font-bold mt-3 mb-6">Everything you need to <br />monitor your <span className="text-primary font-[family-name:var(--font-script)]">Growth</span></h2>
                    <p className="text-muted-foreground text-lg">
                        Our dashboard provides real-time insights into your link performance,
                        audience demographics, and conversion metrics.
                    </p>
                </div>

                <div className="grid md:grid-cols-2 gap-12 items-center">
                    {/* Left Column - Scrollable Feature Text */}
                    <div className="space-y-12">
                        {[
                            {
                                title: "Real-time Analytics",
                                desc: "Monitor clicks, geographical data, and device types as they happen.",
                                badge: "Live Data"
                            },
                            {
                                title: "Smart Targeting",
                                desc: "Redirect users based on their device, location, or language.",
                                badge: "AI Powered"
                            },
                            {
                                title: "Team Collaboration",
                                desc: "Invite team members and manage permissions efficiently.",
                                badge: "Enterprise"
                            }
                        ].map((feature, i) => (
                            <motion.div
                                key={i}
                                initial={{ opacity: 0, x: -30 }}
                                whileInView={{ opacity: 1, x: 0 }}
                                viewport={{ margin: "-100px" }}
                                transition={{ duration: 0.5, delay: i * 0.2 }}
                                className="relative pl-6 border-l-2 border-border/50 hover:border-primary transition-colors"
                            >
                                <Badge variant="secondary" className="mb-2 bg-primary/10 text-primary hover:bg-primary/20">{feature.badge}</Badge>
                                <h3 className="text-2xl font-bold mb-3">{feature.title}</h3>
                                <p className="text-muted-foreground leading-relaxed">
                                    {feature.desc}
                                </p>
                            </motion.div>
                        ))}
                    </div>

                    {/* Right Column - Parallax Image */}
                    <motion.div style={{ y, opacity }} className="relative perspective-1000">
                        <Card className="bg-card/50 backdrop-blur-xl border-border/50 shadow-2xl overflow-hidden rotate-y-12 rotate-x-6 transform-3d hover:rotate-0 transition-transform duration-700">
                            <CardContent className="p-0">
                                <div className="h-96 bg-gradient-to-br from-muted to-background p-6 relative">
                                    {/* Abstract Dashboard UI */}
                                    <div className="absolute top-6 left-6 right-6 bottom-6 border border-border/40 rounded-xl bg-background/80 p-4">
                                        <div className="flex justify-between items-center mb-6">
                                            <div className="h-8 w-32 bg-muted rounded animate-pulse" />
                                            <div className="h-8 w-8 rounded-full bg-primary/20" />
                                        </div>
                                        <div className="grid grid-cols-2 gap-4 mb-4">
                                            <div className="h-24 bg-primary/5 rounded-lg border border-primary/10 p-3">
                                                <div className="h-4 w-12 bg-primary/20 rounded mb-2" />
                                                <div className="h-8 w-20 bg-primary/10 rounded" />
                                            </div>
                                            <div className="h-24 bg-[#ff8904]/5 rounded-lg border border-[#ff8904]/10 p-3">
                                                <div className="h-4 w-12 bg-[#ff8904]/20 rounded mb-2" />
                                                <div className="h-8 w-20 bg-[#ff8904]/10 rounded" />
                                            </div>
                                        </div>
                                        <div className="h-32 bg-muted/30 rounded-lg w-full" />
                                    </div>

                                    {/* Floating overlay card */}
                                    <motion.div
                                        animate={{ y: [0, -10, 0] }}
                                        transition={{ duration: 4, repeat: Infinity, ease: "easeInOut" }}
                                        className="absolute -right-4 top-20 w-48 bg-card border border-border shadow-xl rounded-lg p-3"
                                    >
                                        <div className="flex items-center gap-2 mb-2">
                                            <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
                                            <span className="text-xs font-bold">Active Users</span>
                                        </div>
                                        <div className="text-2xl font-bold font-mono">1,248</div>
                                        <div className="h-1 w-full bg-emerald-500/20 mt-2 rounded-full overflow-hidden">
                                            <motion.div
                                                initial={{ width: "0%" }}
                                                animate={{ width: "70%" }}
                                                transition={{ duration: 1.5, repeat: Infinity }}
                                                className="h-full bg-emerald-500"
                                            />
                                        </div>
                                    </motion.div>
                                </div>
                            </CardContent>
                        </Card>
                    </motion.div>
                </div>
            </div>
        </section>
    );
};
