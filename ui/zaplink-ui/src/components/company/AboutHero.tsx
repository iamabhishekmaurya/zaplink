'use client';

import { motion } from "motion/react";
import { Badge } from "@/components/ui/badge";

export const AboutHero = () => {
    return (
        <section className="relative pt-32 pb-20 overflow-hidden">
            {/* Background Decoration */}
            <div className="absolute top-0 inset-x-0 h-[500px] bg-gradient-to-b from-primary/5 to-transparent -z-10" />

            <div className="container mx-auto px-4 md:px-6 text-center max-w-4xl">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                >
                    <Badge variant="outline" className="mb-6 px-4 py-1 text-sm border-primary/20 text-primary bg-primary/5">
                        Our Mission
                    </Badge>
                    <h1 className="text-4xl md:text-6xl font-bold mb-6 tracking-tight">
                        We're building the <span className="text-primary">future</span> of <br />
                        digital connections.
                    </h1>
                    <p className="text-xl text-muted-foreground leading-relaxed max-w-2xl mx-auto">
                        Zaplink was founded with a simple belief: every link is an opportunity.
                        We help brands and creators turn those opportunities into lasting relationships.
                    </p>
                </motion.div>
            </div>
        </section>
    );
};
