'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";

export default function PrivacyPolicyPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 py-32 container mx-auto px-4 md:px-6 max-w-4xl">
                <div className="space-y-8">
                    <div className="border-b border-border pb-8">
                        <h1 className="text-4xl font-bold mb-4 tracking-tight">Privacy Policy</h1>
                        <p className="text-lg text-muted-foreground">Last updated: January 24, 2026</p>
                    </div>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">1. Introduction</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            Welcome to Zaplink ("we," "our," or "us"). We are committed to protecting your personal information and your right to privacy.
                            If you have any questions or concerns about this privacy notice or our practices with regard to your personal information,
                            please contact us at <span className="text-primary">privacy@zaplink.com</span>.
                        </p>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">2. Information We Collect</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We collect personal information that you voluntarily provide to us when you register on the Services,
                            express an interest in obtaining information about us or our products and Services, when you participate in activities on the Services,
                            or otherwise when you contact us.
                        </p>

                        <h3 className="text-xl font-medium pt-2">Personal Information Provided by You</h3>
                        <p className="text-muted-foreground leading-relaxed">
                            The personal information that we collect depends on the context of your interactions with us and the Services,
                            the choices you make, and the products and features you use. The personal information we collect may include:
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li>Names</li>
                            <li>Email addresses</li>
                            <li>Billing addresses</li>
                            <li>Debit/credit card numbers</li>
                            <li>Passwords</li>
                        </ul>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">3. How We Use Your Information</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We use personal information collected via our Services for a variety of business purposes described below.
                            We process your personal information for these purposes in reliance on our legitimate business interests,
                            in order to enter into or perform a contract with you, with your consent, and/or for compliance with our legal obligations.
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li>To facilitate account creation and logon process.</li>
                            <li>To post testimonials.</li>
                            <li>To request feedback.</li>
                            <li>To enable user-to-user communications.</li>
                            <li>To manage user accounts.</li>
                        </ul>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">4. Sharing Your Information</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We may process or share your data that we hold based on the following legal basis:
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li><strong className="text-foreground">Consent:</strong> We may process your data if you have given us specific consent to use your personal information for a specific purpose.</li>
                            <li><strong className="text-foreground">Legitimate Interests:</strong> We may process your data when it is reasonably necessary to achieve our legitimate business interests.</li>
                            <li><strong className="text-foreground">Performance of a Contract:</strong> Where we have entered into a contract with you, we may process your personal information to fulfill the terms of our contract.</li>
                        </ul>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">5. Your Privacy Rights (GDPR)</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            If you are a resident of the European Economic Area (EEA), you have certain data protection rights. Zaplink aims to take reasonable steps to allow you to correct, amend, delete, or limit the use of your Personal Data.
                        </p>
                        <div className="grid md:grid-cols-2 gap-4 mt-4">
                            <div className="bg-card border border-border/50 p-4 rounded-lg">
                                <h4 className="font-semibold mb-2">Right to Access</h4>
                                <p className="text-sm text-muted-foreground">You have the right to request copies of your personal data.</p>
                            </div>
                            <div className="bg-card border border-border/50 p-4 rounded-lg">
                                <h4 className="font-semibold mb-2">Right to Rectification</h4>
                                <p className="text-sm text-muted-foreground">You have the right to request that we correct any information you believe is inaccurate.</p>
                            </div>
                            <div className="bg-card border border-border/50 p-4 rounded-lg">
                                <h4 className="font-semibold mb-2">Right to Erasure</h4>
                                <p className="text-sm text-muted-foreground">You have the right to request that we erase your personal data ("Right to be Forgotten").</p>
                            </div>
                            <div className="bg-card border border-border/50 p-4 rounded-lg">
                                <h4 className="font-semibold mb-2">Right to Portability</h4>
                                <p className="text-sm text-muted-foreground">You have the right to request that we transfer the data that we have collected to another organization, or directly to you.</p>
                            </div>
                        </div>
                    </section>

                    <section className="space-y-4">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">6. Security of Your Information</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We use administrative, technical, and physical security measures to help protect your personal information.
                            While we have taken reasonable steps to secure the personal information you provide to us, please be aware that despite our efforts,
                            no security measures are perfect or impenetrable, and no method of data transmission can be guaranteed against any interception or other type of misuse.
                        </p>
                    </section>

                    <section className="space-y-4 bg-muted/30 p-6 rounded-lg border border-border">
                        <h2 className="text-2xl font-semibold tracking-tight text-foreground">7. Contact Us</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            If you have questions or comments about this policy, you may email us at <span className="text-primary font-medium">privacy@zaplink.com</span> or by post to:
                        </p>
                        <address className="not-italic text-sm text-muted-foreground mt-4">
                            Zaplink Inc.<br />
                            123 Tech Street<br />
                            San Francisco, CA 94105<br />
                            United States
                        </address>
                    </section>
                </div>
            </main>
            <FooterSection />
        </div>
    );
}
