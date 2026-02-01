'use client';

import { motion } from 'motion/react';
import { Button } from '@/components/ui/button';

export const CareersHero = () => {
    return (
        <section className="relative pt-32 pb-20 overflow-hidden">
            <div className="container mx-auto px-4 md:px-6 text-center max-w-3xl">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                >
                    <h1 className="text-4xl md:text-6xl font-bold mb-6">
                        Join us in building the <span className="text-primary italic">next big thing.</span>
                    </h1>
                    <p className="text-xl text-muted-foreground mb-10 leading-relaxed">
                        We're a remote-first team solving interesting scale and UX problems.
                        Come do the best work of your life.
                    </p>
                    <Button size="lg" className="h-12 px-8 text-base">
                        View Open Positions
                    </Button>
                </motion.div>
            </div>
        </section>
    );
};
