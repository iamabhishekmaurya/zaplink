'use client';

import MainLayout from '@/components/layout/MainLayout';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import {
    Zap,
    BarChart3,
    ShieldCheck,
    Globe,
    Link2,
    Smartphone,
    Code2,
    Clock,
    Activity,
    Layers,
    Fingerprint,
    Cpu,
    Rocket,
    Users,
    Target,
    Database,
    Eye
} from 'lucide-react';
import { motion } from 'framer-motion';

export default function FeaturesPage() {
    const coreFeatures = [
        {
            title: 'Advanced Analytics',
            description: 'Get deep insights into your traffic with real-time data on clicks, geography, devices, and referrers.',
            icon: BarChart3,
            gradient: 'from-blue-500 to-cyan-500',
            features: ['Real-time dashboard', 'Geographic insights', 'Device tracking', 'Conversion analytics']
        },
        {
            title: 'Lightning Fast Redirects',
            description: 'Our global edge network ensures your links resolve in milliseconds, providing a seamless user experience.',
            icon: Zap,
            gradient: 'from-yellow-400 to-orange-500',
            features: ['< 50ms response time', 'Global CDN', 'Auto-scaling', '99.99% uptime']
        },
        {
            title: 'Enterprise Security',
            description: 'Automatic scanning for malware and phishing protects you and your audience from digital threats.',
            icon: ShieldCheck,
            gradient: 'from-green-500 to-emerald-500',
            features: ['SSL encryption', 'Malware scanning', 'GDPR compliant', '2FA authentication']
        },
        {
            title: 'Global Infrastructure',
            description: 'Redundant servers across the globe ensure maximum uptime and low latency for every single click.',
            icon: Globe,
            gradient: 'from-purple-500 to-pink-500',
            features: ['100+ edge locations', 'Load balancing', 'Failover protection', 'Global CDN']
        }
    ];

    const advancedFeatures = [
        {
            title: 'Custom Branded Links',
            description: 'Use your own domain to build trust and increase click-through rates by up to 34%.',
            icon: Link2,
            gradient: 'from-rose-500 to-pink-500',
            features: ['Custom domains', 'Branded URLs', 'SSL certificates', 'DNS management']
        },
        {
            title: 'Device Targeting',
            description: 'Redirect users based on their device, operating system, or language for targeted marketing.',
            icon: Smartphone,
            gradient: 'from-indigo-500 to-blue-500',
            features: ['Device detection', 'OS targeting', 'Language routing', 'Geo-targeting']
        },
        {
            title: 'API First Architecture',
            description: 'Integrate link shortening into your own applications with our powerful and well-documented API.',
            icon: Code2,
            gradient: 'from-cyan-500 to-blue-500',
            features: ['RESTful API', 'Webhooks', 'SDKs', 'Developer docs']
        },
        {
            title: 'Smart Expiration',
            description: 'Set your links to expire at a specific time or after a certain number of clicks for temporary offers.',
            icon: Clock,
            gradient: 'from-orange-500 to-red-500',
            features: ['Time-based expiry', 'Click limits', 'Scheduled campaigns', 'Auto-renewal']
        },
        {
            title: 'Real-time Monitoring',
            description: 'Watch your campaign performance live with our interactive dashboard and instant notifications.',
            icon: Activity,
            gradient: 'from-emerald-500 to-green-500',
            features: ['Live metrics', 'Alert system', 'Performance reports', 'Custom dashboards']
        },
        {
            title: 'Audience Targeting',
            description: 'Deliver personalized experiences based on user behavior, location, and device preferences.',
            icon: Target,
            gradient: 'from-violet-500 to-purple-500',
            features: ['Behavioral targeting', 'A/B testing', 'Personalization', 'Segmentation']
        }
    ];

    const infrastructure = [
        {
            title: 'Multi-layer Caching',
            description: 'Ultra-low latency redirects using our global caching engine.',
            icon: Layers,
            gradient: 'from-blue-500 to-cyan-500'
        },
        {
            title: 'Anti-Fraud Engine',
            description: 'Sophisticated detection algorithms to filter out bots and malicious traffic.',
            icon: Fingerprint,
            gradient: 'from-purple-500 to-pink-500'
        },
        {
            title: 'Serverless Architecture',
            description: 'Automatically scales to handle millions of requests per second.',
            icon: Cpu,
            gradient: 'from-green-500 to-emerald-500'
        },
        {
            title: 'Real-time Analytics',
            description: 'Process billions of events with sub-second latency.',
            icon: Database,
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
                                <Rocket className="w-4 h-4 text-yellow-300" />
                                <span className="text-white text-sm font-medium">Enterprise-grade features</span>
                            </div>

                            <h1 className="text-4xl md:text-7xl font-bold font-display text-white leading-tight">
                                Everything You Need to
                                <br />
                                <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-300 via-pink-300 to-purple-300">
                                    Grow Your Brand
                                </span>
                            </h1>

                            <p className="text-xl md:text-2xl text-white/80 max-w-2xl mx-auto leading-relaxed">
                                Powerful links, deep insights, and unbeatable security.
                                Built for high-performance teams that demand excellence.
                            </p>
                        </motion.div>
                    </div>
                </div>
            </section>

            {/* Core Features Grid */}
            <section className="py-24 bg-gradient-to-br from-background to-muted/20 relative">
                {/* Background Pattern */}
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_70%,_rgba(102,126,234,0.1)_0%,_transparent_50%)]" />

                <div className="container mx-auto px-4 relative z-10">
                    <div className="text-center max-w-3xl mx-auto mb-20">
                        <motion.h2
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6 }}
                            className="text-4xl md:text-6xl font-bold font-display mb-6"
                        >
                            <span className="text-gradient">Core Features</span> That Power Success
                        </motion.h2>
                        <motion.p
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.1 }}
                            className="text-xl text-muted-foreground leading-relaxed"
                        >
                            Industry-leading tools designed to maximize your link performance and ROI
                        </motion.p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                        {coreFeatures.map((feature, i) => (
                            <motion.div
                                key={i}
                                initial={{ opacity: 0, y: 40 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                viewport={{ once: true }}
                                transition={{ duration: 0.6, delay: i * 0.1 }}
                                whileHover={{ y: -8 }}
                                className="group"
                            >
                                <Card className="h-full glass-card border-0 shadow-2xl hover:shadow-3xl transition-all duration-300 rounded-3xl overflow-hidden">
                                    <CardContent className="p-8 flex flex-col gap-6">
                                        <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${feature.gradient} mb-4 shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                                            <feature.icon className="h-8 w-8 text-white" />
                                        </div>

                                        <h3 className="text-2xl font-bold text-gradient">{feature.title}</h3>
                                        <p className="text-muted-foreground leading-relaxed">{feature.description}</p>

                                        <div className="space-y-2 mt-auto">
                                            {feature.features.map((item, idx) => (
                                                <div key={idx} className="flex items-center gap-2 text-sm text-muted-foreground">
                                                    <div className="w-1.5 h-1.5 rounded-full bg-primary" />
                                                    {item}
                                                </div>
                                            ))}
                                        </div>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Advanced Features */}
            <section className="py-24 bg-gradient-to-br from-muted/20 to-background">
                <div className="container mx-auto px-4">
                    <div className="text-center max-w-3xl mx-auto mb-20">
                        <motion.h2
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6 }}
                            className="text-4xl md:text-6xl font-bold font-display mb-6"
                        >
                            <span className="text-gradient">Advanced Capabilities</span> for Power Users
                        </motion.h2>
                        <motion.p
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.1 }}
                            className="text-xl text-muted-foreground leading-relaxed"
                        >
                            Sophisticated tools that give you complete control over your link strategy
                        </motion.p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                        {advancedFeatures.map((feature, i) => (
                            <motion.div
                                key={i}
                                initial={{ opacity: 0, y: 40 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                viewport={{ once: true }}
                                transition={{ duration: 0.6, delay: i * 0.1 }}
                                whileHover={{ y: -8 }}
                                className="group"
                            >
                                <Card className="h-full glass-card border-0 shadow-2xl hover:shadow-3xl transition-all duration-300 rounded-3xl overflow-hidden">
                                    <CardContent className="p-8 flex flex-col gap-6">
                                        <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${feature.gradient} mb-4 shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                                            <feature.icon className="h-8 w-8 text-white" />
                                        </div>

                                        <h3 className="text-2xl font-bold text-gradient">{feature.title}</h3>
                                        <p className="text-muted-foreground leading-relaxed">{feature.description}</p>

                                        <div className="space-y-2 mt-auto">
                                            {feature.features.map((item, idx) => (
                                                <div key={idx} className="flex items-center gap-2 text-sm text-muted-foreground">
                                                    <div className="w-1.5 h-1.5 rounded-full bg-primary" />
                                                    {item}
                                                </div>
                                            ))}
                                        </div>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Infrastructure Section */}
            <section className="py-24 bg-gradient-to-br from-background to-muted/20 relative">
                <div className="container mx-auto px-4">
                    <div className="bg-gradient-to-br from-primary/5 to-accent/5 rounded-3xl p-12 md:p-20 border border-white/10 shadow-2xl">
                        <div className="grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">
                            <div className="flex flex-col gap-8">
                                <motion.h2
                                    initial={{ opacity: 0, x: -20 }}
                                    whileInView={{ opacity: 1, x: 0 }}
                                    transition={{ duration: 0.6 }}
                                    className="text-3xl md:text-5xl font-bold font-display tracking-tight"
                                >
                                    Built for Scale and
                                    <span className="text-gradient"> Performance</span>
                                </motion.h2>

                                <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                                    {infrastructure.map((item, i) => (
                                        <motion.div
                                            key={i}
                                            initial={{ opacity: 0, y: 20 }}
                                            whileInView={{ opacity: 1, y: 0 }}
                                            transition={{ duration: 0.6, delay: i * 0.1 }}
                                            className="flex items-start gap-4"
                                        >
                                            <div className={`inline-flex items-center justify-center w-12 h-12 rounded-xl bg-gradient-to-r ${item.gradient} flex-shrink-0`}>
                                                <item.icon className="h-6 w-6 text-white" />
                                            </div>
                                            <div>
                                                <h4 className="font-bold text-lg mb-1">{item.title}</h4>
                                                <p className="text-muted-foreground text-sm">{item.description}</p>
                                            </div>
                                        </motion.div>
                                    ))}
                                </div>
                            </div>

                            <motion.div
                                initial={{ opacity: 0, x: 20 }}
                                whileInView={{ opacity: 1, x: 0 }}
                                transition={{ duration: 0.6 }}
                                className="relative"
                            >
                                <div className="relative h-[400px] bg-gradient-to-br from-background to-muted/30 rounded-3xl border border-white/20 shadow-2xl overflow-hidden group">
                                    <div className="absolute inset-0 bg-gradient-to-br from-primary/5 to-transparent" />
                                    <div className="absolute inset-0 flex items-center justify-center">
                                        <Eye className="h-32 w-32 text-primary/20 animate-pulse" />
                                    </div>
                                    <div className="p-8 relative">
                                        <div className="w-full h-8 bg-white/50 rounded-lg mb-4 flex items-center px-4 gap-2 backdrop-blur-sm">
                                            <div className="w-2 h-2 rounded-full bg-red-400" />
                                            <div className="w-2 h-2 rounded-full bg-yellow-400" />
                                            <div className="w-2 h-2 rounded-full bg-green-400" />
                                        </div>
                                        <div className="space-y-4">
                                            <div className="h-4 w-3/4 bg-white/30 rounded animate-pulse backdrop-blur-sm" />
                                            <div className="h-4 w-1/2 bg-white/30 rounded animate-pulse backdrop-blur-sm" />
                                            <div className="h-20 w-full bg-primary/10 rounded-xl border border-primary/20 flex flex-col justify-center px-6 gap-2 backdrop-blur-sm">
                                                <div className="h-2 w-1/4 bg-primary/30 rounded" />
                                                <div className="h-3 w-1/2 bg-primary/40 rounded" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </motion.div>
                        </div>
                    </div>
                </div>
            </section>

            {/* CTA Section */}
            <section className="py-32 relative overflow-hidden">
                <div className="absolute inset-0 gradient-primary opacity-95" />
                <div className="absolute inset-0 gradient-secondary opacity-30 animate-pulse" />

                <div className="container mx-auto px-4 relative z-10">
                    <div className="max-w-4xl mx-auto text-center">
                        <motion.div
                            initial={{ opacity: 0, y: 30 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8 }}
                            className="space-y-8"
                        >
                            <h2 className="text-4xl md:text-6xl font-bold font-display text-white leading-tight">
                                Ready to Experience
                                <br />
                                <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-300 via-pink-300 to-purple-300">
                                    Enterprise Power?
                                </span>
                            </h2>

                            <p className="text-xl md:text-2xl text-white/80 max-w-2xl mx-auto leading-relaxed">
                                Join thousands of businesses that trust Zaplink for their critical link management needs.
                            </p>

                            <div className="flex flex-col sm:flex-row gap-6 justify-center items-center">
                                <Button size="lg" className="h-16 px-12 text-lg font-semibold font-display bg-white text-primary hover:bg-white/95 hover:scale-105 transition-all duration-300 rounded-[2rem] shadow-2xl group">
                                    <Rocket className="w-5 h-5 mr-3 group-hover:rotate-12 transition-transform" />
                                    Start Free Trial
                                </Button>
                                <Button size="lg" variant="outline" className="h-16 px-12 text-lg font-semibold font-display bg-transparent border-white/40 text-white hover:bg-white/10 hover:border-white/60 hover:scale-105 transition-all duration-300 rounded-[2rem] backdrop-blur-sm group">
                                    <Users className="w-5 h-5 mr-3 group-hover:scale-110 transition-transform" />
                                    Schedule Demo
                                </Button>
                            </div>
                        </motion.div>
                    </div>
                </div>
            </section>
        </MainLayout>
    );
}
