'use client';

import { motion } from "motion/react";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import {
    SiSlack,
    SiX,
    SiNotion,
    SiHubspot,
    SiSalesforce,
    SiMailchimp,
    SiZapier,
    SiDiscord
} from "react-icons/si";

const integrations = [
    { name: "Slack", color: "bg-[#E01E5A]", x: 0, y: -120, delay: 0, icon: <SiSlack className="w-6 h-6 md:w-8 md:h-8" /> },
    { name: "X", color: "bg-[#000000]", x: 120, y: 0, delay: 1, icon: <SiX className="w-6 h-6 md:w-8 md:h-8" /> },
    { name: "Notion", color: "bg-[#000000]", x: 0, y: 120, delay: 2, icon: <SiNotion className="w-6 h-6 md:w-8 md:h-8" /> },
    { name: "HubSpot", color: "bg-[#FF7A59]", x: -120, y: 0, delay: 3, icon: <SiHubspot className="w-6 h-6 md:w-8 md:h-8" /> },
    { name: "Salesforce", color: "bg-[#00A1E0]", x: 85, y: -85, delay: 0.5, icon: <SiSalesforce className="w-6 h-6 md:w-8 md:h-8" /> },
    { name: "Mailchimp", color: "bg-[#FFE01B]", x: 85, y: 85, delay: 1.5, icon: <SiMailchimp className="w-6 h-6 md:w-8 md:h-8 text-black" /> },
    { name: "Zapier", color: "bg-[#FF4F00]", x: -85, y: 85, delay: 2.5, icon: <SiZapier className="w-6 h-6 md:w-8 md:h-8" /> },
    { name: "Discord", color: "bg-[#5865F2]", x: -85, y: -85, delay: 3.5, icon: <SiDiscord className="w-6 h-6 md:w-8 md:h-8" /> },
];

export const IntegrationUniverse = () => {
    return (
        <section className="py-32 relative overflow-hidden bg-background">
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,_var(--tw-gradient-stops))] from-primary/5 via-transparent to-transparent" />

            <div className="max-w-7xl mx-auto px-4 md:px-6 relative z-10 text-center">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    className="max-w-3xl mx-auto mb-16"
                >
                    <h2 className="text-3xl md:text-5xl font-bold mb-6">Connects with your <br />favorite <span className="text-primary font-[family-name:var(--font-script)]">tools</span></h2>
                    <p className="text-muted-foreground text-lg mb-8">
                        Streamline your workflow by integrating zaipme with the apps you use every day.
                        API-first design ensures seamless connectivity.
                    </p>
                    <Button variant="outline" className="rounded-full">Explore Integration Library</Button>
                </motion.div>

                {/* Orbit Animation Container */}
                <div className="relative w-[300px] h-[300px] md:w-[500px] md:h-[500px] mx-auto flex items-center justify-center">
                    {/* Central Hub */}
                    <Image src="/logo-light.png" alt="Logo" width={70} height={70} className="block dark:hidden" />
                    <Image src="/logo-dark.png" alt="Logo" width={70} height={70} className="hidden dark:block" />

                    {/* Orbit Rings */}
                    <div className="absolute w-[70%] h-[70%] border border-dashed border-border/50 rounded-full animate-[spin_60s_linear_infinite]" />
                    <div className="absolute w-[100%] h-[100%] border border-dashed border-border/30 rounded-full animate-[spin_80s_linear_infinite_reverse]" />

                    {/* Orbiting Icons */}
                    {integrations.map((app, i) => (
                        <motion.div
                            key={i}
                            className="absolute z-10"
                            initial={{ x: 0, y: 0, opacity: 0 }}
                            whileInView={{
                                x: app.x,
                                y: app.y,
                                opacity: 1,
                                scale: 1
                            }}
                            viewport={{ once: true }}
                            transition={{
                                duration: 0.8,
                                delay: i * 0.1,
                                type: "spring",
                                stiffness: 50
                            }}
                        >
                            <motion.div
                                className={`w-12 h-12 md:w-16 md:h-16 ${app.color} rounded-xl flex items-center justify-center shadow-lg text-white font-bold text-xs md:text-sm`}
                                whileHover={{ scale: 1.1, y: -5 }}
                            >
                                {app.icon}
                            </motion.div>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
};
