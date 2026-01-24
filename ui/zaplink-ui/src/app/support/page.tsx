'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";
import { SupportHero } from "@/components/support/SupportHero";
import { SupportOptions } from "@/components/support/SupportOptions";
import { ContactSection } from "@/components/support/ContactSection";

export default function SupportPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <SupportHero />
                <SupportOptions />
                <ContactSection />
            </main>
            <FooterSection />
        </div>
    );
}
