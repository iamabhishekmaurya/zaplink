'use client';

import React from 'react';
import { motion } from 'framer-motion';
import { Check, Minus, Info } from 'lucide-react';
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from '@/components/ui/tooltip';

const features = [
    {
        category: 'Core Features', items: [
            { name: 'Short Links', free: '50/mo', pro: 'Unlimited', business: 'Unlimited' },
            { name: 'Custom Domains', free: '1', pro: '5', business: 'Unlimited' },
            { name: 'QR Codes', free: 'Basic', pro: 'Customizable', business: 'Advanced + API' },
            { name: 'Link Tags', free: false, pro: true, business: true },
        ]
    },
    {
        category: 'Analytics', items: [
            { name: 'Click Tracking', free: '30 days', pro: '1 year', business: 'Unlimited' },
            { name: 'Geographic Data', free: true, pro: true, business: true },
            { name: 'Device/OS Data', free: true, pro: true, business: true },
            { name: 'Export Reports', free: false, pro: 'CSV', business: 'CSV, PDF, API' },
        ]
    },
    {
        category: 'Management', items: [
            { name: 'Team Members', free: '1', pro: '5', business: 'Unlimited' },
            { name: 'SSO (SAML)', free: false, pro: false, business: true },
            { name: 'API Access', free: false, pro: true, business: true },
            { name: 'Bulk Creation', free: false, pro: true, business: true },
        ]
    },
];

export default function ComparisonTable() {
    return (
        <section className="py-20 bg-muted/30">
            <div className="max-w-7xl mx-auto px-4 md:px-8">
                <div className="text-center mb-16">
                    <h2 className="text-3xl font-bold mb-4">Detailed Comparison of <span className="text-primary font-[family-name:var(--font-script)]">Plans</span></h2>
                    <p className="text-muted-foreground">Compare features across all plans to find your perfect fit.</p>
                </div>

                <div className="overflow-x-auto">
                    <table className="w-full min-w-[800px] border-collapse">
                        <thead>
                            <tr className="border-b border-border">
                                <th className="text-left py-4 px-6 w-1/3">Feature</th>
                                <th className="text-center py-4 px-6 w-1/5 text-lg font-semibold">Starter</th>
                                <th className="text-center py-4 px-6 w-1/5 text-lg font-semibold text-primary">Pro</th>
                                <th className="text-center py-4 px-6 w-1/5 text-lg font-semibold">Business</th>
                            </tr>
                        </thead>
                        <tbody>
                            {features.map((category) => (
                                <React.Fragment key={category.category}>
                                    <tr className="bg-muted/50">
                                        <td colSpan={4} className="py-3 px-6 font-semibold text-sm uppercase tracking-wider text-muted-foreground">
                                            {category.category}
                                        </td>
                                    </tr>
                                    {category.items.map((item, index) => (
                                        <motion.tr
                                            initial={{ opacity: 0 }}
                                            whileInView={{ opacity: 1 }}
                                            viewport={{ once: true }}
                                            transition={{ delay: index * 0.05 }}
                                            key={item.name}
                                            className="border-b border-border/50 hover:bg-muted/30 transition-colors"
                                        >
                                            <td className="py-4 px-6 flex items-center gap-2">
                                                <span>{item.name}</span>
                                                <TooltipProvider>
                                                    <Tooltip>
                                                        <TooltipTrigger>
                                                            <Info className="w-4 h-4 text-muted-foreground/50 hover:text-primary transition-colors cursor-help" />
                                                        </TooltipTrigger>
                                                        <TooltipContent>
                                                            <p>Details about {item.name} feature.</p>
                                                        </TooltipContent>
                                                    </Tooltip>
                                                </TooltipProvider>
                                            </td>
                                            <td className="text-center py-4 px-6 text-muted-foreground">
                                                <CellContent value={item.free} />
                                            </td>
                                            <td className="text-center py-4 px-6 font-medium text-foreground">
                                                <CellContent value={item.pro} isPro={true} />
                                            </td>
                                            <td className="text-center py-4 px-6 text-muted-foreground">
                                                <CellContent value={item.business} />
                                            </td>
                                        </motion.tr>
                                    ))}
                                </React.Fragment>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    );
}

function CellContent({ value, isPro }: { value: string | boolean; isPro?: boolean }) {
    if (value === true) {
        return (
            <div className="flex justify-center">
                <Check className={`w-5 h-5 ${isPro ? 'text-primary' : 'text-emerald-500'}`} />
            </div>
        );
    }
    if (value === false) {
        return (
            <div className="flex justify-center">
                <Minus className="w-5 h-5 text-muted-foreground/30" />
            </div>
        );
    }
    return <span>{value}</span>;
}
