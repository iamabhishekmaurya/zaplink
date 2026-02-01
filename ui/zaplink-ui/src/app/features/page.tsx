'use client';

import { useState } from 'react';
import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { FeaturesHero } from '@/features/marketing-features/ui/FeaturesHero';
import { FeatureTabs } from '@/features/marketing-features/ui/FeatureTabs';
import { DetailedFeatureSection } from '@/features/marketing-features/ui/DetailedFeatureSection';
import { CTASection } from '@/features/landing/ui/CTASection';

export default function FeaturesPage() {
    const [activeTab, setActiveTab] = useState("all");

    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <FeaturesHero />
                <FeatureTabs activeTab={activeTab} setActiveTab={setActiveTab} />
                <DetailedFeatureSection activeTab={activeTab} />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
