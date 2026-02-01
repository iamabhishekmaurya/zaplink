'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { AboutHero } from '@/features/company/ui/AboutHero';
import { OurStory } from '@/features/company/ui/OurStory';
import { TeamGrid } from '@/features/company/ui/TeamGrid';
import { CTASection } from '@/features/landing/ui/CTASection';

export default function AboutPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <AboutHero />
                <OurStory />
                <TeamGrid />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
