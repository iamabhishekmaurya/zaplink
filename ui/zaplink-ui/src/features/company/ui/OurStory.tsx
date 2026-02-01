'use client';

import { motion } from 'motion/react';
import Image from 'next/image';

export const OurStory = () => {
    return (
        <section className="py-20">
            <div className="container mx-auto px-4 md:px-6">
                <div className="grid md:grid-cols-2 gap-12 items-center">
                    <motion.div
                        initial={{ opacity: 0, x: -30 }}
                        whileInView={{ opacity: 1, x: 0 }}
                        viewport={{ once: true }}
                        transition={{ duration: 0.6 }}
                    >
                        <h2 className="text-3xl font-bold mb-6">Our Story</h2>
                        <div className="space-y-4 text-muted-foreground text-lg leading-relaxed">
                            <p>
                                It started as a side project in 2024. We were frustrated with the clunky,
                                analytic-poor URL shorteners available. We wanted something that felt fast,
                                looked good, and actually gave us data we could use.
                            </p>
                            <p>
                                So we built zaipmeWhat began as a tool for our own marketing campaigns
                                quickly grew into a platform used by thousands of teams worldwide.
                            </p>
                            <p>
                                Today, we are a diverse team of engineers, designers, and marketers,
                                united by our passion for building software that feels like magic.
                            </p>
                        </div>
                    </motion.div>

                    <motion.div
                        initial={{ opacity: 0, scale: 0.9 }}
                        whileInView={{ opacity: 1, scale: 1 }}
                        viewport={{ once: true }}
                        transition={{ duration: 0.6 }}
                        className="relative h-[400px] rounded-3xl overflow-hidden bg-muted"
                    >
                        {/* Placeholder for Team Image */}
                        <div className="absolute inset-0 bg-gradient-to-br from-primary/20 to-secondary/20 flex items-center justify-center">
                            <span className="text-muted-foreground font-medium">Team Photo Placeholder</span>
                        </div>
                    </motion.div>
                </div>
            </div>
        </section>
    );
};
