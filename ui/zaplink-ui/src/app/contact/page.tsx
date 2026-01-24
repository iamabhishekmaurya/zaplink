'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";
import { ContactSection } from "@/components/support/ContactSection";


export default function ContactPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 pt-20">
                <div className="text-center py-20 bg-muted/20">
                    <h1 className="text-4xl md:text-5xl font-bold mb-4">Contact Sales</h1>
                    <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
                        Ready to empower your team? Let's talk about how Zaplink can help you scale.
                    </p>
                </div>
                {/* Reusing the form from support but technically it works for contact too. 
                    Ideally we would split this or prop it, but for speed reuse is fine. */}
                <ContactSection />
            </main>
            <FooterSection />
        </div>
    );
}
