import Link from 'next/link';
import { Link as LinkIcon, Twitter, Github, Linkedin, Mail } from 'lucide-react';

export default function Footer() {
    return (
        <footer className="bg-muted/30 border-t pt-16 pb-8">
            <div className="container mx-auto px-4 md:px-6">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12 mb-12">
                    {/* Brand */}
                    <div className="flex flex-col gap-4">
                        <Link href="/" className="flex items-center gap-2">
                            <div className="bg-primary p-1.5 rounded-lg">
                                <LinkIcon className="h-5 w-5 text-primary-foreground" />
                            </div>
                            <span className="text-xl font-bold tracking-tight">Zaplink</span>
                        </Link>
                        <p className="text-muted-foreground text-sm leading-relaxed max-w-xs">
                            Empowering individuals and businesses to share more with less. Shorten, track, and optimize your links at scale.
                        </p>
                        <div className="flex items-center gap-4 mt-2">
                            <Twitter className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer transition-colors" />
                            <Github className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer transition-colors" />
                            <Linkedin className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer transition-colors" />
                            <Mail className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer transition-colors" />
                        </div>
                    </div>

                    {/* Product */}
                    <div className="flex flex-col gap-4">
                        <h4 className="font-semibold text-sm uppercase tracking-wider text-muted-foreground">Product</h4>
                        <ul className="flex flex-col gap-2">
                            <li><Link href="/features" className="text-sm hover:text-primary transition-colors">URL Shortener</Link></li>
                            <li><Link href="/features" className="text-sm hover:text-primary transition-colors">QR Codes</Link></li>
                            <li><Link href="/features" className="text-sm hover:text-primary transition-colors">Link Management</Link></li>
                            <li><Link href="/features" className="text-sm hover:text-primary transition-colors">Advanced Analytics</Link></li>
                        </ul>
                    </div>

                    {/* Resources */}
                    <div className="flex flex-col gap-4">
                        <h4 className="font-semibold text-sm uppercase tracking-wider text-muted-foreground">Resources</h4>
                        <ul className="flex flex-col gap-2">
                            <li><Link href="/resources" className="text-sm hover:text-primary transition-colors">API Documentation</Link></li>
                            <li><Link href="/resources" className="text-sm hover:text-primary transition-colors">Success Stories</Link></li>
                            <li><Link href="/resources" className="text-sm hover:text-primary transition-colors">Developer Blog</Link></li>
                            <li><Link href="/resources" className="text-sm hover:text-primary transition-colors">Help Center</Link></li>
                        </ul>
                    </div>

                    {/* Legal */}
                    <div className="flex flex-col gap-4">
                        <h4 className="font-semibold text-sm uppercase tracking-wider text-muted-foreground">Legal</h4>
                        <ul className="flex flex-col gap-2">
                            <li><Link href="/privacy" className="text-sm hover:text-primary transition-colors">Privacy Policy</Link></li>
                            <li><Link href="/terms" className="text-sm hover:text-primary transition-colors">Terms of Service</Link></li>
                            <li><Link href="/privacy" className="text-sm hover:text-primary transition-colors">Cookie Settings</Link></li>
                            <li><Link href="/privacy" className="text-sm hover:text-primary transition-colors">Security</Link></li>
                        </ul>
                    </div>
                </div>

                <div className="pt-8 border-t flex flex-col md:flex-row justify-between items-center gap-4 text-xs text-muted-foreground">
                    <p>© {new Date().getFullYear()} Zaplink Inc. All rights reserved.</p>
                    <div className="flex items-center gap-6">
                        <Link href="/privacy" className="hover:text-primary transition-colors">Privacy</Link>
                        <Link href="/terms" className="hover:text-primary transition-colors">Terms</Link>
                        <Link href="/privacy" className="hover:text-primary transition-colors">Cookies</Link>
                    </div>
                </div>
            </div>
        </footer>
    );
}
