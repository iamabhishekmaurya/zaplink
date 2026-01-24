'use client';

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { ArrowRight, TrendingUp } from "lucide-react";
import { motion } from "motion/react";
import Link from 'next/link';

export const CTASection = () => {
    return (
        <section className="py-32 bg-background">
            <div className="max-w-7xl mx-auto px-4 md:px-6">
                <div className="relative rounded-3xl bg-primary/10 overflow-hidden">
                    <div className="absolute inset-0 opacity-40">
                        <div className="absolute top-0 right-0 w-1/2 h-full bg-gradient-to-l from-primary/30 to-transparent" />
                        <div className="absolute bottom-0 left-0 w-1/3 h-1/2 bg-gradient-to-t from-secondary/30 to-transparent" />
                    </div>

                    <motion.div
                        className="relative z-10 grid lg:grid-cols-2 gap-8 p-8 md:p-12 items-center"
                        initial={{ opacity: 0, y: 30 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ duration: 0.6 }}
                    >
                        <div className="text-center lg:text-left">
                            <span className="text-sm font-medium text-primary mb-2 block">Contact</span>
                            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
                                <span className="font-[family-name:var(--font-script)] text-primary">Supercharge</span> your links<br />with Zaplink today!
                            </h2>
                            <p className="text-muted-foreground mb-6">Join thousands of marketers who trust Zaplink.</p>
                            <Link href="/signup">
                                <Button className="bg-primary hover:bg-primary/90 text-primary-foreground px-8 py-6 rounded-full font-medium shadow-lg">
                                    Get Started Free <ArrowRight className="w-4 h-4 ml-2" />
                                </Button>
                            </Link>
                        </div>

                        <div className="hidden lg:block">
                            <Card className="bg-background/40 backdrop-blur border-border/20 shadow-lg">
                                <CardContent className="p-5">
                                    <div className="grid grid-cols-3 gap-3 mb-4">
                                        <div className="bg-background/50 rounded-lg p-3">
                                            <p className="text-xl font-bold text-foreground">$37.8k</p>
                                            <p className="text-xs text-muted-foreground">Revenue</p>
                                        </div>
                                        <div className="bg-background/50 rounded-lg p-3">
                                            <p className="text-xl font-bold text-foreground">12.4k</p>
                                            <p className="text-xs text-muted-foreground">Clicks</p>
                                        </div>
                                        <div className="bg-background/50 rounded-lg p-3">
                                            <p className="text-xl font-bold text-foreground flex items-center gap-1">84% <TrendingUp className="w-3 h-3 text-emerald-500" /></p>
                                            <p className="text-xs text-muted-foreground">Growth</p>
                                        </div>
                                    </div>
                                    <div className="flex gap-1 h-16">
                                        {[30, 45, 35, 60, 50, 70, 55, 80, 65].map((h, i) => (
                                            <div key={i} className="flex-1 bg-gradient-to-t from-indigo-500 to-indigo-400 rounded-t" style={{ height: `${h}%` }} />
                                        ))}
                                    </div>
                                </CardContent>
                            </Card>
                        </div>
                    </motion.div>
                </div>
            </div>
        </section>
    );
};
