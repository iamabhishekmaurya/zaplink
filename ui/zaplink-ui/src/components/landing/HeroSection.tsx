'use client';

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { ArrowRight, Link as LinkIcon, Plus, Search, TrendingDown, TrendingUp, Zap } from "lucide-react";
import { motion } from "motion/react";
import Link from 'next/link';

export const HeroSection = () => {
    return (
        <section className="relative min-h-screen overflow-hidden bg-gradient-to-br from-violet-600/30 via-background to-[#ff8904]/30 m-2 md:m-4 rounded-2xl">
            {/* Soft radial gradient overlay */}
            <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-violet-600/20 via-transparent to-[#ff8904]/10" />

            <div className="relative z-10 max-w-7xl mx-auto px-4 md:px-6 lg:px-8">
                {/* Hero Text - with enough top padding for floating navbar */}
                <div className="text-center max-w-3xl mx-auto pt-32 md:pt-40">
                    <motion.h1
                        className="text-4xl md:text-5xl lg:text-6xl font-bold text-slate-800 dark:text-white leading-tight mb-6"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6 }}
                    >
                        AI-Driven link management with{" "}
                        <span className="font-[family-name:var(--font-script)] font-normal text-primary">human-level precision</span>
                    </motion.h1>

                    <motion.p
                        className="text-slate-600 dark:text-slate-400 text-lg md:text-xl max-w-2xl mx-auto mb-8"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.1 }}
                    >
                        Empower your business with AI-driven teams that execute tasks with
                        human-level precision, efficiency, and reliability.
                    </motion.p>
                </div>

                {/* Floating Cards Layout */}
                <motion.div
                    className="relative mt-8 w-full max-w-6xl mx-auto h-[550px] md:h-[500px] pb-16"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.8, delay: 0.2 }}
                >
                    {/* Percentage Badge - Top Left */}
                    <motion.div
                        className="absolute left-[2%] top-[8%] z-20"
                        initial={{ opacity: 0, scale: 0.8 }}
                        animate={{
                            opacity: 1,
                            scale: 1,
                            y: [0, -8, 0]
                        }}
                        transition={{
                            opacity: { delay: 0.4 },
                            scale: { delay: 0.4 },
                            y: { duration: 3, repeat: Infinity, ease: "easeInOut" }
                        }}
                    >
                        <div className="bg-card rounded-2xl shadow-lg px-4 py-3 flex items-center gap-2 border border-border/50">
                            <span className="text-2xl font-bold text-rose-500">48%</span>
                            <TrendingDown className="w-5 h-5 text-rose-500" />
                        </div>
                    </motion.div>

                    {/* Percentage Badge - Top Right */}
                    <motion.div
                        className="absolute right-[5%] top-[3%] z-20"
                        initial={{ opacity: 0, scale: 0.8 }}
                        animate={{
                            opacity: 1,
                            scale: 1,
                            y: [0, -10, 0]
                        }}
                        transition={{
                            opacity: { delay: 0.5 },
                            scale: { delay: 0.5 },
                            y: { duration: 4, repeat: Infinity, ease: "easeInOut", delay: 0.5 }
                        }}
                    >
                        <div className="bg-card rounded-2xl shadow-lg px-4 py-3 flex items-center gap-2 border border-border/50">
                            <span className="text-2xl font-bold text-primary">68%</span>
                            <TrendingUp className="w-5 h-5 text-emerald-500" />
                        </div>
                    </motion.div>

                    {/* New Chat Card - Left Side */}
                    <motion.div
                        className="absolute left-[0%] top-[22%] z-10 hidden md:block"
                        initial={{ opacity: 0, x: -30 }}
                        animate={{
                            opacity: 1,
                            x: 0,
                            y: [0, 12, 0]
                        }}
                        transition={{
                            opacity: { delay: 0.5 },
                            x: { delay: 0.5 },
                            y: { duration: 5, repeat: Infinity, ease: "easeInOut", delay: 1 }
                        }}
                    >
                        <Card className="w-56 bg-card shadow-xl border-border/50">
                            <CardContent className="p-4">
                                <div className="flex items-center gap-3 mb-3">
                                    <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                                        <Plus className="w-4 h-4 text-white" />
                                    </div>
                                    <span className="font-medium text-foreground text-sm">New chat</span>
                                </div>
                                <div className="bg-muted/50 rounded-lg p-3">
                                    <p className="text-xs text-muted-foreground leading-relaxed">
                                        Provide a summary of my company's latest metrics...
                                    </p>
                                </div>
                            </CardContent>
                        </Card>
                    </motion.div>

                    {/* Sales Card - Bottom Left */}
                    <motion.div
                        className="absolute left-[3%] bottom-[12%] z-10 hidden md:block"
                        initial={{ opacity: 0, y: 30 }}
                        animate={{
                            opacity: 1,
                            y: [0, -10, 0]
                        }}
                        transition={{
                            opacity: { delay: 0.6 },
                            y: { duration: 4, repeat: Infinity, ease: "easeInOut", delay: 0.8 }
                        }}
                    >
                        <Card className="w-52 bg-card shadow-xl border-border/50">
                            <CardContent className="p-4">
                                <div className="flex items-center gap-2 mb-2">
                                    <div className="w-6 h-6 rounded bg-gradient-to-br from-violet-500 to-fuchsia-500" />
                                    <span className="text-xs text-muted-foreground">Sales</span>
                                </div>
                                <div className="text-2xl font-bold text-foreground">$7,854.21</div>
                                <div className="text-xs text-muted-foreground mt-1">Previous: $1,243.00</div>
                            </CardContent>
                        </Card>
                    </motion.div>

                    {/* Central Search Bar */}
                    <motion.div
                        className="absolute left-1/2 top-[45%] -translate-x-1/2 -translate-y-1/2 z-30 w-full max-w-md px-4"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: 0.3 }}
                    >
                        <div className="bg-card rounded-full shadow-2xl border border-border px-6 py-4 flex items-center gap-3">
                            <Search className="w-5 h-5 text-muted-foreground" />
                            <span className="text-muted-foreground text-sm flex-1">Ask or search for anything...</span>
                            <div className="w-8 h-8 rounded-full bg-gradient-to-br from-orange-400 to-rose-400" />
                        </div>
                    </motion.div>

                    {/* Series Revenue Card - Right Side */}
                    <motion.div
                        className="absolute right-[0%] top-[18%] z-10 hidden md:block"
                        initial={{ opacity: 0, x: 30 }}
                        animate={{
                            opacity: 1,
                            x: 0,
                            y: [0, 10, 0]
                        }}
                        transition={{
                            opacity: { delay: 0.5 },
                            x: { delay: 0.5 },
                            y: { duration: 4.5, repeat: Infinity, ease: "easeInOut", delay: 0.3 }
                        }}
                    >
                        <Card className="w-56 bg-card shadow-xl border-border/50">
                            <CardContent className="p-4">
                                <div className="flex items-center justify-between mb-2">
                                    <span className="text-sm font-medium text-foreground">Series Revenue</span>
                                    <Badge className="bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400 text-xs">+12%</Badge>
                                </div>
                                <div className="text-2xl font-bold text-foreground mb-3">$4,688.82</div>
                                <div className="flex gap-1 h-10">
                                    {[35, 55, 40, 70, 50, 65, 80].map((h, i) => (
                                        <div
                                            key={i}
                                            className="flex-1 bg-gradient-to-t from-indigo-500 to-indigo-400 rounded-sm"
                                            style={{ height: `${h}%` }}
                                        />
                                    ))}
                                </div>
                            </CardContent>
                        </Card>
                    </motion.div>

                    {/* Pie Chart Card - Bottom Right */}
                    <motion.div
                        className="absolute right-[10%] bottom-[10%] z-10 hidden md:block"
                        initial={{ opacity: 0, scale: 0.9 }}
                        animate={{
                            opacity: 1,
                            scale: 1,
                            y: [0, -8, 0]
                        }}
                        transition={{
                            opacity: { delay: 0.7 },
                            scale: { delay: 0.7 },
                            y: { duration: 3.5, repeat: Infinity, ease: "easeInOut", delay: 1.2 }
                        }}
                    >
                        <Card className="w-32 bg-card shadow-xl border-border/50">
                            <CardContent className="p-3 flex flex-col items-center">
                                <div className="relative w-16 h-16">
                                    <svg className="w-full h-full -rotate-90">
                                        <circle cx="32" cy="32" r="28" fill="none" stroke="currentColor" className="text-muted" strokeWidth="6" />
                                        <circle
                                            cx="32"
                                            cy="32"
                                            r="28"
                                            fill="none"
                                            stroke="url(#gradient)"
                                            strokeWidth="6"
                                            strokeDasharray="176"
                                            strokeDashoffset="21"
                                            strokeLinecap="round"
                                        />
                                        <defs>
                                            <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="0%">
                                                <stop offset="0%" stopColor="#f472b6" />
                                                <stop offset="100%" stopColor="#fb923c" />
                                            </linearGradient>
                                        </defs>
                                    </svg>
                                    <div className="absolute inset-0 flex items-center justify-center">
                                        <span className="text-sm font-bold text-foreground">88%</span>
                                    </div>
                                </div>
                                <span className="text-xs text-muted-foreground mt-1">Conversion</span>
                            </CardContent>
                        </Card>
                    </motion.div>

                    {/* CTA Buttons at Bottom */}
                    <motion.div
                        className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-4 z-40"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: 0.8 }}
                    >
                        <Link href="/signup">
                            <Button className="bg-primary hover:bg-primary/90 text-primary-foreground px-8 py-6 text-base font-medium rounded-full shadow-lg">
                                Try for free
                            </Button>
                        </Link>
                        <Link href="/contact">
                            <Button variant="outline" className="border-border text-foreground hover:bg-muted/50 px-8 py-6 text-base font-medium rounded-full">
                                Request a Demo
                            </Button>
                        </Link>
                    </motion.div>
                </motion.div>
            </div>
        </section>
    );
};
