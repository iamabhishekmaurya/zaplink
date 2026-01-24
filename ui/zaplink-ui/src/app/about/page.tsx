'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";
import { AboutHero } from "@/components/company/AboutHero";
import { OurStory } from "@/components/company/OurStory";
import { TeamGrid } from "@/components/company/TeamGrid";
import { CTASection } from "@/components/landing/CTASection";

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
