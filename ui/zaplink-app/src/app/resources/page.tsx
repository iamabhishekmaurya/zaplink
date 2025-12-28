'use client';

import MainLayout from '@/components/layout/MainLayout';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import {
    FileText,
    BookOpen,
    HelpCircle,
    Code2,
    Newspaper,
    MessageSquare,
    ArrowRight,
    Video,
    Users,
    Download,
    Play,
    Headphones,
    Award,
    Lightbulb,
    Rocket,
    BarChart3,
    Clock
} from 'lucide-react';
import { motion } from 'framer-motion';

export default function ResourcesPage() {
    const resources = [
        {
            title: 'API Documentation',
            description: 'Integrate Zaplink into your workflow with our comprehensive API guides, SDKs, and interactive playground.',
            icon: Code2,
            tag: 'Developers',
            gradient: 'from-blue-500 to-cyan-500',
            features: ['RESTful API', 'SDKs & Libraries', 'Code Examples', 'Interactive Playground']
        },
        {
            title: 'Help Center',
            description: 'Find answers to frequently asked questions and learn how to use advanced features like a pro.',
            icon: HelpCircle,
            tag: 'Support',
            gradient: 'from-green-500 to-emerald-500',
            features: ['Step-by-step guides', 'Video tutorials', 'Best practices', 'Troubleshooting']
        },
        {
            title: 'Success Stories',
            description: 'See how businesses of all sizes use Zaplink to grow their brand and track real impact.',
            icon: Users,
            tag: 'Case Studies',
            gradient: 'from-purple-500 to-pink-500',
            features: ['Customer stories', 'ROI metrics', 'Industry insights', 'Growth strategies']
        },
        {
            title: 'Developer Blog',
            description: 'Latest updates on our infrastructure, engineering challenges, and product releases from our team.',
            icon: Newspaper,
            tag: 'News',
            gradient: 'from-orange-500 to-red-500',
            features: ['Tech updates', 'Product releases', 'Engineering insights', 'Company news']
        },
        {
            title: 'Video Tutorials',
            description: 'Step-by-step visual guides on setting up custom domains, managing campaigns, and advanced analytics.',
            icon: Video,
            tag: 'Learning',
            gradient: 'from-rose-500 to-pink-500',
            features: ['HD video content', 'Screen recordings', 'Webinar recordings', 'Quick tips']
        },
        {
            title: 'Community Forum',
            description: 'Join the conversation with thousands of other users and share your best practices and insights.',
            icon: MessageSquare,
            tag: 'Community',
            gradient: 'from-indigo-500 to-blue-500',
            features: ['Active discussions', 'Expert answers', 'User tips', 'Networking']
        }
    ];

    const learningPaths = [
        {
            title: 'Getting Started',
            description: 'Perfect for new users. Learn the basics and set up your first campaign.',
            icon: Rocket,
            duration: '15 min',
            level: 'Beginner',
            gradient: 'from-green-500 to-emerald-500'
        },
        {
            title: 'Advanced Analytics',
            description: 'Master our analytics dashboard and data-driven decision making.',
            icon: BarChart3,
            duration: '45 min',
            level: 'Advanced',
            gradient: 'from-blue-500 to-cyan-500'
        },
        {
            title: 'API Integration',
            description: 'Learn to integrate Zaplink into your existing workflows and applications.',
            icon: Code2,
            duration: '60 min',
            level: 'Intermediate',
            gradient: 'from-purple-500 to-pink-500'
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
                                <Lightbulb className="w-4 h-4 text-yellow-300" />
                                <span className="text-white text-sm font-medium">Comprehensive learning hub</span>
                            </div>

                            <h1 className="text-4xl md:text-7xl font-bold font-display text-white leading-tight">
                                Elite
                                <br />
                                <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-300 via-pink-300 to-purple-300">
                                    Resources Center
                                </span>
                            </h1>

                            <p className="text-xl md:text-2xl text-white/80 max-w-2xl mx-auto leading-relaxed">
                                Everything you need to master link management and grow your digital footprint
                                with state-of-the-art tools and expert guidance.
                            </p>
                        </motion.div>
                    </div>
                </div>
            </section>

            {/* Main Resources Grid */}
            <section className="py-24 bg-gradient-to-br from-background to-muted/20 relative">
                {/* Background Pattern */}
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_30%,_rgba(102,126,234,0.1)_0%,_transparent_50%)]" />

                <div className="container mx-auto px-4 relative z-10">
                    <div className="text-center max-w-3xl mx-auto mb-20">
                        <motion.h2
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6 }}
                            className="text-4xl md:text-6xl font-bold font-display mb-6"
                        >
                            <span className="text-gradient">Learning Resources</span> for Every Level
                        </motion.h2>
                        <motion.p
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.1 }}
                            className="text-xl text-muted-foreground leading-relaxed"
                        >
                            From beginner guides to advanced technical documentation, we&apos;ve got you covered
                        </motion.p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                        {resources.map((resource, i) => (
                            <motion.div
                                key={i}
                                initial={{ opacity: 0, y: 40 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                viewport={{ once: true }}
                                transition={{ duration: 0.6, delay: i * 0.1 }}
                                whileHover={{ y: -8 }}
                                className="group"
                            >
                                <Card className="h-full glass-card border-0 shadow-2xl hover:shadow-3xl transition-all duration-300 rounded-3xl overflow-hidden flex flex-col">
                                    <div className="aspect-video relative overflow-hidden">
                                        <div className={`absolute inset-0 bg-gradient-to-br ${resource.gradient} opacity-10`} />
                                        <div className="absolute inset-0 flex items-center justify-center">
                                            <div className={`inline-flex items-center justify-center w-20 h-20 rounded-2xl bg-gradient-to-r ${resource.gradient} shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                                                <resource.icon className="h-10 w-10 text-white" />
                                            </div>
                                        </div>
                                        <div className="absolute top-4 right-4">
                                            <span className="text-xs font-bold px-3 py-1 rounded-full bg-white/10 backdrop-blur-sm border border-white/20 text-white">
                                                {resource.tag}
                                            </span>
                                        </div>
                                    </div>

                                    <CardContent className="p-8 flex flex-col gap-6 flex-grow">
                                        <div className="space-y-4">
                                            <h3 className="text-2xl font-bold font-display text-gradient group-hover:text-primary transition-colors">
                                                {resource.title}
                                            </h3>
                                            <p className="text-muted-foreground leading-relaxed">
                                                {resource.description}
                                            </p>
                                        </div>

                                        <div className="space-y-2 mt-auto">
                                            {resource.features.map((feature, idx) => (
                                                <div key={idx} className="flex items-center gap-2 text-sm text-muted-foreground">
                                                    <div className="w-1.5 h-1.5 rounded-full bg-primary" />
                                                    {feature}
                                                </div>
                                            ))}
                                        </div>

                                        <Button variant="ghost" className="p-0 h-auto justify-start gap-2 mt-auto font-bold text-primary hover:text-primary/80 group-hover:translate-x-2 transition-all">
                                            Explore Resource <ArrowRight className="h-4 w-4" />
                                        </Button>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Learning Paths */}
            <section className="py-24 bg-gradient-to-br from-muted/20 to-background">
                <div className="container mx-auto px-4">
                    <div className="text-center max-w-3xl mx-auto mb-20">
                        <motion.h2
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6 }}
                            className="text-4xl md:text-6xl font-bold font-display mb-6"
                        >
                            <span className="text-gradient">Learning Paths</span> for Success
                        </motion.h2>
                        <motion.p
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.1 }}
                            className="text-xl text-muted-foreground leading-relaxed"
                        >
                            Structured learning journeys designed to take you from beginner to expert
                        </motion.p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                        {learningPaths.map((path, i) => (
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
                                        <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${path.gradient} mb-4 shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                                            <path.icon className="h-8 w-8 text-white" />
                                        </div>

                                        <div className="flex items-center gap-2 mb-2">
                                            <span className="text-xs font-bold px-2 py-1 rounded-full bg-primary/10 text-primary">
                                                {path.level}
                                            </span>
                                            <span className="text-xs text-muted-foreground flex items-center gap-1">
                                                <Clock className="h-3 w-3" />
                                                {path.duration}
                                            </span>
                                        </div>

                                        <h3 className="text-2xl font-bold font-display text-gradient">{path.title}</h3>
                                        <p className="text-muted-foreground leading-relaxed">{path.description}</p>

                                        <Button className="mt-auto gradient-primary border-0 text-white font-semibold font-display hover:shadow-lg hover:scale-105 transition-all duration-300">
                                            Start Learning
                                        </Button>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Support CTA Section */}
            <section className="py-32 relative overflow-hidden">
                <div className="absolute inset-0 gradient-primary opacity-95" />
                <div className="absolute inset-0 gradient-secondary opacity-30 animate-pulse" />

                <div className="container mx-auto px-4 relative z-10">
                    <div className="max-w-6xl mx-auto">
                        <div className="flex flex-col lg:flex-row gap-12 items-center justify-between">
                            <motion.div
                                initial={{ opacity: 0, x: -20 }}
                                whileInView={{ opacity: 1, x: 0 }}
                                transition={{ duration: 0.6 }}
                                className="max-w-xl text-center lg:text-left"
                            >
                                <h2 className="text-3xl md:text-5xl font-bold font-display mb-6 text-white">
                                    Need Expert Help?
                                </h2>
                                <p className="text-white/80 text-lg font-medium leading-relaxed mb-8">
                                    Our support team is available 24/7 to help you with anything from
                                    custom domains to API integrations. Get personalized assistance when you need it.
                                </p>

                                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
                                    <div className="flex items-center gap-3 text-white/80">
                                        <Headphones className="h-5 w-5 text-green-400" />
                                        <span className="text-sm">24/7 Live Support</span>
                                    </div>
                                    <div className="flex items-center gap-3 text-white/80">
                                        <Award className="h-5 w-5 text-yellow-400" />
                                        <span className="text-sm">Expert Team</span>
                                    </div>
                                    <div className="flex items-center gap-3 text-white/80">
                                        <MessageSquare className="h-5 w-5 text-blue-400" />
                                        <span className="text-sm">Quick Response</span>
                                    </div>
                                    <div className="flex items-center gap-3 text-white/80">
                                        <FileText className="h-5 w-5 text-purple-400" />
                                        <span className="text-sm">Detailed Solutions</span>
                                    </div>
                                </div>
                            </motion.div>

                            <motion.div
                                initial={{ opacity: 0, x: 20 }}
                                whileInView={{ opacity: 1, x: 0 }}
                                transition={{ duration: 0.6 }}
                                className="flex flex-col sm:flex-row gap-4 shrink-0"
                            >
                                <Button size="lg" className="h-16 px-10 font-semibold font-display bg-white text-primary hover:bg-white/95 hover:scale-105 transition-all duration-300 rounded-2xl shadow-2xl group">
                                    <Headphones className="w-5 h-5 mr-3 group-hover:rotate-12 transition-transform" />
                                    Contact Support
                                </Button>
                                <Button size="lg" variant="outline" className="h-16 px-10 font-semibold font-display bg-transparent border-white/40 text-white hover:bg-white/10 hover:border-white/60 hover:scale-105 transition-all duration-300 rounded-2xl group">
                                    <BookOpen className="w-5 h-5 mr-3 group-hover:scale-110 transition-transform" />
                                    Documentation
                                </Button>
                            </motion.div>
                        </div>
                    </div>
                </div>
            </section>
        </MainLayout>
    );
}
