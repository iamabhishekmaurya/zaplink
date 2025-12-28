'use client';

import MainLayout from '@/components/layout/MainLayout';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from '@/components/ui/card';
import { Check, Zap, Shield, BarChart2, Globe, Command, Sparkles, Crown, Rocket } from 'lucide-react';
import { motion } from 'framer-motion';
import Link from 'next/link';

export default function PricingPage() {
    const plans = [
        {
            name: 'Starter',
            price: '$0',
            description: 'Perfect for individuals and side projects getting started.',
            features: [
                '50 Short links per month',
                'Basic link analytics',
                'Standard redirects',
                'Zaplink domain (zap.lk)',
                'Email support',
                'Mobile app access'
            ],
            cta: 'Get Started Free',
            href: '/signup',
            highlight: false,
            icon: Rocket,
            gradient: 'from-blue-500 to-cyan-500'
        },
        {
            name: 'Professional',
            price: '$12',
            period: '/mo',
            description: 'The perfect choice for growing brands and businesses.',
            features: [
                'Unlimited short links',
                'Advanced analytics dashboard',
                'Custom domain support',
                'Bulk URL shortening',
                'QR Code generation',
                'Priority email support',
                'API access',
                'Team collaboration'
            ],
            cta: 'Start Free Trial',
            href: '/signup',
            highlight: true,
            icon: Sparkles,
            gradient: 'from-purple-500 to-pink-500'
        },
        {
            name: 'Enterprise',
            price: 'Custom',
            description: 'Dedicated solutions for large organizations and teams.',
            features: [
                'Everything in Professional',
                'SSO/SAML Authentication',
                'Dedicated account manager',
                'Custom API limits',
                'SLA & 99.99% uptime',
                '24/7 Phone support',
                'White-label options',
                'Custom integrations'
            ],
            cta: 'Contact Sales',
            href: '#contact',
            highlight: false,
            icon: Crown,
            gradient: 'from-orange-500 to-red-500'
        }
    ];

    return (
        <MainLayout>
            {/* Hero Section - Stunning Gradient */}
            <section className="relative py-32 overflow-hidden">
                <div className="absolute inset-0 gradient-primary opacity-95" />
                <div className="absolute inset-0 gradient-secondary opacity-30 animate-pulse" />

                {/* Floating Elements */}
                <motion.div
                    className="absolute top-20 left-20 w-32 h-32 bg-white/10 rounded-full blur-2xl float-animation"
                    initial={{ opacity: 0 }}
                    whileInView={{ opacity: 0.6 }}
                />
                <motion.div
                    className="absolute bottom-20 right-20 w-24 h-24 bg-accent/10 rounded-full blur-xl float-animation"
                    initial={{ opacity: 0 }}
                    whileInView={{ opacity: 0.4 }}
                    style={{ animationDelay: '2s' }}
                />

                <div className="container mx-auto px-4 relative z-10">
                    <div className="text-center max-w-4xl mx-auto">
                        <motion.div
                            initial={{ opacity: 0, y: 30 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8 }}
                            className="space-y-8"
                        >
                            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/10 backdrop-blur-sm border border-white/20">
                                <Crown className="w-4 h-4 text-yellow-300" />
                                <span className="text-white text-sm font-medium">Flexible pricing for every scale</span>
                            </div>

                            <h1 className="text-4xl md:text-7xl font-bold font-display text-white leading-tight">
                                Scale with
                                <br />
                                <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-300 via-pink-300 to-purple-300">
                                    Professional Plans
                                </span>
                            </h1>

                            <p className="text-xl md:text-2xl text-white/80 max-w-2xl mx-auto leading-relaxed">
                                Choose the perfect plan for your growth journey.
                                Enterprise performance with transparent pricing.
                            </p>

                            <div className="flex flex-col sm:flex-row gap-4 justify-center items-center pt-4">
                                <div className="flex items-center gap-2 text-white/60">
                                    <Check className="w-5 h-5 text-green-400" />
                                    <span>14-day free trial</span>
                                </div>
                                <div className="flex items-center gap-2 text-white/60">
                                    <Check className="w-5 h-5 text-green-400" />
                                    <span>No credit card required</span>
                                </div>
                                <div className="flex items-center gap-2 text-white/60">
                                    <Check className="w-5 h-5 text-green-400" />
                                    <span>Cancel anytime</span>
                                </div>
                            </div>
                        </motion.div>
                    </div>
                </div>
            </section>

            {/* Pricing Cards - Modern Glass Design */}
            <section className="py-24 bg-gradient-to-br from-background to-muted/20 relative">
                {/* Background Pattern */}
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_30%,_rgba(102,126,234,0.1)_0%,_transparent_50%)]" />

                <div className="container mx-auto px-4 relative z-10">
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 max-w-7xl mx-auto">
                        {plans.map((plan, i) => (
                            <motion.div
                                key={i}
                                initial={{ opacity: 0, y: 40 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                viewport={{ once: true }}
                                transition={{ duration: 0.6, delay: i * 0.1 }}
                                whileHover={{ y: -8 }}
                                className="flex"
                            >
                                <Card className={`w-full flex flex-col transition-all duration-300 glass-card border-0 shadow-2xl hover:shadow-3xl rounded-3xl overflow-hidden relative ${plan.highlight ? 'scale-105 ring-2 ring-white/20' : ''
                                    }`}>
                                    {plan.highlight && (
                                        <div className="absolute top-0 right-0 bg-gradient-to-r from-yellow-400 to-orange-500 text-white text-xs font-bold px-4 py-2 rounded-bl-2xl">
                                            MOST POPULAR
                                        </div>
                                    )}

                                    <CardHeader className="p-8 pb-0 text-center">
                                        <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${plan.gradient} mb-6 shadow-lg mx-auto`}>
                                            <plan.icon className="h-8 w-8 text-white" />
                                        </div>
                                        <CardTitle className="text-2xl font-bold font-display text-gradient mb-2">{plan.name}</CardTitle>
                                        <CardDescription className="text-base text-muted-foreground leading-relaxed">{plan.description}</CardDescription>
                                    </CardHeader>

                                    <CardContent className="p-8 pt-4 flex-grow">
                                        <div className="text-center mb-8">
                                            <div className="flex items-baseline justify-center gap-1">
                                                <span className="text-5xl font-bold font-display text-gradient">{plan.price}</span>
                                                {plan.period && <span className="text-xl text-muted-foreground font-medium">{plan.period}</span>}
                                            </div>
                                        </div>

                                        <ul className="space-y-4 mb-8">
                                            {plan.features.map((feature, j) => (
                                                <li key={j} className="flex items-start gap-3">
                                                    <div className="w-5 h-5 rounded-full bg-gradient-to-r from-primary to-accent flex items-center justify-center flex-shrink-0 mt-0.5">
                                                        <Check className="h-3 w-3 text-white" />
                                                    </div>
                                                    <span className="text-sm font-medium text-foreground">{feature}</span>
                                                </li>
                                            ))}
                                        </ul>
                                    </CardContent>

                                    <CardFooter className="p-8 pt-0">
                                        <Link href={plan.href} className="w-full">
                                            <Button
                                                className={`w-full h-14 text-lg font-semibold font-display rounded-2xl transition-all duration-300 ${plan.highlight
                                                    ? 'gradient-primary text-white hover:shadow-lg hover:scale-105 border-0'
                                                    : 'glass-effect border-white/20 text-white hover:bg-white/10'
                                                    }`}
                                            >
                                                {plan.cta}
                                            </Button>
                                        </Link>
                                    </CardFooter>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Features Included - Modern Grid */}
            <section className="py-24 bg-gradient-to-br from-muted/20 to-background">
                <div className="container mx-auto px-4">
                    <div className="text-center max-w-3xl mx-auto mb-16">
                        <motion.h2
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6 }}
                            className="text-4xl md:text-5xl font-bold font-display mb-6"
                        >
                            <span className="text-gradient">Powerful Features</span> Included in All Plans
                        </motion.h2>
                        <motion.p
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.1 }}
                            className="text-xl text-muted-foreground"
                        >
                            Enterprise-grade infrastructure and security standards across every plan
                        </motion.p>
                    </div>

                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
                        {[
                            {
                                title: 'Secure Redirects',
                                icon: Shield,
                                desc: 'HTTPS encryption everywhere',
                                gradient: 'from-green-500 to-emerald-500'
                            },
                            {
                                title: 'Global Edge Network',
                                icon: Globe,
                                desc: 'Low latency worldwide nodes',
                                gradient: 'from-blue-500 to-cyan-500'
                            },
                            {
                                title: 'Developer API',
                                icon: Command,
                                desc: 'RESTful API & webhooks',
                                gradient: 'from-purple-500 to-pink-500'
                            },
                            {
                                title: '24/7 Support',
                                icon: Zap,
                                desc: 'Round-the-clock help desk',
                                gradient: 'from-orange-500 to-red-500'
                            }
                        ].map((item, i) => (
                            <motion.div
                                key={i}
                                initial={{ opacity: 0, y: 30 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.6, delay: i * 0.1 }}
                                className="text-center group"
                            >
                                <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${item.gradient} mb-6 shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                                    <item.icon className="h-8 w-8 text-white" />
                                </div>
                                <h3 className="text-lg font-bold mb-2 text-gradient">{item.title}</h3>
                                <p className="text-sm text-muted-foreground">{item.desc}</p>
                            </motion.div>
                        ))}
                    </div>
                </div>
            </section>

            {/* FAQ Section */}
            <section className="py-24 bg-gradient-to-br from-background to-muted/20">
                <div className="container mx-auto px-4">
                    <div className="max-w-4xl mx-auto text-center">
                        <motion.h2
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6 }}
                            className="text-3xl md:text-4xl font-bold font-display mb-8"
                        >
                            Frequently Asked <span className="text-gradient">Questions</span>
                        </motion.h2>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 text-left">
                            {[
                                {
                                    q: "Can I change plans anytime?",
                                    a: "Yes! You can upgrade or downgrade your plan at any time. Changes take effect immediately."
                                },
                                {
                                    q: "What happens if I exceed my limits?",
                                    a: "We'll notify you before you hit any limits. You can upgrade or wait for your monthly reset."
                                },
                                {
                                    q: "Do you offer discounts for annual billing?",
                                    a: "Yes! Save 20% with annual billing on all paid plans."
                                },
                                {
                                    q: "Is there a free trial for paid plans?",
                                    a: "All plans include a 14-day free trial with full access to features."
                                }
                            ].map((faq, i) => (
                                <motion.div
                                    key={i}
                                    initial={{ opacity: 0, x: i % 2 === 0 ? -20 : 20 }}
                                    whileInView={{ opacity: 1, x: 0 }}
                                    transition={{ duration: 0.6, delay: i * 0.1 }}
                                    className="glass-card p-6 rounded-2xl border-0 shadow-lg"
                                >
                                    <h3 className="font-bold text-lg mb-2 text-gradient">{faq.q}</h3>
                                    <p className="text-muted-foreground">{faq.a}</p>
                                </motion.div>
                            ))}
                        </div>
                    </div>
                </div>
            </section>
        </MainLayout>
    );
}
