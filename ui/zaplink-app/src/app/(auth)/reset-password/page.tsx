'use client';

import { useState, useEffect, Suspense } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Link as LinkIcon, Lock, Eye, EyeOff, CheckCircle, AlertCircle } from 'lucide-react';
import { motion } from 'framer-motion';
import { toast } from 'sonner';
import api from '@/utils/api';
import axios from 'axios';

function ResetPasswordContent() {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const [tokenValid, setTokenValid] = useState<boolean | null>(null);

    const router = useRouter();
    const searchParams = useSearchParams();
    const token = searchParams.get('token');

    useEffect(() => {
        if (!token) {
            setTokenValid(false);
            toast.error('Invalid reset token');
            return;
        }

        // Validate token
        const validateToken = async () => {
            try {
                await api.get(`/auth/validate-reset-token?token=${token}`);
                setTokenValid(true);
            } catch {
                setTokenValid(false);
                toast.error('Invalid or expired reset token');
            }
        };

        validateToken();
    }, [token]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!password || !confirmPassword) {
            toast.error('Please fill in all fields');
            return;
        }

        if (password.length < 8) {
            toast.error('Password must be at least 8 characters long');
            return;
        }

        if (password !== confirmPassword) {
            toast.error('Passwords do not match');
            return;
        }

        setIsLoading(true);
        try {
            await api.post('/auth/reset-password', { token, password });
            setIsSuccess(true);
            toast.success('Password reset successfully');

            // Redirect to login after 3 seconds
            setTimeout(() => {
                router.push('/login');
            }, 3000);
        } catch (error: unknown) {
            let message = 'Failed to reset password';
            if (axios.isAxiosError(error)) {
                message = error.response?.data?.message || message;
            }
            toast.error(message);
        } finally {
            setIsLoading(false);
        }
    };

    if (tokenValid === null) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-muted/30">
                <motion.div
                    animate={{ rotate: 360 }}
                    transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                    className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full"
                />
            </div>
        );
    }

    if (tokenValid === false) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-muted/30 p-4">
                <Card className="w-full max-w-md border-destructive/50 shadow-2xl">
                    <CardContent className="pt-6">
                        <div className="text-center space-y-4">
                            <motion.div
                                initial={{ scale: 0 }}
                                animate={{ scale: 1 }}
                                className="w-16 h-16 bg-destructive/10 rounded-full flex items-center justify-center mx-auto"
                            >
                                <AlertCircle className="h-8 w-8 text-destructive" />
                            </motion.div>
                            <div>
                                <h2 className="text-xl font-bold text-destructive mb-2">Invalid Reset Link</h2>
                                <p className="text-muted-foreground mb-4">
                                    This password reset link is invalid or has expired.
                                </p>
                                <div className="space-y-2">
                                    <Link href="/forgot-password">
                                        <Button className="w-full">Request New Reset Link</Button>
                                    </Link>
                                    <Link href="/login">
                                        <Button variant="outline" className="w-full">Back to Login</Button>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </div>
        );
    }

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
                    <p className="text-muted-foreground font-medium">Set your new password</p>
                </div>

                <Card className="border-border/50 shadow-2xl backdrop-blur-sm bg-background/95">
                    <CardHeader className="space-y-1 text-center">
                        <CardTitle className="text-2xl font-bold">
                            {isSuccess ? 'Password Reset!' : 'New Password'}
                        </CardTitle>
                        <CardDescription>
                            {isSuccess
                                ? 'Your password has been successfully reset'
                                : 'Enter your new password below'
                            }
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        {!isSuccess ? (
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div className="space-y-2">
                                    <Label htmlFor="password" className="font-medium">New Password</Label>
                                    <div className="relative">
                                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                            <Lock className="h-4 w-4 text-muted-foreground" />
                                        </div>
                                        <Input
                                            id="password"
                                            type={showPassword ? "text" : "password"}
                                            placeholder="Enter new password"
                                            required
                                            className="h-11 pl-10 pr-10"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                        <button
                                            type="button"
                                            className="absolute inset-y-0 right-0 pr-3 flex items-center"
                                            onClick={() => setShowPassword(!showPassword)}
                                        >
                                            {showPassword ? (
                                                <EyeOff className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                            ) : (
                                                <Eye className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                            )}
                                        </button>
                                    </div>
                                    <p className="text-xs text-muted-foreground">
                                        Must be at least 8 characters long
                                    </p>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="confirmPassword" className="font-medium">Confirm Password</Label>
                                    <div className="relative">
                                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                            <Lock className="h-4 w-4 text-muted-foreground" />
                                        </div>
                                        <Input
                                            id="confirmPassword"
                                            type={showConfirmPassword ? "text" : "password"}
                                            placeholder="Confirm new password"
                                            required
                                            className="h-11 pl-10 pr-10"
                                            value={confirmPassword}
                                            onChange={(e) => setConfirmPassword(e.target.value)}
                                        />
                                        <button
                                            type="button"
                                            className="absolute inset-y-0 right-0 pr-3 flex items-center"
                                            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                        >
                                            {showConfirmPassword ? (
                                                <EyeOff className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                            ) : (
                                                <Eye className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                            )}
                                        </button>
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
                                            Resetting...
                                        </div>
                                    ) : (
                                        'Reset Password'
                                    )}
                                </Button>
                            </form>
                        ) : (
                            <motion.div
                                initial={{ opacity: 0, scale: 0.9 }}
                                animate={{ opacity: 1, scale: 1 }}
                                className="text-center space-y-4"
                            >
                                <motion.div
                                    initial={{ scale: 0 }}
                                    animate={{ scale: 1 }}
                                    transition={{ type: "spring", stiffness: 200 }}
                                    className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto"
                                >
                                    <CheckCircle className="h-8 w-8 text-green-600" />
                                </motion.div>
                                <div>
                                    <p className="text-muted-foreground mb-4">
                                        Redirecting to login page...
                                    </p>
                                    <Link href="/login">
                                        <Button variant="outline" className="w-full">
                                            Go to Login Now
                                        </Button>
                                    </Link>
                                </div>
                            </motion.div>
                        )}
                    </CardContent>
                </Card>

                <Link href="/" className="mt-8 flex items-center justify-center gap-2 text-sm text-muted-foreground hover:text-primary transition-colors font-medium">
                    <LinkIcon className="h-4 w-4" /> Back to home
                </Link>
            </motion.div>
        </div>
    );
}

export default function ResetPasswordPage() {
    return (
        <Suspense fallback={
            <div className="min-h-screen flex items-center justify-center bg-muted/30">
                <motion.div
                    animate={{ rotate: 360 }}
                    transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                    className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full"
                />
            </div>
        }>
            <ResetPasswordContent />
        </Suspense>
    );
}
