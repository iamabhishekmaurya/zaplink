'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";

export default function TermsOfServicePage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 py-32 container mx-auto px-4 md:px-6 max-w-4xl">
                <div className="space-y-8">
                    <div className="border-b border-border pb-8">
                        <h1 className="text-4xl font-bold mb-4 tracking-tight">Terms of Service</h1>
                        <p className="text-lg text-muted-foreground">Last updated: January 24, 2026</p>
                    </div>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">1. Agreement to Terms</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            These Terms of Service constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and
                            <span className="font-semibold text-foreground"> Zaplink</span> ("we," "us" or "our"), concerning your access to and use of the Zaplink website as well as any other media form, media channel,
                            mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the "Site").
                        </p>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">2. Intellectual Property Rights</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            Unless otherwise indicated, the Site is our proprietary property and all source code, databases, functionality, software, website designs,
                            audio, video, text, photographs, and graphics on the Site (collectively, the "Content") and the trademarks, service marks, and logos contained therein
                            (the "Marks") are owned or controlled by us or licensed to us, and are protected by copyright and trademark laws and various other intellectual property rights.
                        </p>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">3. User Representations</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            By using the Site, you represent and warrant that:
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li>All registration information you submit will be true, accurate, current, and complete.</li>
                            <li>You will maintain the accuracy of such information and promptly update such registration information as necessary.</li>
                            <li>You have the legal capacity and you agree to comply with these Terms of Service.</li>
                            <li>You are not a minor in the jurisdiction in which you reside.</li>
                        </ul>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">4. Prohibited Activities</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            You may not access or use the Site for any purpose other than that for which we make the Site available.
                            The Site may not be used in connection with any commercial endeavors except those that are specifically endorsed or approved by us.
                        </p>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">5. Termination</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We may terminate or suspend your account immediately, without prior notice or liability, for any reason whatsoever, including without limitation if you breach the Terms.
                            Upon termination, your right to use the Service will immediately cease.
                        </p>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">6. Governing Law</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            These Terms shall be governed and construed in accordance with the laws of California, United States, without regard to its conflict of law provisions.
                        </p>
                    </section>

                    <section className="space-y-4 bg-muted/30 p-6 rounded-lg border border-border">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">7. Contact Us</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            In order to resolve a complaint regarding the Site or to receive further information regarding use of the Site, please contact us at:
                        </p>
                        <div className="mt-4">
                            <span className="text-primary font-medium text-lg">legal@zaplink.com</span>
                        </div>
                    </section>
                </div>
            </main>
            <FooterSection />
        </div>
    );
}
