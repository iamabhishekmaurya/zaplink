'use client';

import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { BarChart3, Link as LinkIcon, QrCode, Shield } from 'lucide-react';
import { motion } from 'motion/react';

const benefits = [
    {
        icon: LinkIcon,
        title: "Smart Link Management",
        description: "Manage and track all your links in one powerful dashboard."
    },
    {
        icon: QrCode,
        title: "Dynamic QR Codes",
        description: "Create customizable QR codes that update in real-time."
    },
    {
        icon: BarChart3,
        title: "Advanced Analytics",
        description: "Track clicks, locations, and devices with detailed insights."
    },
    {
        icon: Shield,
        title: "Enterprise Security",
        description: "Bank-grade encryption and password protection."
    }
];

export const BenefitsSection = () => {
    return (
        <section className="py-36 bg-gradient-to-b from-background to-muted/50 relative overflow-hidden">
            {/* Decorative gradient orb */}
            <div className="absolute -top-40 -right-40 w-80 h-80 bg-gradient-to-br from-primary/20 to-secondary/20 rounded-full blur-3xl" />

            <div className="max-w-7xl mx-auto px-4 md:px-6 relative z-10">
                {/* Header - Moved separate for better alignment */}
                <div className="max-w-2xl mb-12">
                    <motion.div
                        initial={{ opacity: 0, x: -50 }}
                        whileInView={{ opacity: 1, x: 0 }}
                        viewport={{ once: true }}
                        transition={{ duration: 0.6 }}
                    >
                        <span className="text-sm font-medium text-primary mb-2 block">Benefits</span>
                        <h2 className="text-3xl md:text-4xl font-bold text-foreground leading-tight">
                            Why users love our<br />
                            <span className="font-[family-name:var(--font-script)] text-primary">AI-Powered</span> dashboard
                        </h2>
                    </motion.div>
                </div>

                <div className="grid lg:grid-cols-2 gap-12 items-start">
                    {/* Left - Cards Grid */}
                    <div className="grid sm:grid-cols-2 gap-6">
                        {benefits.map((benefit, index) => (
                            <motion.div
                                key={index}
                                initial={{ opacity: 0, y: 20 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                viewport={{ once: true }}
                                transition={{ delay: index * 0.1, duration: 0.5 }}
                            >
                                <Card className="bg-card border-border/50 shadow-sm hover:shadow-md transition-shadow h-full">
                                    <CardContent className="p-5">
                                        <div className="w-10 h-10 rounded-lg bg-orange-100 dark:bg-orange-900/20 flex items-center justify-center mb-4">
                                            <benefit.icon className="w-5 h-5 text-orange-600 dark:text-orange-400" />
                                        </div>
                                        <h3 className="font-semibold text-foreground mb-2">{benefit.title}</h3>
                                        <p className="text-sm text-muted-foreground">{benefit.description}</p>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        ))}
                    </div>

                    {/* Right - Dashboard Preview */}
                    <div className="relative">
                        <Card className="bg-card shadow-xl border-border/50 overflow-hidden">
                            <CardContent className="p-0">
                                {/* Browser Chrome */}
                                <div className="flex items-center gap-2 p-3 bg-muted/50 border-b border-border">
                                    <div className="flex gap-1.5">
                                        <div className="w-3 h-3 rounded-full bg-rose-400" />
                                        <div className="w-3 h-3 rounded-full bg-amber-400" />
                                        <div className="w-3 h-3 rounded-full bg-emerald-400" />
                                    </div>
                                    <div className="flex-1 mx-4">
                                        <div className="bg-background rounded px-3 py-1.5 text-xs text-muted-foreground flex items-center gap-2">
                                            zaipmeo <span className="text-muted-foreground/50">·</span> 25.4k visits
                                        </div>
                                    </div>
                                </div>
                                {/* Mock Content */}
                                <div className="p-6">
                                    <div className="flex items-center justify-between mb-4">
                                        <span className="font-semibold text-foreground">QR Code Design</span>
                                        <Badge variant="secondary" className="text-xs">Preview</Badge>
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div className="aspect-square bg-muted rounded-xl flex items-center justify-center">
                                            <QrCode className="w-16 h-16 text-muted-foreground/50" />
                                        </div>
                                        <div className="space-y-3">
                                            <div className="bg-muted/50 rounded-lg p-3">
                                                <p className="text-xs text-muted-foreground mb-2">I would recommend using more branding elements.</p>
                                                <Button size="sm" className="w-full bg-primary hover:bg-primary/90 text-primary-foreground text-xs">Apply</Button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                </div>
            </div>
        </section>
    );
};
