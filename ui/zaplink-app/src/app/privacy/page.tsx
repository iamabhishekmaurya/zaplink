'use client';

import MainLayout from '@/components/layout/MainLayout';
import { motion } from 'framer-motion';

export default function PrivacyPage() {
    return (
        <MainLayout>
            <div className="py-20 bg-muted/30">
                <div className="container mx-auto px-4">
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="max-w-3xl mx-auto"
                    >
                        <h1 className="text-4xl md:text-5xl font-black mb-6">Privacy <span className="text-primary italic">Policy</span></h1>
                        <p className="text-muted-foreground">Last updated: December 28, 2025</p>
                    </motion.div>
                </div>
            </div>

            <div className="py-20">
                <div className="container mx-auto px-4">
                    <article className="max-w-3xl mx-auto prose prose-neutral dark:prose-invert">
                        <p className="text-lg leading-relaxed">
                            At Zaplink, we take your privacy seriously. This policy describes how we collect, use, and handle your information when you use our services.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">1. Information We Collect</h2>
                        <p className="text-muted-foreground leading-relaxed mb-4">
                            We collect information you provide directly to us, such as when you create an account, shorten a URL, or contact support. This includes:
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li>Contact information (email address)</li>
                            <li>Authentication credentials (passwords)</li>
                            <li>URLs that you shorten</li>
                            <li>User profile information</li>
                        </ul>

                        <h2 className="text-2xl font-bold mt-12 mb-6">2. Information Collected Automatically</h2>
                        <p className="text-muted-foreground leading-relaxed mb-4">
                            When someone clicks a Zaplink short URL, we automatically collect certain information for analytics purposes:
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li>IP address (obfuscated for privacy)</li>
                            <li>Approximate geographic location (City/Country)</li>
                            <li>Referrer header (where the click originated)</li>
                            <li>User agent (Browser and Device type)</li>
                        </ul>

                        <h2 className="text-2xl font-bold mt-12 mb-6">3. How We Use Information</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We use the collected information to provide, maintain, and improve our services, including providing analytics to our users, detecting fraud (scams/malware), and communicating with you about your account.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">4. Data Sharing</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We do not sell your personal data. We may share information with third-party service providers (like hosting platforms or database providers) solely to operate our service.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">5. Your Rights</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            You have the right to access, update, or delete your account and associated links at any time through our dashboard.
                        </p>
                    </article>
                </div>
            </div>
        </MainLayout>
    );
}
