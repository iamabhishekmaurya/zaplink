'use client';

import { motion } from 'motion/react';
import { Link2, BarChart2, QrCode, Shield, Users, Globe, Smartphone, Zap } from 'lucide-react';
import Image from 'next/image';

// Placeholder using divs for images if no actual assets are available, 
// but using the structure for Image component for future proofing.

const features = [
    {
        id: "management",
        category: "Link Management",
        title: "Intelligent Link Shortening",
        description: "Create branded, memorable links that drive higher click-through rates. Customize the back-half of your links to match your marketing campaigns.",
        items: [
            "Custom domains (branding.com/offer)",
            "Bulk link creation & updates",
            "Link retargeting & UTM builder",
            "Broken link monitoring"
        ],
        icon: Link2,
        imageAlign: "right",
        gradient: "from-blue-500/20 to-cyan-500/20",
        color: "text-blue-500"
    },
    {
        id: "analytics",
        category: "Analytics & Data",
        title: "Deep Dive Analytics",
        description: "Understand your audience with precision. Track every click, scan, and tap in real-time to optimize your marketing performance.",
        items: [
            "Geographic location tracking",
            "Device & browser breakdown",
            "Referrer analytics",
            "Click-stream visualizer"
        ],
        icon: BarChart2,
        imageAlign: "left",
        gradient: "from-purple-500/20 to-pink-500/20",
        color: "text-purple-500"
    },
    {
        id: "qr",
        category: "QR Codes",
        title: "Dynamic QR Codes",
        description: "Bridge the offline and online worlds. Generate completely customizable QR codes that you can update even after printing.",
        items: [
            "Fully design-customizable QRs",
            "Logo embedding & color gradients",
            "Dynamic destination changing",
            "Scan analytics tracking"
        ],
        icon: QrCode,
        imageAlign: "right",
        gradient: "from-orange-500/20 to-red-500/20",
        color: "text-orange-500"
    },
    {
        id: "collaboration",
        category: "Team & Security",
        title: "Enterprise-Grade Control",
        description: "Scale your link management securely. Manage team permissions, audit logs, and ensure brand consistency across your organization.",
        items: [
            "Role-based access control (RBAC)",
            "SSO & 2FA authentication",
            "Detailed audit logs",
            "Domain whitelisting"
        ],
        icon: Shield,
        imageAlign: "left",
        gradient: "from-emerald-500/20 to-green-500/20",
        color: "text-emerald-500"
    }
];

interface DetailedFeatureSectionProps {
    activeTab: string;
}

export const DetailedFeatureSection = ({ activeTab }: DetailedFeatureSectionProps) => {
    const filteredFeatures = activeTab === "all"
        ? features
        : features.filter(f => f.id === activeTab);

    return (
        <section className="py-20">
            <div className="max-w-7xl mx-auto px-4 md:px-6">
                <div className="space-y-32">
                    {filteredFeatures.map((feature, index) => (
                        <motion.div
                            key={feature.title}
                            initial={{ opacity: 0, y: 40 }}
                            whileInView={{ opacity: 1, y: 0 }}
                            viewport={{ once: true, margin: "-100px" }}
                            transition={{ duration: 0.6 }}
                            className={`flex flex-col lg:flex-row items-center gap-12 lg:gap-20 ${feature.imageAlign === "left" ? "lg:flex-row-reverse" : ""
                                }`}
                        >
                            {/* Text Content */}
                            <div className="flex-1 space-y-8">
                                <div className="space-y-4">
                                    <div className={`inline-flex items-center gap-2 px-3 py-1 rounded-full bg-secondary/50 border border-border w-fit`}>
                                        <feature.icon className={`w-4 h-4 ${feature.color}`} />
                                        <span className={`text-xs font-semibold uppercase tracking-wider ${feature.color}`}>
                                            {feature.category}
                                        </span>
                                    </div>
                                    <h2 className="text-3xl md:text-4xl font-bold leading-tight">
                                        {feature.title}
                                    </h2>
                                    <p className="text-lg text-muted-foreground leading-relaxed">
                                        {feature.description}
                                    </p>
                                </div>

                                <ul className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    {feature.items.map((item, i) => (
                                        <li key={i} className="flex items-start gap-3">
                                            <div className={`mt-1 min-w-5 min-h-5 rounded-full flex items-center justify-center bg-muted`}>
                                                <div className={`w-2 h-2 rounded-full ${feature.color.replace('text-', 'bg-')}`} />
                                            </div>
                                            <span className="text-sm font-medium">{item}</span>
                                        </li>
                                    ))}
                                </ul>
                            </div>

                            {/* Image Visual / Placeholder */}
                            <div className="flex-1 w-full">
                                <div className={`relative aspect-square md:aspect-[4/3] rounded-3xl overflow-hidden border border-border/50 bg-card/50 shadow-2xl group`}>
                                    <div className={`absolute inset-0 bg-gradient-to-br ${feature.gradient} opacity-20`} />

                                    {/* Abstract UI Mockups created with CSS/divs for now to avoid needing external images */}
                                    <div className="absolute inset-0 p-8 flex items-center justify-center">
                                        {feature.id === 'management' && (
                                            <div className="w-full h-full relative">
                                                <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-3/4 h-20 bg-background rounded-xl shadow-lg border border-border flex items-center px-4 gap-4">
                                                    <Globe className="w-5 h-5 text-blue-500" />
                                                    <div className="flex-1 h-2 bg-muted rounded-full overflow-hidden">
                                                        <div className="w-2/3 h-full bg-blue-500" />
                                                    </div>
                                                    <span className="text-xs font-mono">zap.link/summer</span>
                                                </div>
                                                <div className="absolute top-[60%] left-[60%] -translate-x-1/2 w-1/2 h-24 bg-background rounded-xl shadow-xl border border-border flex flex-col p-4 gap-2 animate-bounce hover:pause">
                                                    <div className="flex gap-2 items-center text-xs text-muted-foreground">
                                                        <Users className="w-3 h-3" />
                                                        Clicks
                                                    </div>
                                                    <div className="text-2xl font-bold">12,453</div>
                                                </div>
                                            </div>
                                        )}
                                        {feature.id === 'analytics' && (
                                            <div className="w-full h-full flex items-end justify-center px-8 pb-8 gap-3">
                                                {[40, 70, 50, 90, 60, 80].map((h, idx) => (
                                                    <motion.div
                                                        key={idx}
                                                        initial={{ height: 0 }}
                                                        whileInView={{ height: `${h}%` }}
                                                        transition={{ duration: 1, delay: idx * 0.1 }}
                                                        className="w-8 bg-purple-500/80 rounded-t-lg"
                                                    />
                                                ))}
                                            </div>
                                        )}
                                        {feature.id === 'qr' && (
                                            <div className="relative">
                                                <div className="w-48 h-48 bg-white p-2 rounded-xl border-4 border-orange-500/20">
                                                    <div className="w-full h-full bg-slate-900 flex items-center justify-center">
                                                        <QrCode className="w-20 h-20 text-white" />
                                                    </div>
                                                </div>
                                                <div className="absolute -bottom-6 -right-6 bg-background border border-border px-4 py-2 rounded-lg shadow-lg">
                                                    <div className="flex items-center gap-2">
                                                        <Smartphone className="w-4 h-4 text-orange-500" />
                                                        <span className="text-sm font-bold">Scan to view</span>
                                                    </div>
                                                </div>
                                            </div>
                                        )}
                                        {feature.id === 'collaboration' && (
                                            <div className="grid grid-cols-2 gap-4 w-full">
                                                {[1, 2, 3, 4].map((i) => (
                                                    <div key={i} className="bg-background border border-border p-4 rounded-xl shadow-sm flex items-center gap-3">
                                                        <div className="w-8 h-8 rounded-full bg-emerald-500/20 flex items-center justify-center">
                                                            <div className="w-full h-full rounded-full bg-emerald-500/40 animate-pulse" />
                                                        </div>
                                                        <div className="space-y-2 flex-1">
                                                            <div className="h-2 w-16 bg-muted rounded-full" />
                                                            <div className="h-2 w-10 bg-muted rounded-full" />
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
};
