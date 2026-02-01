'use client';

import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card';
import { motion } from 'motion/react';

const testimonials = [
    {
        quote: "zaipmeis by far my favorite link management tool among all the options available. It's incredibly powerful yet remains surprisingly user-friendly.",
        author: "Jake George",
        role: "Founder, Loom",
        company: "Loom",
        icon: "🎬"
    },
    {
        quote: "I recently started using zaipme to help marketing teams automate their workflows, and the results have been impressive.",
        author: "Steve Alvarez",
        role: "VP Marketing, Evernote",
        company: "Evernote",
        icon: "🐘"
    },
    {
        quote: "I absolutely love building with zaipme—it has enabled me to create incredibly powerful link tracking solutions.",
        author: "Sara Rahmanian",
        role: "Head of Engineering, Lattice",
        company: "Lattice",
        icon: "💎"
    }
];

export const TestimonialsSection = () => {
    return (
        <section className="py-32 bg-background relative overflow-hidden">
            {/* Decorative elements */}
            <div className="absolute top-20 left-10 w-20 h-20 border border-border rounded-full opacity-50" />
            <div className="absolute bottom-20 right-10 w-32 h-32 border border-border rounded-full opacity-50" />

            <div className="container mx-auto px-4 md:px-6 relative z-10">
                <motion.div
                    className="text-center mb-12"
                    initial={{ opacity: 0, y: 30 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    transition={{ duration: 0.6 }}
                >
                    <span className="text-sm font-medium text-primary mb-2 block">Testimonials</span>
                    <h2 className="text-3xl md:text-4xl font-bold text-foreground">
                        What users are <span className="font-[family-name:var(--font-script)] text-primary">saying</span>
                    </h2>
                    <p className="text-muted-foreground mt-2">Trusted by high-performing teams worldwide.</p>
                </motion.div>

                <div className="grid md:grid-cols-3 gap-6 max-w-5xl mx-auto">
                    {testimonials.map((t, i) => (
                        <Card key={i} className="bg-card border-border">
                            <CardHeader className="pb-2">
                                <div className="flex items-center gap-2">
                                    <span className="text-lg">{t.icon}</span>
                                    <span className="font-medium text-muted-foreground text-sm">{t.company}</span>
                                </div>
                            </CardHeader>
                            <CardContent>
                                <p className="text-muted-foreground text-sm leading-relaxed mb-4">"{t.quote}"</p>
                            </CardContent>
                            <CardFooter className="pt-0">
                                <div className="flex items-center gap-3">
                                    <div className="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-primary-foreground text-sm font-medium">
                                        {t.author.charAt(0)}
                                    </div>
                                    <div>
                                        <p className="font-medium text-foreground text-sm">{t.author}</p>
                                        <p className="text-muted-foreground text-xs">{t.role}</p>
                                    </div>
                                </div>
                            </CardFooter>
                        </Card>
                    ))}
                </div>
            </div>
        </section>
    );
};
