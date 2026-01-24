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
import { User, LogOut, LayoutDashboard, Settings, Menu, X, Link as LinkIcon } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '@/store/slices/authSlice';
import { ThemeToggle } from '../common/ThemeToggle';

export default function Navbar() {
    const [isScrolled, setIsScrolled] = useState(false);
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const { isAuthenticated, user } = useSelector((state: any) => state.auth);
    const dispatch = useDispatch();

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

    const navLinks = [
        { label: 'Product', href: '/product' },
        // { label: 'Features', href: '/features' },
        { label: 'Pricing', href: '/pricing' },
        { label: 'Resources', href: '/resources' },
        { label: 'Blogs', href: '/blogs' },
    ];

    return (
        <>
            {/* Fixed full-width header bar */}
            <header className="fixed top-0 left-0 right-0 z-50 px-4 md:px-8 pt-8">
                <nav className={`mx-auto max-w-7xl bg-background/95 backdrop-blur-md rounded-2xl shadow-lg border border-border/50 transition-all duration-300 ${isScrolled ? 'shadow-xl' : ''}`}>
                    <div className="flex items-center justify-between h-14 px-6">
                        {/* Logo */}
                        <Link href="/" className="flex items-center gap-2.5">
                            <div className="w-7 h-7 rounded-lg bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                                <LinkIcon className="h-3.5 w-3.5 text-white" />
                            </div>
                            <span className="text-base font-semibold text-foreground">Zaplink</span>
                        </Link>

                        {/* Desktop Nav - Center */}
                        <div className="hidden md:flex items-center gap-1">
                            {navLinks.map((link) => (
                                <Link
                                    key={link.label}
                                    href={link.href}
                                    className="px-4 py-2 text-sm font-medium text-muted-foreground hover:text-foreground transition-colors"
                                >
                                    {link.label}
                                </Link>
                            ))}
                        </div>

                        {/* Right side - Auth buttons */}
                        <div className="hidden md:flex items-center gap-2">
                            {isAuthenticated ? (
                                <>
                                    <Link href="/dashboard">
                                        <Button variant="ghost" size="sm" className="text-muted-foreground hover:text-foreground text-sm font-medium">
                                            Dashboard
                                        </Button>
                                    </Link>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger asChild>
                                            <Button variant="ghost" size="sm" className="gap-2">
                                                <div className="w-6 h-6 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                                                    <User className="h-3 w-3 text-white" />
                                                </div>
                                                <span className="hidden lg:inline text-foreground text-sm">{user?.username}</span>
                                            </Button>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent align="end" className="w-56">
                                            <DropdownMenuLabel>My Account</DropdownMenuLabel>
                                            <DropdownMenuSeparator />
                                            <DropdownMenuItem asChild>
                                                <Link href="/dashboard">
                                                    <LayoutDashboard className="mr-2 h-4 w-4" />
                                                    Dashboard
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuItem asChild>
                                                <Link href="/dashboard/profile">
                                                    <User className="mr-2 h-4 w-4" />
                                                    Profile
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuItem asChild>
                                                <Link href="/dashboard/settings">
                                                    <Settings className="mr-2 h-4 w-4" />
                                                    Settings
                                                </Link>
                                            </DropdownMenuItem>
                                            <DropdownMenuSeparator />
                                            <DropdownMenuItem onClick={handleLogout} className="text-red-600">
                                                <LogOut className="mr-2 h-4 w-4" />
                                                Logout
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </>
                            ) : (
                                <>
                                    <Link href="/login">
                                        <Button variant="ghost" size="sm" className="text-muted-foreground hover:text-foreground text-sm font-medium">
                                            Sign in
                                        </Button>
                                    </Link>
                                    <Link href="/signup">
                                        <Button variant="outline" size="sm" className="rounded-full border-border text-foreground hover:bg-muted text-sm font-medium px-4">
                                            Sign up
                                        </Button>
                                    </Link>
                                </>
                            )}
                            <div className="ml-1 pl-2 border-l border-border">
                                <ThemeToggle />
                            </div>
                        </div>

                        {/* Mobile Toggle */}
                        <button
                            className="md:hidden p-2 rounded-lg hover:bg-muted transition-colors"
                            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                        >
                            {mobileMenuOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
                        </button>
                    </div>
                </nav>
            </header>

            {/* Mobile Menu */}
            <AnimatePresence>
                {mobileMenuOpen && (
                    <motion.div
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -10 }}
                        className="md:hidden fixed top-25 left-4 right-4 z-40 bg-white dark:bg-slate-900 rounded-2xl shadow-xl border border-slate-200 dark:border-slate-700"
                    >
                        <div className="p-4 flex flex-col gap-2">
                            {navLinks.map((link) => (
                                <Link
                                    key={link.label}
                                    href={link.href}
                                    className="px-4 py-3 text-sm font-medium text-muted-foreground hover:bg-muted rounded-lg"
                                    onClick={() => setMobileMenuOpen(false)}
                                >
                                    {link.label}
                                </Link>
                            ))}
                            <div className="border-t border-border pt-4 mt-2 flex flex-col gap-2">
                                {isAuthenticated ? (
                                    <>
                                        <Link href="/dashboard" onClick={() => setMobileMenuOpen(false)}>
                                            <Button variant="outline" className="w-full justify-start">
                                                <LayoutDashboard className="mr-2 h-4 w-4" /> Dashboard
                                            </Button>
                                        </Link>
                                        <Button variant="ghost" onClick={() => { handleLogout(); setMobileMenuOpen(false); }} className="w-full justify-start text-red-600">
                                            <LogOut className="mr-2 h-4 w-4" /> Logout
                                        </Button>
                                    </>
                                ) : (
                                    <>
                                        <Link href="/login" onClick={() => setMobileMenuOpen(false)}>
                                            <Button variant="outline" className="w-full">Sign in</Button>
                                        </Link>
                                        <Link href="/signup" onClick={() => setMobileMenuOpen(false)}>
                                            <Button className="w-full bg-primary hover:bg-primary/90 text-primary-foreground">Sign up</Button>
                                        </Link>
                                    </>
                                )}
                            </div>
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>
        </>
    );
}
