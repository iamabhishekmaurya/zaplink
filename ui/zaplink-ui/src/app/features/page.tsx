'use client';

import { useState } from 'react';
import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/components/landing/FooterSection';
import { FeaturesHero } from '@/components/features/FeaturesHero';
import { FeatureTabs } from '@/components/features/FeatureTabs';
import { DetailedFeatureSection } from '@/components/features/DetailedFeatureSection';
import { CTASection } from '@/components/landing/CTASection';

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
