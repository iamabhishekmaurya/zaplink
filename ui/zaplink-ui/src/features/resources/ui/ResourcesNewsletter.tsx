'use client';

import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Mail } from 'lucide-react';

export default function ResourcesNewsletter() {
    return (
        <section className="py-20 px-4">
            <div className="max-w-4xl mx-auto">
                <motion.div
                    initial={{ opacity: 0, scale: 0.95 }}
                    whileInView={{ opacity: 1, scale: 1 }}
                    viewport={{ once: true }}
                    className="relative rounded-3xl overflow-hidden bg-primary/5 border border-primary/10 p-8 md:p-16 text-center"
                >
                    {/* Decorative Background */}
                    <div className="absolute top-0 right-0 w-64 h-64 bg-primary/10 rounded-full blur-[80px] -translate-y-1/2 translate-x-1/2" />
                    <div className="absolute bottom-0 left-0 w-64 h-64 bg-[#ff8904]/10 rounded-full blur-[80px] translate-y-1/2 -translate-x-1/2" />

                    <div className="relative z-10">
                        <div className="w-16 h-16 bg-background rounded-2xl flex items-center justify-center mx-auto mb-6 shadow-sm">
                            <Mail className="w-8 h-8 text-primary" />
                        </div>
                        <h2 className="text-3xl font-bold mb-4">Stay in the loop</h2>
                        <p className="text-muted-foreground max-w-lg mx-auto mb-8">
                            Get the latest updates, articles, and resources delivered straight to your inbox. No spam, ever.
                        </p>

                        <form className="flex flex-col sm:flex-row gap-3 max-w-md mx-auto" onSubmit={(e) => e.preventDefault()}>
                            <Input
                                type="email"
                                placeholder="Enter your email"
                                className="h-12 bg-background/50 border-border/50 focus:border-primary/50"
                            />
                            <Button type="submit" size="lg" className="h-12 px-8 bg-gradient-to-r from-primary to-violet-600">
                                Subscribe
                            </Button>
                        </form>
                    </div>
                </motion.div>
            </div>
        </section>
    );
}
