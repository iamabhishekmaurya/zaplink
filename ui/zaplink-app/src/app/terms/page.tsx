'use client';

import MainLayout from '@/components/layout/MainLayout';
import { motion } from 'framer-motion';

export default function TermsPage() {
    return (
        <MainLayout>
            <div className="py-20 bg-muted/30">
                <div className="container mx-auto px-4">
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="max-w-3xl mx-auto"
                    >
                        <h1 className="text-4xl md:text-5xl font-black mb-6">Terms of <span className="text-primary italic">Service</span></h1>
                        <p className="text-muted-foreground">Last updated: December 28, 2025</p>
                    </motion.div>
                </div>
            </div>

            <div className="py-20">
                <div className="container mx-auto px-4">
                    <article className="max-w-3xl mx-auto prose prose-neutral dark:prose-invert">
                        <p className="text-lg leading-relaxed">
                            By using Zaplink, you agree to these terms. Please read them carefully.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">1. Usage Agreement</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            Zaplink provides a URL shortening service. By using our service, you represent that you are at least 13 years old and that your use of the service does not violate any applicable law or regulation.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">2. Prohibited Content</h2>
                        <p className="text-muted-foreground leading-relaxed mb-4">
                            You may not use Zaplink for any illegal or unauthorized purpose. Prohibited uses include, but are not limited to:
                        </p>
                        <ul className="list-disc pl-6 space-y-2 text-muted-foreground">
                            <li>Spamming or sending unsolicited communications</li>
                            <li>Distributing malware, viruses, or other harmful code</li>
                            <li>Phishing or identity theft attempts</li>
                            <li>Sharing content that violates intellectual property rights</li>
                        </ul>

                        <h2 className="text-2xl font-bold mt-12 mb-6">3. Account Responsibility</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            You are responsible for maintaining the security of your account and password. Zaplink cannot and will not be liable for any loss or damage from your failure to comply with this security obligation.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">4. Termination</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            We reserve the right to suspend or terminate your account and disable any short links that violate our terms of service or are reported for abuse by third parties.
                        </p>

                        <h2 className="text-2xl font-bold mt-12 mb-6">5. Limitation of Liability</h2>
                        <p className="text-muted-foreground leading-relaxed">
                            Zaplink is provided "as is" without any warranties. We shall not be liable for any damages arising out of the use or inability to use our services.
                        </p>
                    </article>
                </div>
            </div>
        </MainLayout>
    );
}
