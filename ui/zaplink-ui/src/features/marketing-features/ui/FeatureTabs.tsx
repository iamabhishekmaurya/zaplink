'use client';

import { motion } from 'motion/react';

interface FeatureTabsProps {
    activeTab: string;
    setActiveTab: (tab: string) => void;
}

const tabs = [
    { id: "all", label: "All Features" },
    { id: "management", label: "Link Management" },
    { id: "analytics", label: "Analytics & Data" },
    { id: "qr", label: "QR Codes" },
    { id: "collaboration", label: "Team & Security" },
];

export const FeatureTabs = ({ activeTab, setActiveTab }: FeatureTabsProps) => {
    return (
        <div className="w-full border-b border-border/50">
            <div className="max-w-7xl mx-auto px-4 md:px-6 overflow-x-auto scrollbar-hide">
                <div className="flex items-center gap-8 min-w-max">
                    {tabs.map((tab) => (
                        <button
                            key={tab.id}
                            onClick={() => setActiveTab(tab.id)}
                            className="relative py-4 px-2 group"
                        >
                            <span className={`text-sm font-medium transition-colors ${activeTab === tab.id
                                ? "text-primary"
                                : "text-muted-foreground group-hover:text-foreground"
                                }`}>
                                {tab.label}
                            </span>
                            {activeTab === tab.id && (
                                <motion.div
                                    layoutId="activeFeatureTab"
                                    className="absolute bottom-0 left-0 right-0 h-0.5 bg-primary"
                                    transition={{ type: "spring", stiffness: 300, damping: 30 }}
                                />
                            )}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
};
