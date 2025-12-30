'use client';

import { BorderBeam } from "@/components/ui/border-beam";
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useAuth } from '@/hooks/useAuth';
import api from "@/utils/api";
import { motion } from 'framer-motion';
import { ArrowLeft, CheckCircle, KeyRound, Mail } from 'lucide-react';
import Link from 'next/link';
import { useRouter, useSearchParams } from 'next/navigation';
import { Suspense, useEffect, useState } from 'react';
import { toast } from 'sonner';

function VerifyEmailContent() {

    const { user } = useAuth();
    const [token, setToken] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isVerified, setIsVerified] = useState(false);
    const [resendCooldown, setResendCooldown] = useState(60);
    const router = useRouter();
    const searchParams = useSearchParams();

    useEffect(() => {
        const tokenFromUrl = searchParams.get('token');
        if (tokenFromUrl) {
            setToken(tokenFromUrl);
        }
    }, [searchParams]);

    useEffect(() => {
        let interval: NodeJS.Timeout;
        if (resendCooldown > 0) {
            interval = setInterval(() => {
                setResendCooldown((prev) => prev - 1);
            }, 1000);
        }
        return () => clearInterval(interval);
    }, [resendCooldown]);

    const handleResend = async () => {
        if (!user?.email) {
            toast.error("User email not found. Please login again.");
            return;
        }

        try {
            await api.post(`/auth/resend-verification?email=${user.email}`);
            toast.success("Verification code resent! Please check your inbox.");
            setResendCooldown(60);
        } catch (error) {
            toast.error("Failed to resend code. Please try again later.");
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);
        try {
            await api.post(`/auth/verify-email?token=${token}`);
            setIsVerified(true);
            toast.success('Email verified successfully!');
            setTimeout(() => {
                router.push('/dashboard');
            }, 2000);
        } catch (error: any) {
            toast.error(error.response?.data?.message || 'Verification failed. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    if (isVerified) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-muted/30 p-4 relative overflow-hidden">
                {/* Decorative Blur */}
                <div className="absolute top-0 right-0 w-96 h-96 bg-primary/10 blur-[100px] rounded-full translate-x-1/2 -translate-y-1/2" />
                <div className="absolute bottom-0 left-0 w-80 h-80 bg-primary/10 blur-[100px] rounded-full -translate-x-1/2 translate-y-1/2" />

                <motion.div
                    initial={{ opacity: 0, scale: 0.95 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ duration: 0.3 }}
                    className="w-full max-w-md z-10"
                >
                    <Card className="border-border/50 shadow-2xl backdrop-blur-sm bg-background/95 relative overflow-hidden">
                        <CardHeader className="text-center">
                            <div className="mx-auto w-12 h-12 bg-green-100 dark:bg-green-900/20 rounded-full flex items-center justify-center mb-4">
                                <CheckCircle className="h-6 w-6 text-green-600 dark:text-green-400" />
                            </div>
                            <CardTitle className="text-2xl font-bold font-display">Email Verified</CardTitle>
                            <CardDescription>
                                Your email address has been successfully verified. Redirecting to dashboard...
                            </CardDescription>
                        </CardHeader>
                        <CardFooter className="flex justify-center border-t py-6">
                            <Button onClick={() => router.push('/dashboard')} className="w-full">
                                Continue to Dashboard
                            </Button>
                        </CardFooter>
                        <BorderBeam size={250} duration={12} delay={9} />
                    </Card>
                </motion.div>
            </div>
        );
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-muted/30 p-4 relative overflow-hidden">
            {/* Decorative Blur */}
            <div className="absolute top-0 right-0 w-96 h-96 bg-primary/10 blur-[100px] rounded-full translate-x-1/2 -translate-y-1/2" />
            <div className="absolute bottom-0 left-0 w-80 h-80 bg-primary/10 blur-[100px] rounded-full -translate-x-1/2 translate-y-1/2" />

            <motion.div
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.3 }}
                className="w-full max-w-md z-10"
            >
                <div className="mb-8 flex flex-col items-center gap-2">
                    <Link href="/" className="flex items-center gap-2 mb-2 hover:scale-105 transition-transform">
                        <div className="bg-primary p-1.5 rounded-lg shadow-lg">
                            <KeyRound className="h-5 w-5 text-primary-foreground" />
                        </div>
                        <span className="text-2xl font-bold font-display tracking-tight">Zaplink</span>
                    </Link>
                    <p className="text-muted-foreground font-medium">Secure your account</p>
                </div>

                <Card className="border-border/50 shadow-2xl backdrop-blur-sm bg-background/95 relative overflow-hidden">
                    <CardHeader className="space-y-1 text-center">
                        <CardTitle className="text-2xl font-bold font-display">Verify Email</CardTitle>
                        <CardDescription>Enter the verification token sent to your email</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="space-y-2">
                                <Label htmlFor="token" className="font-medium">Verification Token</Label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <KeyRound className="h-4 w-4 text-muted-foreground" />
                                    </div>
                                    <Input
                                        id="token"
                                        placeholder="Paste your token here"
                                        required
                                        className="h-11 pl-10"
                                        value={token}
                                        onChange={(e) => setToken(e.target.value)}
                                    />
                                </div>
                            </div>
                            <Button type="submit" className="w-full h-11 text-base font-semibold" disabled={isSubmitting}>
                                {isSubmitting ? (
                                    <div className="flex items-center gap-2">
                                        <div className="h-4 w-4 border-2 border-primary-foreground border-t-transparent animate-spin rounded-full" />
                                        Verifying...
                                    </div>
                                ) : (
                                    'Verify Email'
                                )}
                            </Button>
                        </form>
                    </CardContent>
                    <CardFooter className="flex flex-col gap-4 text-center border-t py-6">
                        <p className="text-sm text-muted-foreground">
                            Didn't receive a code?{" "}
                            <button
                                onClick={handleResend}
                                disabled={resendCooldown > 0}
                                className={`font-bold bg-transparent border-none cursor-pointer ${resendCooldown > 0
                                    ? 'text-muted-foreground cursor-not-allowed'
                                    : 'text-primary hover:underline'
                                    }`}
                            >
                                {resendCooldown > 0 ? `Resend in ${resendCooldown}s` : 'Resend'}
                            </button>
                        </p>
                    </CardFooter>
                    <BorderBeam size={250} duration={12} delay={9} />
                </Card>

                <Link href="/dashboard" className="mt-8 flex items-center justify-center gap-2 text-sm text-muted-foreground hover:text-primary transition-colors font-medium">
                    <ArrowLeft className="h-4 w-4" /> Back to dashboard
                </Link>
            </motion.div>
        </div>
    );
}

export default function VerifyEmailPage() {
    return (
        <Suspense fallback={<div>Loading...</div>}>
            <VerifyEmailContent />
        </Suspense>
    );
}
