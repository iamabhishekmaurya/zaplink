'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { SupportHero } from '@/features/support/ui/SupportHero';
import { SupportOptions } from '@/features/support/ui/SupportOptions';
import { ContactSection } from '@/features/support/ui/ContactSection';

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
