'use client';

import { Link as LinkIcon, Shield, TrendingUp, Zap } from "lucide-react";
import { motion } from "motion/react";

const stats = [
    { value: "10M+", label: "Links shortened", icon: LinkIcon },
    { value: "500K+", label: "Happy users", icon: Zap },
    { value: "99.9%", label: "Uptime guaranteed", icon: Shield },
    { value: "150+", label: "Countries served", icon: TrendingUp },
];

export const StatsSection = () => {
    return (
        <section className="py-32 bg-gradient-to-r from-primary via-purple-500 to-indigo-500 relative overflow-hidden">
            {/* Decorative orbs */}
            <div className="absolute top-0 left-1/4 w-72 h-72 bg-white/10 rounded-full blur-3xl" />
            <div className="absolute bottom-0 right-1/4 w-96 h-96 bg-accent/20 rounded-full blur-3xl" />

            <div className="max-w-7xl mx-auto px-4 md:px-6 relative z-10">
                {/* Section Header */}
                <motion.div
                    className="text-center mb-16"
                    initial={{ opacity: 0, y: 30 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    transition={{ duration: 0.6 }}
                >
                    <span className="text-sm font-medium text-primary-foreground/80 mb-3 block uppercase tracking-wider">Our Impact</span>
                    <h2 className="text-3xl md:text-4xl font-bold text-white">
                        Trusted by teams <span className="font-[family-name:var(--font-script)]">worldwide</span>
                    </h2>
                </motion.div>

                <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
                    {stats.map((stat, index) => (
                        <motion.div
                            key={index}
                            className="text-center"
                            initial={{ opacity: 0, y: 20 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            viewport={{ once: true }}
                            transition={{ delay: index * 0.1 }}
                        >
                            <div className="w-14 h-14 mx-auto mb-4 rounded-2xl bg-white/20 backdrop-blur flex items-center justify-center">
                                <stat.icon className="w-7 h-7 text-white" />
                            </div>
                            <p className="text-4xl md:text-5xl font-bold text-white mb-2">{stat.value}</p>
                            <p className="text-primary-foreground/80 text-sm">{stat.label}</p>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
};
