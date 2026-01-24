'use client';

import { motion } from "motion/react";
import { Book, MessageCircle, Mail, Users } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import Link from "next/link";

const options = [
    {
        title: "Documentation",
        description: "Browse detailed guides and API references.",
        icon: Book,
        href: "/resources", // Linking to resources for now
        color: "text-blue-500",
        bg: "bg-blue-500/10"
    },
    {
        title: "Community Forum",
        description: "Join the conversation with other developers.",
        icon: Users,
        href: "#",
        color: "text-orange-500",
        bg: "bg-orange-500/10"
    },
    {
        title: "Live Chat",
        description: "Chat with our support team in real-time.",
        icon: MessageCircle,
        href: "#",
        color: "text-green-500",
        bg: "bg-green-500/10"
    },
    {
        title: "Email Support",
        description: "Get help via email for complex inquiries.",
        icon: Mail,
        href: "mailto:support@zap.link",
        color: "text-purple-500",
        bg: "bg-purple-500/10"
    }
];

export const SupportOptions = () => {
    return (
        <section className="py-12">
            <div className="container mx-auto px-4 md:px-6">
                <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                    {options.map((option, index) => (
                        <Link key={index} href={option.href} className="block group">
                            <motion.div
                                initial={{ opacity: 0, y: 20 }}
                                whileInView={{ opacity: 1, y: 0 }}
                                viewport={{ once: true }}
                                transition={{ duration: 0.4, delay: index * 0.1 }}
                            >
                                <Card className="h-full bg-card/50 backdrop-blur-sm border-border/50 hover:border-primary/50 transition-all hover:shadow-lg">
                                    <CardHeader>
                                        <div className={`w-12 h-12 rounded-xl ${option.bg} flex items-center justify-center mb-4 group-hover:scale-110 transition-transform`}>
                                            <option.icon className={`w-6 h-6 ${option.color}`} />
                                        </div>
                                        <CardTitle className="text-xl">{option.title}</CardTitle>
                                    </CardHeader>
                                    <CardContent>
                                        <CardDescription>{option.description}</CardDescription>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        </Link>
                    ))}
                </div>
            </div>
        </section>
    );
};
