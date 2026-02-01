'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { CareersHero } from '@/features/company/ui/CareersHero';
import { ValuesSection } from '@/features/company/ui/ValuesSection';
import { OpenPositions } from '@/features/company/ui/OpenPositions';
import { CTASection } from '@/features/landing/ui/CTASection';

export default function CareersPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <CareersHero />
                <ValuesSection />
                <OpenPositions />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
