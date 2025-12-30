'use client';

import { BorderBeam } from "@/components/ui/border-beam";
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import api from '@/utils/api';
import axios from 'axios';
import { AnimatePresence, motion } from 'framer-motion';
import { ArrowLeft, CheckCircle, Link as LinkIcon, Mail } from 'lucide-react';
import Link from 'next/link';
import { useState } from 'react';
import { toast } from 'sonner';

export default function ForgotPasswordPage() {
    const [email, setEmail] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [isSubmitted, setIsSubmitted] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!email) {
            toast.error('Please enter your email address');
            return;
        }

        setIsLoading(true);
        try {
            await api.post('/auth/forgot-password', { email });
            setIsSubmitted(true);
            toast.success('Password reset instructions sent to your email');
        } catch (error: unknown) {
            let message = 'Failed to send reset instructions';
            if (axios.isAxiosError(error)) {
                message = error.response?.data?.message || message;
            }
            toast.error(message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-muted/30 p-4 relative overflow-hidden">
            {/* Decorative Background */}
            <div className="absolute top-0 right-0 w-96 h-96 bg-gradient-to-br from-primary/20 to-accent/10 blur-[100px] rounded-full translate-x-1/2 -translate-y-1/2" />
            <div className="absolute bottom-0 left-0 w-80 h-80 bg-gradient-to-tr from-accent/20 to-primary/10 blur-[100px] rounded-full -translate-x-1/2 translate-y-1/2" />

            <motion.div
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.3 }}
                className="w-full max-w-md z-10"
            >
                <div className="mb-8 flex flex-col items-center gap-2">
                    <Link href="/" className="flex items-center gap-2 mb-2 hover:scale-105 transition-transform">
                        <motion.div
                            className="bg-gradient-to-r from-primary to-accent p-2 rounded-xl shadow-lg"
                            whileHover={{ rotate: 12, scale: 1.05 }}
                        >
                            <LinkIcon className="h-6 w-6 text-white" />
                        </motion.div>
                        <span className="text-2xl font-bold tracking-tight">Zaplink</span>
                    </Link>
                    <p className="text-muted-foreground font-medium">Reset your password</p>
                </div>

                <Card className="border-border/50 shadow-2xl backdrop-blur-sm bg-background/95 relative overflow-hidden">
                    <CardHeader className="space-y-1 text-center">
                        <CardTitle className="text-2xl font-bold">
                            {isSubmitted ? 'Check your email' : 'Forgot your password?'}
                        </CardTitle>
                        <CardDescription>
                            {isSubmitted
                                ? 'We have sent password reset instructions to your email'
                                : 'Enter your email address and we will send you a link to reset your password'
                            }
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <AnimatePresence mode="wait">
                            {!isSubmitted ? (
                                <motion.form
                                    key="form"
                                    onSubmit={handleSubmit}
                                    className="space-y-4"
                                    initial={{ opacity: 0, x: -20 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    exit={{ opacity: 0, x: 20 }}
                                    transition={{ duration: 0.3 }}
                                >
                                    <div className="space-y-2">
                                        <Label htmlFor="email" className="font-medium">Email Address</Label>
                                        <div className="relative">
                                            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                                <Mail className="h-4 w-4 text-muted-foreground" />
                                            </div>
                                            <Input
                                                id="email"
                                                type="email"
                                                placeholder="name@example.com"
                                                required
                                                className="h-11 pl-10"
                                                value={email}
                                                onChange={(e) => setEmail(e.target.value)}
                                            />
                                        </div>
                                    </div>
                                    <Button
                                        type="submit"
                                        className="w-full h-11 text-base font-semibold gradient-primary border-0"
                                        disabled={isLoading}
                                    >
                                        {isLoading ? (
                                            <div className="flex items-center gap-2">
                                                <div className="h-4 w-4 border-2 border-white border-t-transparent animate-spin rounded-full" />
                                                Sending...
                                            </div>
                                        ) : (
                                            'Send Reset Instructions'
                                        )}
                                    </Button>
                                </motion.form>
                            ) : (
                                <motion.div
                                    key="success"
                                    className="text-center space-y-6"
                                    initial={{ opacity: 0, x: 20 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    exit={{ opacity: 0, x: -20 }}
                                    transition={{ duration: 0.3 }}
                                >
                                    <div className="flex justify-center">
                                        <motion.div
                                            initial={{ scale: 0 }}
                                            animate={{ scale: 1 }}
                                            transition={{ delay: 0.2, type: "spring", stiffness: 200 }}
                                            className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center"
                                        >
                                            <CheckCircle className="h-8 w-8 text-green-600" />
                                        </motion.div>
                                    </div>
                                    <div className="space-y-2">
                                        <p className="text-sm text-muted-foreground">
                                            We&apos;ve sent a password reset link to:
                                        </p>
                                        <p className="font-medium text-foreground bg-muted/50 px-3 py-2 rounded-lg">
                                            {email}
                                        </p>
                                    </div>
                                    <div className="space-y-3">
                                        <p className="text-xs text-muted-foreground">
                                            Didn&apos;t receive the email? Check your spam folder or
                                        </p>
                                        <Button
                                            variant="outline"
                                            className="w-full"
                                            onClick={() => setIsSubmitted(false)}
                                        >
                                            Try again
                                        </Button>
                                    </div>
                                </motion.div>
                            )}
                        </AnimatePresence>

                        {!isSubmitted && (
                            <div className="mt-6 text-center">
                                <p className="text-sm text-muted-foreground">
                                    Remember your password?{" "}
                                    <Link href="/login" className="text-primary hover:underline font-bold">
                                        Back to login
                                    </Link>
                                </p>
                            </div>
                        )}
                    </CardContent>
                    <BorderBeam size={250} duration={12} delay={9} />
                </Card>

                <Link href="/" className="mt-8 flex items-center justify-center gap-2 text-sm text-muted-foreground hover:text-primary transition-colors font-medium">
                    <ArrowLeft className="h-4 w-4" /> Back to home
                </Link>
            </motion.div>
        </div>
    );
}
