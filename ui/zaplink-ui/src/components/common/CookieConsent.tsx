'use client';

import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { motion, AnimatePresence } from 'motion/react';
import Cookies from 'js-cookie';
import { Cookie, X } from 'lucide-react';
import Link from 'next/link';

export const CookieConsent = () => {
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const consent = Cookies.get('zaplink-cookie-consent');
        if (!consent) {
            const timer = setTimeout(() => setIsVisible(true), 1000);
            return () => clearTimeout(timer);
        }
    }, []);

    const handleAccept = () => {
        Cookies.set('zaplink-cookie-consent', 'true', { expires: 365 });
        setIsVisible(false);
    };

    const handleDecline = () => {
        Cookies.set('zaplink-cookie-consent', 'false', { expires: 365 });
        setIsVisible(false);
    }

    return (
        <AnimatePresence>
            {isVisible && (
                <motion.div
                    initial={{ y: 100, opacity: 0 }}
                    animate={{ y: 0, opacity: 1 }}
                    exit={{ y: 100, opacity: 0 }}
                    transition={{ duration: 0.5, ease: "circOut" }}
                    className="fixed bottom-0 left-0 right-0 z-[100] px-4 md:px-8 pb-8"
                >
                    <div className="mx-auto max-w-7xl bg-background/95 backdrop-blur-md rounded-2xl shadow-xl border border-border/50 p-4 md:px-6 md:py-4 flex flex-col md:flex-row items-center justify-between gap-4">

                        {/* Text Content */}
                        <div className="flex items-center gap-3 text-center md:text-left">
                            <div className="hidden md:flex h-10 w-10 items-center justify-center rounded-xl bg-primary/10">
                                <Cookie className="h-5 w-5 text-primary" />
                            </div>
                            <p className="text-sm text-muted-foreground leading-relaxed max-w-2xl">
                                <span className="font-semibold text-foreground">We value your privacy.</span>{' '}
                                We use cookies to enhance your experience. By continuing, you agree to our use of cookies.
                                <Link href="/privacy" className="ml-1 text-primary hover:underline font-medium">
                                    Privacy Policy
                                </Link>
                            </p>
                        </div>

                        {/* Actions */}
                        <div className="flex items-center gap-2 w-full md:w-auto">
                            <Button
                                variant="ghost"
                                onClick={handleDecline}
                                className="flex-1 md:flex-none rounded-full text-muted-foreground hover:text-foreground"
                            >
                                Decline
                            </Button>
                            <Button
                                onClick={handleAccept}
                                className="flex-1 md:flex-none rounded-full bg-primary hover:bg-primary/90 text-primary-foreground shadow-lg shadow-primary/20 px-6"
                            >
                                Accept All
                            </Button>
                            <button
                                onClick={() => setIsVisible(false)}
                                className="md:hidden p-2 text-muted-foreground"
                            >
                                <X className="h-5 w-5" />
                            </button>
                        </div>
                    </div>
                </motion.div>
            )}
        </AnimatePresence>
    );
};
