'use client';

import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

export default function LegalPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 py-32 container mx-auto px-4 md:px-6 max-w-4xl">
                <h1 className="text-4xl font-bold mb-8 tracking-tight">Legal Center</h1>

                <Tabs defaultValue="privacy" className="w-full">
                    <TabsList className="mb-8 w-full justify-start h-auto flex-wrap gap-2 bg-transparent p-0">
                        <TabsTrigger value="privacy" className="px-6 py-2 rounded-full border border-transparent data-[state=active]:bg-primary/10 data-[state=active]:text-primary data-[state=active]:border-primary/20 transition-all">Privacy Policy</TabsTrigger>
                        <TabsTrigger value="terms" className="px-6 py-2 rounded-full border border-transparent data-[state=active]:bg-primary/10 data-[state=active]:text-primary data-[state=active]:border-primary/20 transition-all">Terms of Service</TabsTrigger>
                        <TabsTrigger value="cookies" className="px-6 py-2 rounded-full border border-transparent data-[state=active]:bg-primary/10 data-[state=active]:text-primary data-[state=active]:border-primary/20 transition-all">Cookie Policy</TabsTrigger>
                    </TabsList>

                    <TabsContent value="privacy" className="space-y-6 mt-4">
                        <div className="space-y-2">
                            <h3 className="text-2xl font-bold">Privacy Policy</h3>
                            <p className="text-sm text-muted-foreground">Last updated: January 24, 2026</p>
                        </div>
                        <p className="text-muted-foreground leading-relaxed">
                            At Zaplink, we take your privacy seriously. This Privacy Policy explains how we collect, use, disclosure, and safeguard your information when you visit our website.
                        </p>
                        <div className="space-y-3">
                            <h4 className="text-xl font-semibold">1. Information We Collect</h4>
                            <p className="text-muted-foreground">We may collect information about you in a variety of ways. The information we may collect on the Site includes:</p>
                            <ul className="list-disc pl-5 space-y-2 text-muted-foreground">
                                <li><strong>Personal Data:</strong> Personally identifiable information, such as your name, shipping address, email address, and telephone number.</li>
                                <li><strong>Derivative Data:</strong> Information our servers automatically collect when you access the Site, such as your IP address, your browser type, your operating system, your access times, and the pages you have viewed directly before and after accessing the Site.</li>
                            </ul>
                        </div>
                    </TabsContent>

                    <TabsContent value="terms" className="space-y-6 mt-4">
                        <div className="space-y-2">
                            <h3 className="text-2xl font-bold">Terms of Service</h3>
                            <p className="text-sm text-muted-foreground">Last updated: January 24, 2026</p>
                        </div>
                        <p className="text-muted-foreground leading-relaxed">
                            These Terms of Service ("Terms") govern your access to and use of Zaplink services.
                        </p>
                        <div className="space-y-3">
                            <h4 className="text-xl font-semibold">1. Acceptance of Terms</h4>
                            <p className="text-muted-foreground leading-relaxed">By accessing or using our Services, you agree to be bound by these Terms. If you do not agree to these Terms, you may not access or use the Services.</p>
                        </div>
                        <div className="space-y-3">
                            <h4 className="text-xl font-semibold">2. Changes to Terms</h4>
                            <p className="text-muted-foreground leading-relaxed">We reserve the right to modify these Terms at any time. We will provide notice of significant changes by posting the new Terms on the Site.</p>
                        </div>
                    </TabsContent>

                    <TabsContent value="cookies" className="space-y-6 mt-4">
                        <div className="space-y-2">
                            <h3 className="text-2xl font-bold">Cookie Policy</h3>
                        </div>
                        <p className="text-muted-foreground leading-relaxed">We use cookies to improve your experience. By using Zaplink, you agree to our use of cookies.</p>
                    </TabsContent>
                </Tabs>
            </main>
            <FooterSection />
        </div>
    );
}
