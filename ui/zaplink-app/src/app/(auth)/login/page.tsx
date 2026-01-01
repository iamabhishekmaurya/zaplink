'use client';

import { BorderBeam } from "@/components/ui/border-beam";
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useAuth } from '@/hooks/useAuth';
import { motion } from 'framer-motion';
import { ArrowLeft, Eye, EyeOff, Link as LinkIcon, Lock, Mail } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { FaGithub, FaGoogle } from "react-icons/fa";

export default function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const { login, isLoading, isAuthenticated, isInitialized } = useAuth();
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await login({ email, password, rememberMe });
    };

    useEffect(() => {
        if (isInitialized && isAuthenticated) {
            router.push('/dashboard');
        }
    }, [isInitialized, isAuthenticated, router]);

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
                            <LinkIcon className="h-5 w-5 text-primary-foreground" />
                        </div>
                        <span className="text-2xl font-bold font-display tracking-tight">Zaplink</span>
                    </Link>
                    <p className="text-muted-foreground font-medium">Transform your links</p>
                </div>

                <Card className="border-border/50 shadow-2xl backdrop-blur-sm bg-background/95 relative overflow-hidden">
                    <CardHeader className="space-y-1 text-center">
                        <CardTitle className="text-2xl font-bold font-display">Welcome back</CardTitle>
                        <CardDescription>Enter your credentials to access your account</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="space-y-2">
                                <Label htmlFor="email" className="font-medium">Email</Label>
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
                            <div className="space-y-2">
                                <div className="flex items-center justify-between">
                                    <Label htmlFor="password" className="font-medium">Password</Label>
                                    <Link href="/forgot-password" className="text-xs text-primary hover:underline font-medium">Forgot password?</Link>
                                </div>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <Lock className="h-4 w-4 text-muted-foreground" />
                                    </div>
                                    <Input
                                        id="password"
                                        type={showPassword ? "text" : "password"}
                                        placeholder="**********"
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
                                            <EyeOff className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                                        ) : (
                                            <Eye className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                                        )}
                                    </button>
                                </div>
                            </div>
                            <div className="flex items-center space-x-2">
                                <Checkbox
                                    id="remember"
                                    checked={rememberMe}
                                    onCheckedChange={(checked) => setRememberMe(checked as boolean)}
                                />
                                <Label htmlFor="remember" className="text-sm font-normal text-muted-foreground leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                    Stay logged in for 30 days
                                </Label>
                            </div>
                            <Button type="submit" className="w-full h-11 text-base font-semibold" disabled={isLoading}>
                                {isLoading ? (
                                    <div className="flex items-center gap-2">
                                        <div className="h-4 w-4 border-2 border-primary-foreground border-t-transparent animate-spin rounded-full" />
                                        Signing in...
                                    </div>
                                ) : (
                                    'Sign In'
                                )}
                            </Button>
                        </form>

                        <div className="relative">
                            <div className="absolute inset-0 flex items-center">
                                <span className="w-full border-t" />
                            </div>
                            <div className="relative flex justify-center text-xs uppercase">
                                <span className="bg-background px-2 text-muted-foreground">Or continue with</span>
                            </div>
                        </div>

                        <div className="grid grid-cols-2 gap-3">
                            <Button variant="outline" className="h-11 gap-2 hover:bg-primary/15 dark:hover:bg-primary/20 hover:text-foreground dark:hover:text-foreground font-medium border-1 hover:border-primary/30">
                                <FaGoogle className="h-4 w-4" /> Google
                            </Button>
                            <Button variant="outline" className="h-11 gap-2 hover:bg-primary/15 dark:hover:bg-primary/20 hover:text-foreground dark:hover:text-foreground font-medium border-1 hover:border-primary/30">
                                <FaGithub className="h-4 w-4" /> GitHub
                            </Button>
                        </div>
                        <div className="relative">
                            <div className="absolute inset-0 flex items-center">
                                <span className="w-full border-t" />
                            </div>
                        </div>
                        <p className="pt-6 text-center text-sm text-muted-foreground">
                            Don't have an account?{" "}
                            <Link href="/signup" className="text-primary hover:underline font-bold">Sign up for free</Link>
                        </p>
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
