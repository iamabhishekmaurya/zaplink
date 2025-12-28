'use client';

import Link from 'next/link';
import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from '@/components/ui/dropdown-menu';
import { User, LogOut, LayoutDashboard, Settings, Menu, X, Link as LinkIcon, Sparkles } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '@/store';
import { logout } from '@/store/slices/authSlice';
import { ThemeToggle } from '@/components/common/ThemeToggle';
import { useTheme } from 'next-themes';

export default function Navbar() {
    const [isScrolled, setIsScrolled] = useState(false);
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const { isAuthenticated, user } = useSelector((state: RootState) => state.auth);
    const dispatch = useDispatch();
    const { theme } = useTheme();

    useEffect(() => {
        const handleScroll = () => {
            setIsScrolled(window.scrollY > 20);
        };
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    const handleLogout = () => {
        dispatch(logout());
    };

    return (
        <>
            <nav className={`fixed top-0 left-0 right-0 z-50 transition-all duration-500 ${isScrolled
                ? 'glass-effect border-b border-white/10 shadow-lg'
                : 'bg-transparent py-6'
                }`}>
                <div className="container mx-auto px-4 md:px-6">
                    <div className="flex items-center justify-between">
                        {/* Logo - Enhanced */}
                        <Link href="/" className="flex items-center gap-3 group">
                            <motion.div
                                className="bg-gradient-to-r from-primary to-accent p-2 rounded-xl shadow-lg group-hover:shadow-xl transition-all duration-300"
                                whileHover={{ rotate: 12, scale: 1.05 }}
                                whileTap={{ scale: 0.95 }}
                            >
                                <LinkIcon className="h-6 w-6 text-white" />
                            </motion.div>
                            <div className="flex flex-col">
                                <span className="text-xl font-bold tracking-tight text-foreground">Zaplink</span>
                                <span className="text-xs text-muted-foreground">Transform Links</span>
                            </div>
                        </Link>

                        {/* Desktop Nav - Modern Design */}
                        <div className="hidden md:flex items-center gap-8">
                            <div className="flex items-center gap-6">
                                <Link href="/features" className="text-sm font-medium hover:text-primary transition-colors relative group">
                                    Features
                                    <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-gradient-to-r from-primary to-accent group-hover:w-full transition-all duration-300" />
                                </Link>
                                <Link href="/pricing" className="text-sm font-medium hover:text-primary transition-colors relative group">
                                    Pricing
                                    <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-gradient-to-r from-primary to-accent group-hover:w-full transition-all duration-300" />
                                </Link>
                                <Link href="/resources" className="text-sm font-medium hover:text-primary transition-colors relative group">
                                    Resources
                                    <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-gradient-to-r from-primary to-accent group-hover:w-full transition-all duration-300" />
                                </Link>
                            </div>

                            {isAuthenticated ? (
                                <div className="flex items-center gap-4">
                                    <Link href="/dashboard">
                                        <Button variant="ghost" size="sm" className="gap-2 hover:bg-primary/10 hover:text-primary">
                                            <LayoutDashboard className="h-4 w-4" />
                                            Dashboard
                                        </Button>
                                    </Link>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger asChild>
                                            <Button variant="outline" size="sm" className={`rounded-full gap-2 px-4 hover:bg-white/10`}>
                                                <div className="w-6 h-6 rounded-full bg-gradient-to-r from-primary to-accent flex items-center justify-center">
                                                    <User className="h-3 w-3 text-white" />
                                                </div>
                                                <span className={`hidden lg:inline font-medium ${theme === 'dark' ? 'text-white' : 'text-gray-900'}`}>{user?.username}</span>
                                            </Button>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent align="end" className="w-56 glass-card border-0 shadow-2xl">
                                            <DropdownMenuLabel className="text-foreground">My Account</DropdownMenuLabel>
                                            <DropdownMenuSeparator className="bg-white/10" />
                                            <DropdownMenuItem asChild className="hover:bg-white/10 cursor-pointer">
                                                <Link href="/dashboard" className="cursor-pointer">
                                                    <LayoutDashboard className="mr-2 h-4 w-4" />
                                                    Dashboard
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuItem asChild className="hover:bg-white/10 cursor-pointer">
                                                <Link href="/dashboard/links" className="cursor-pointer">
                                                    <LinkIcon className="mr-2 h-4 w-4" />
                                                    Manage Links
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuItem asChild className="hover:bg-white/10 cursor-pointer">
                                                <Link href="/dashboard/profile" className="cursor-pointer">
                                                    <User className="mr-2 h-4 w-4" />
                                                    Profile
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuItem asChild className="hover:bg-white/10 cursor-pointer">
                                                <Link href="/dashboard/settings" className="cursor-pointer">
                                                    <Settings className="mr-2 h-4 w-4" />
                                                    Settings
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuSeparator className="bg-white/10" />
                                            <DropdownMenuItem onClick={handleLogout} className="text-destructive hover:text-destructive/80 hover:bg-destructive/10 cursor-pointer">
                                                <LogOut className="mr-2 h-4 w-4" />
                                                Logout
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </div>
                            ) : (
                                <div className="flex items-center gap-4">
                                    <Link href="/login">
                                        <Button variant="ghost" size="sm" className="hover:bg-primary/10 hover:text-primary font-medium">
                                            Login
                                        </Button>
                                    </Link>
                                    <Link href="/signup">
                                        <Button size="sm" className="rounded-full px-6 gradient-primary hover:shadow-lg hover:scale-105 transition-all duration-300 font-bold text-white border-0">
                                            <Sparkles className="w-4 h-4 mr-2" />
                                            Get Started
                                        </Button>
                                    </Link>
                                </div>
                            )}
                            <div className="border-l border-white/20 pl-4 flex items-center h-8">
                                <ThemeToggle />
                            </div>
                        </div>

                        {/* Mobile Toggle - Enhanced */}
                        <motion.button
                            className="md:hidden p-2 rounded-lg hover:bg-white/10 transition-colors"
                            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                            whileTap={{ scale: 0.95 }}
                        >
                            {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
                        </motion.button>
                    </div>
                </div>
            </nav>

            {/* Mobile Menu - Modern Glass */}
            <AnimatePresence>
                {mobileMenuOpen && (
                    <motion.div
                        initial={{ opacity: 0, height: 0, y: -20 }}
                        animate={{ opacity: 1, height: 'auto', y: 0 }}
                        exit={{ opacity: 0, height: 0, y: -20 }}
                        transition={{ duration: 0.3 }}
                        className="md:hidden glass-card border-t border-white/10 mt-16"
                    >
                        <div className="container mx-auto px-4 py-8 flex flex-col gap-6">
                            <div className="flex flex-col gap-4">
                                <Link href="/features" className="text-lg font-medium hover:text-primary transition-colors py-2" onClick={() => setMobileMenuOpen(false)}>
                                    Features
                                </Link>
                                <Link href="/pricing" className="text-lg font-medium hover:text-primary transition-colors py-2" onClick={() => setMobileMenuOpen(false)}>
                                    Pricing
                                </Link>
                                <Link href="/resources" className="text-lg font-medium hover:text-primary transition-colors py-2" onClick={() => setMobileMenuOpen(false)}>
                                    Resources
                                </Link>
                            </div>

                            <div className="border-t border-white/10 pt-6" />

                            {isAuthenticated ? (
                                <div className="flex flex-col gap-4">
                                    <Link href="/dashboard" className="text-lg font-medium flex items-center gap-3 py-2 hover:text-primary transition-colors" onClick={() => setMobileMenuOpen(false)}>
                                        <LayoutDashboard className="h-5 w-5" /> Dashboard
                                    </Link>
                                    <Button onClick={() => { handleLogout(); setMobileMenuOpen(false); }} variant="outline" className="justify-start gap-3 h-12 glass-card border-white/20 hover:bg-white/10">
                                        <LogOut className="h-5 w-5" /> Logout
                                    </Button>
                                </div>
                            ) : (
                                <div className="flex flex-col gap-4">
                                    <Link href="/login" onClick={() => setMobileMenuOpen(false)}>
                                        <Button variant="outline" className="w-full h-12 glass-card border-white/20 hover:bg-white/10 font-medium">
                                            Login
                                        </Button>
                                    </Link>
                                    <Link href="/signup" onClick={() => setMobileMenuOpen(false)}>
                                        <Button className="w-full h-12 gradient-primary text-white font-bold hover:shadow-lg transition-all">
                                            <Sparkles className="w-4 h-4 mr-2" />
                                            Get Started
                                        </Button>
                                    </Link>
                                </div>
                            )}
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>
        </>
    );
}
