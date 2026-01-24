'use client';

import { motion } from "motion/react";
import { Search } from "lucide-react";
import { Input } from "@/components/ui/input";

export const SupportHero = () => {
    return (
        <section className="relative pt-32 pb-20 overflow-hidden">
            {/* Background Gradients */}
            <div className="absolute inset-0 -z-10">
                <div className="absolute top-0 right-1/4 w-[600px] h-[600px] bg-primary/10 rounded-full blur-3xl opacity-40" />
                <div className="absolute bottom-0 left-1/4 w-[500px] h-[500px] bg-orange-500/10 rounded-full blur-3xl opacity-30" />
            </div>

            <div className="container mx-auto px-4 md:px-6 relative z-10 text-center">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="max-w-3xl mx-auto"
                >
                    <h1 className="text-4xl md:text-5xl font-bold mb-6">
                        How can we <span className="text-primary">help you?</span>
                    </h1>
                    <p className="text-lg text-muted-foreground mb-10">
                        Search our knowledge base or get in touch with our support team.
                    </p>

                    <div className="relative max-w-xl mx-auto">
                        <div className="absolute inset-y-0 left-4 flex items-center pointer-events-none">
                            <Search className="h-5 w-5 text-muted-foreground" />
                        </div>
                        <Input
                            type="search"
                            placeholder="Search for answers (e.g., 'API keys', 'Billing')"
                            className="w-full h-14 pl-12 pr-4 text-lg rounded-2xl bg-background/50 backdrop-blur-sm border-border/50 shadow-lg focus:ring-2 focus:ring-primary/20"
                        />
                    </div>
                </motion.div>
            </div>
        </section>
    );
};
