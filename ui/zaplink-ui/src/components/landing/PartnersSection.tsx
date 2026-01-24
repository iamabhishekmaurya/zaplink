'use client';

import { motion } from "motion/react";

const partners = [
    { name: "Hotjar", icon: "🔥" },
    { name: "Loom", icon: "🎬" },
    { name: "Lattice", icon: "💎" },
    { name: "Evernote", icon: "🐘" },
    { name: "Notion", icon: "📝" },
];

export const PartnersSection = () => {
    return (
        <section className="py-24 bg-background">
            <div className="max-w-7xl mx-auto px-4 md:px-6">
                {/* Section Header */}
                <motion.div
                    className="text-center mb-16"
                    initial={{ opacity: 0, y: 30 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    transition={{ duration: 0.6 }}
                >
                    <span className="text-sm font-medium text-muted-foreground mb-3 block uppercase tracking-wider">Our Partners</span>
                    <h2 className="text-2xl md:text-3xl font-bold text-foreground">
                        Trusted by 10,000+ teams worldwide
                    </h2>
                </motion.div>

                <div className="flex flex-wrap justify-center items-center gap-10 md:gap-20">
                    {partners.map((partner, index) => (
                        <motion.div
                            key={index}
                            className="flex items-center gap-3 text-muted-foreground hover:text-foreground transition-colors cursor-pointer"
                            initial={{ opacity: 0, y: 10 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            viewport={{ once: true }}
                            transition={{ delay: index * 0.1 }}
                        >
                            <span className="text-2xl">{partner.icon}</span>
                            <span className="text-base font-medium">{partner.name}</span>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
};
