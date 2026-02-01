'use client';

import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Check, X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

const tiers = [
    {
        name: 'Starter',
        description: 'Perfect for side projects and small teams.',
        price: { monthly: 0, yearly: 0 },
        features: [
            'Up to 50 links/month',
            'Basic analytics',
            'Standard support',
            '1 Custom domain',
        ],
        notIncluded: [
            'Team collaboration',
            'API Access',
            'SSO Authentication',
        ]
    },
    {
        name: 'Pro',
        description: 'For growing businesses needing more power.',
        price: { monthly: 29, yearly: 24 },
        popular: true,
        features: [
            'Unlimited links',
            'Advanced analytics',
            'Priority support',
            '5 Custom domains',
            'Team collaboration',
            'API Access',
        ],
        notIncluded: [
            'SSO Authentication',
        ]
    },
    {
        name: 'Business',
        description: 'Full control and support for large scale needs.',
        price: { monthly: 99, yearly: 82 },
        features: [
            'Unlimited everything',
            'Real-time analytics',
            '24/7 Dedicated support',
            'Unlimited domains',
            'SSO Authentication',
            'SLA Guarantee',
            'Audit logs',
        ],
        notIncluded: []
    },
];

export default function PricingTiers() {
    const [isYearly, setIsYearly] = useState(true);

    return (
        <section className="py-20 relative px-4">
            {/* Background Glow */}
            <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full max-w-3xl h-full bg-primary/5 blur-[120px] rounded-full pointer-events-none" />

            <div className="max-w-7xl mx-auto relative z-10">

                {/* Toggle */}
                <div className="flex justify-center mb-16">
                    <div className="bg-muted p-1 rounded-full flex items-center relative">
                        <button
                            onClick={() => setIsYearly(false)}
                            className={cn(
                                "px-6 py-2 rounded-full text-sm font-medium transition-all duration-300 relative z-10",
                                !isYearly ? "text-foreground" : "text-muted-foreground"
                            )}
                        >
                            Monthly
                        </button>
                        <button
                            onClick={() => setIsYearly(true)}
                            className={cn(
                                "px-6 py-2 rounded-full text-sm font-medium transition-all duration-300 relative z-10",
                                isYearly ? "text-foreground" : "text-muted-foreground"
                            )}
                        >
                            Yearly
                            <span className="absolute -top-3 -right-6 text-[10px] bg-emerald-500 text-white px-2 py-0.5 rounded-full font-bold animate-pulse">
                                -20%
                            </span>
                        </button>

                        {/* Sliding Indicator */}
                        <div
                            className={cn(
                                "absolute top-1 bottom-1 w-[calc(50%-4px)] bg-background rounded-full shadow-sm transition-all duration-300",
                                isYearly ? "left-[calc(50%)]" : "left-1"
                            )}
                        />
                    </div>
                </div>

                {/* Cards */}
                <div className="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
                    {tiers.map((tier, index) => (
                        <motion.div
                            key={tier.name}
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            viewport={{ once: true }}
                            transition={{ delay: index * 0.1 }}
                            className={cn(
                                "relative group rounded-3xl p-8 border backdrop-blur-sm transition-all duration-300 hover:shadow-2xl hover:shadow-primary/5",
                                tier.popular
                                    ? "bg-primary/5 border-primary/50 shadow-xl shadow-primary/10 scale-105 z-10"
                                    : "bg-card/50 border-border/50 hover:border-primary/20"
                            )}
                        >
                            {tier.popular && (
                                <div className="absolute -top-4 left-1/2 -translate-x-1/2 bg-gradient-to-r from-primary to-violet-600 text-white px-4 py-1 rounded-full text-sm font-bold shadow-lg">
                                    Most Popular
                                </div>
                            )}

                            <div className="mb-6">
                                <h3 className="text-xl font-bold mb-2">{tier.name}</h3>
                                <p className="text-sm text-muted-foreground h-10">{tier.description}</p>
                            </div>

                            <div className="mb-8 flex items-baseline gap-1">
                                <span className="text-4xl font-bold">
                                    ${isYearly ? tier.price.yearly : tier.price.monthly}
                                </span>
                                <span className="text-muted-foreground">/mo</span>
                            </div>

                            <Button
                                variant={tier.popular ? "default" : "outline"}
                                className={cn(
                                    "w-full mb-8 rounded-xl h-11",
                                    tier.popular ? "bg-gradient-to-r from-primary to-violet-600 hover:opacity-90 transition-opacity shadow-lg shadow-primary/25" : ""
                                )}
                            >
                                {tier.price.monthly === 0 ? 'Get Started' : 'Start Free Trial'}
                            </Button>

                            <div className="space-y-4">
                                <div className="text-xs font-semibold uppercase tracking-wider text-muted-foreground mb-4">
                                    What's included
                                </div>

                                {tier.features.map((feature) => (
                                    <div key={feature} className="flex items-start gap-3 text-sm">
                                        <Check className="w-4 h-4 text-emerald-500 mt-0.5 shrink-0" />
                                        <span>{feature}</span>
                                    </div>
                                ))}

                                {tier.notIncluded.map((feature) => (
                                    <div key={feature} className="flex items-start gap-3 text-sm text-muted-foreground/60">
                                        <X className="w-4 h-4 mt-0.5 shrink-0" />
                                        <span className="line-through">{feature}</span>
                                    </div>
                                ))}
                            </div>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
}
