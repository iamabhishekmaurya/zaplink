'use client';

import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Send } from 'lucide-react';

export default function BlogNewsletter() {
    return (
        <section className="py-20 bg-muted/30 border-t border-border/50">
            <div className="max-w-3xl mx-auto px-4 text-center">
                <motion.div
                    initial={{ opacity: 0, scale: 0.95 }}
                    whileInView={{ opacity: 1, scale: 1 }}
                    viewport={{ once: true }}
                >
                    <div className="w-16 h-16 bg-primary/10 rounded-2xl flex items-center justify-center mx-auto mb-6">
                        <Send className="w-8 h-8 text-primary" />
                    </div>
                    <h2 className="text-3xl font-bold mb-4">Never miss an update</h2>
                    <p className="text-muted-foreground mb-8 text-lg">
                        Join 10,000+ marketers and developers who get our weekly insights delivered to their inbox.
                    </p>

                    <form className="flex flex-col sm:flex-row gap-3 max-w-md mx-auto" onSubmit={(e) => e.preventDefault()}>
                        <Input
                            type="email"
                            placeholder="email@example.com"
                            className="h-12 bg-background shadow-sm"
                        />
                        <Button type="submit" size="lg" className="h-12 px-8 bg-primary hover:bg-primary/90">
                            Subscribe
                        </Button>
                    </form>
                    <p className="text-xs text-muted-foreground mt-4">
                        Unsubscribe at any time. We respect your privacy.
                    </p>
                </motion.div>
            </div>
        </section>
    );
}
