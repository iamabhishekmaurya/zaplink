'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";
import { CareersHero } from "@/components/company/CareersHero";
import { ValuesSection } from "@/components/company/ValuesSection";
import { OpenPositions } from "@/components/company/OpenPositions";
import { CTASection } from "@/components/landing/CTASection";

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
