'use client';

import { Button } from "@/components/ui/button";
import { Facebook, Instagram, Linkedin, Twitter, Zap } from "lucide-react";
import Image from "next/image";
import Link from 'next/link';

export const FooterSection = () => {
    return (
        <footer className="bg-background border-t border-border py-12">
            <div className="max-w-7xl mx-auto px-4 md:px-6">
                <div className="flex flex-col lg:flex-row gap-12 lg:gap-24 mb-12">
                    <div className="lg:w-1/3">
                        <Link href="/" className="flex items-center gap-2 mb-4">
                            <Image src="/logo-light.png" alt="Logo" width={24} height={24} className="block dark:hidden" />
                            <Image src="/logo-dark.png" alt="Logo" width={24} height={24} className="hidden dark:block" />
                            <span className="text-lg font-semibold text-foreground">Zaipme</span>
                        </Link>
                        <p className="text-sm text-muted-foreground mb-6 leading-relaxed">
                            The advanced URL shortener for modern marketing teams.
                            Built for speed, security, and scale.
                        </p>
                        <div className="flex gap-2">
                            {[Twitter, Facebook, Instagram, Linkedin].map((Icon, i) => (
                                <Button key={i} variant="ghost" size="icon" className="w-9 h-9 rounded-full text-muted-foreground hover:text-foreground hover:bg-muted">
                                    <Icon className="w-4 h-4" />
                                </Button>
                            ))}
                        </div>
                    </div>

                    <div className="flex-1 grid grid-cols-2 md:grid-cols-3 gap-8">
                        {[
                            {
                                title: "Product",
                                links: [
                                    { label: "Features", href: "/features" },
                                    { label: "Pricing", href: "/pricing" },
                                    { label: "Analytics", href: "/product#analytics" },
                                    { label: "QR Codes", href: "/product#qr-codes" }
                                ]
                            },
                            {
                                title: "Resources",
                                links: [
                                    { label: "Blog", href: "/blogs" },
                                    { label: "Resources", href: "/resources" },
                                    { label: "Guides", href: "/resources" },
                                    { label: "Support", href: "/support" }
                                ]
                            },
                            {
                                title: "Company",
                                links: [
                                    { label: "About", href: "/about" },
                                    { label: "Careers", href: "/careers" },
                                    { label: "Legal", href: "/legal" },
                                    { label: "Contact", href: "/contact" }
                                ]
                            },
                        ].map((section) => (
                            <div key={section.title}>
                                <h4 className="font-semibold text-foreground text-sm mb-4">{section.title}</h4>
                                <ul className="space-y-3">
                                    {section.links.map((link) => (
                                        <li key={link.label}>
                                            <Link href={link.href} className="text-sm text-muted-foreground hover:text-primary transition-colors">
                                                {link.label}
                                            </Link>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="border-t border-border pt-8 flex flex-col md:flex-row justify-between items-center gap-4">
                    <p className="text-sm text-muted-foreground">
                        © {new Date().getFullYear()} zaipme. All rights reserved.
                    </p>
                    <div className="flex gap-6">
                        <Link href="/privacy" className="text-sm text-muted-foreground hover:text-primary transition-colors">Privacy Policy</Link>
                        <Link href="/terms" className="text-sm text-muted-foreground hover:text-primary transition-colors">Terms of Service</Link>
                    </div>
                </div>
            </div>
        </footer>
    );
};
