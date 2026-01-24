'use client';

import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { ArrowRight, Code2, Users, HelpCircle, GraduationCap } from 'lucide-react';
import { cn } from '@/lib/utils';
import Link from 'next/link';

const resources = [
    {
        icon: GraduationCap,
        title: 'Academy',
        description: 'Master link management with our structured courses and certifications.',
        color: 'text-blue-500',
        bg: 'bg-blue-500/10',
        href: '#'
    },
    {
        icon: HelpCircle,
        title: 'Help Center',
        description: 'Find answers to common questions and learn how to use Zaplink features.',
        color: 'text-emerald-500',
        bg: 'bg-emerald-500/10',
        href: '#'
    },
    {
        icon: Code2,
        title: 'Developers',
        description: 'Explore our API documentation, SDKs, and developer tools.',
        color: 'text-violet-500',
        bg: 'bg-violet-500/10',
        href: '#'
    },
    {
        icon: Users,
        title: 'Community',
        description: 'Connect with other Zaplink users, share tips, and get inspired.',
        color: 'text-[#ff8904]',
        bg: 'bg-[#ff8904]/10',
        href: '#'
    },
];

export default function ResourceGrid() {
    return (
        <section className="py-20 px-4">
            <div className="max-w-7xl mx-auto">
                <div className="text-center mb-16">
                    <h2 className="text-3xl font-bold mb-4">Browse Resources</h2>
                    <p className="text-muted-foreground">Everything you need to succeed with Zaplink.</p>
                </div>

                <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                    {resources.map((item, index) => (
                        <motion.div
                            key={item.title}
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            viewport={{ once: true }}
                            transition={{ delay: index * 0.1 }}
                        >
                            <Link href={item.href} className="block h-full">
                                <div className="h-full p-8 rounded-3xl bg-card border border-border/50 hover:border-primary/30 transition-all duration-300 hover:shadow-lg hover:-translate-y-1 group">
                                    <div className={cn("w-12 h-12 rounded-2xl flex items-center justify-center mb-6 transition-colors", item.bg, "group-hover:bg-primary/10")}>
                                        <item.icon className={cn("w-6 h-6 transition-colors", item.color, "group-hover:text-primary")} />
                                    </div>
                                    <h3 className="text-xl font-bold mb-3">{item.title}</h3>
                                    <p className="text-muted-foreground text-sm mb-6 leading-relaxed">
                                        {item.description}
                                    </p>
                                    <div className="flex items-center text-sm font-medium text-primary opacity-0 -translate-x-2 group-hover:opacity-100 group-hover:translate-x-0 transition-all duration-300">
                                        Explore <ArrowRight className="w-4 h-4 ml-1" />
                                    </div>
                                </div>
                            </Link>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
}
