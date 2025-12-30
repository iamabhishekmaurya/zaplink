'use client';

import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '@/store';
import { usePathname } from 'next/navigation';
import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetDescription, SheetTrigger } from '@/components/ui/sheet';
import {
  LayoutDashboard,
  Link as LinkIcon,
  User,
  Settings,
  Menu,
  LogOut,
  BarChart3,
  Users,
  Shield,
  Palette
} from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { toast } from 'sonner';
import { Copy, ChevronsUpDown } from 'lucide-react';
import { ThemeToggle } from '@/components/common/ThemeToggle';

const navigation = [
  { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
  { name: 'Links', href: '/dashboard/links', icon: LinkIcon },
  { name: 'Analytics', href: '/dashboard/analytics', icon: BarChart3 },
];

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { isAuthenticated, isInitialized, user } = useSelector((state: RootState) => state.auth);
  const pathname = usePathname();
  const router = useRouter();

  const handleLogout = () => {
    // Dispatch logout action here
    router.push('/');
  };

  const copyProfileLink = () => {
    const profileUrl = `${window.location.origin}/u/${user?.username}`;
    navigator.clipboard.writeText(profileUrl);
    toast.success('Profile link copied to clipboard!');
  };

  useEffect(() => {
    if (isInitialized && !isAuthenticated) {
      router.push('/');
    }
  }, [isInitialized, isAuthenticated, router]);

  if (!isInitialized || !isAuthenticated) {
    return null;
  }

  return (
    <div className="flex h-screen bg-background">
      {/* Desktop Sidebar */}
      <div className="hidden lg:flex lg:w-64 lg:flex-col lg:fixed lg:inset-y-0">
        <div className="flex flex-col flex-grow bg-card border-r border-border pt-5 pb-4 overflow-y-auto">
          <div className="flex items-center justify-between flex-shrink-0 px-4">
            <Link href="/dashboard" className="text-2xl font-bold font-display text-primary tracking-tight">
              Zaplink
            </Link>
            <ThemeToggle />
          </div>
          <nav className="mt-8 flex-1 px-2 space-y-1">
            {navigation.map((item) => {
              const isActive = pathname === item.href;
              return (
                <Link
                  key={item.name}
                  href={item.href}
                  className={cn(
                    'group flex items-center px-2 py-2 text-sm font-bold font-display rounded-md transition-colors',
                    isActive
                      ? 'bg-primary text-primary-foreground'
                      : 'text-muted-foreground hover:text-foreground hover:bg-accent'
                  )}
                >
                  <item.icon className="mr-3 h-5 w-5" />
                  {item.name}
                </Link>
              );
            })}
          </nav>
          <div className="flex-shrink-0 flex border-t border-border p-4">
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  variant="ghost"
                  className="w-full flex items-center gap-3 px-2 py-6 hover:bg-accent transition-all group"
                >
                  <Avatar className="h-9 w-9 border-2 border-primary/20 group-hover:border-primary/50 transition-colors">
                    <AvatarImage src={`https://avatar.vercel.sh/${user?.username}`} />
                    <AvatarFallback className="bg-primary/10 text-primary font-bold">
                      {user?.username?.charAt(0).toUpperCase()}
                    </AvatarFallback>
                  </Avatar>
                  <div className="flex flex-col items-start flex-1 overflow-hidden">
                    <span className="text-sm font-bold font-display text-foreground truncate w-full text-left">
                      {user?.username}
                    </span>
                    <span className="text-xs text-muted-foreground truncate w-full text-left">
                      {user?.email}
                    </span>
                  </div>
                  <ChevronsUpDown className="h-4 w-4 text-muted-foreground group-hover:text-foreground transition-colors" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent className="w-56 mb-2" side="right" align="end">
                <DropdownMenuLabel className="font-bold font-display">My Account</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem asChild>
                  <Link href="/dashboard/profile" className="flex items-center gap-2 cursor-pointer font-medium">
                    <User className="h-4 w-4" />
                    <span>View Profile</span>
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem asChild>
                  <Link href="/dashboard/settings" className="flex items-center gap-2 cursor-pointer font-medium">
                    <Settings className="h-4 w-4" />
                    <span>Settings</span>
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem onClick={copyProfileLink} className="flex items-center gap-2 cursor-pointer font-medium">
                  <Copy className="h-4 w-4" />
                  <span>Copy Profile Link</span>
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={handleLogout} className="flex items-center gap-2 cursor-pointer font-bold text-destructive focus:text-destructive">
                  <LogOut className="h-4 w-4" />
                  <span>Logout</span>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </div>

      {/* Mobile Sidebar */}
      <Sheet open={sidebarOpen} onOpenChange={setSidebarOpen}>
        <SheetTrigger asChild>
          <Button
            variant="ghost"
            size="icon"
            className="lg:hidden fixed top-4 left-4 z-40"
          >
            <Menu className="h-5 w-5" />
          </Button>
        </SheetTrigger>
        <SheetContent side="left" className="p-0 w-64">
          <SheetHeader className="sr-only">
            <SheetTitle>Navigation Menu</SheetTitle>
            <SheetDescription>
              Access your dashboard, links, analytics, and settings.
            </SheetDescription>
          </SheetHeader>
          <div className="flex flex-col h-full bg-card">
            <div className="flex items-center justify-between flex-shrink-0 px-4 pt-5 pb-4">
              <Link href="/dashboard" className="text-2xl font-bold font-display text-primary tracking-tight">
                Zaplink
              </Link>
              <ThemeToggle />
            </div>
            <nav className="flex-1 px-2 space-y-1">
              {navigation.map((item) => {
                const isActive = pathname === item.href;
                return (
                  <Link
                    key={item.name}
                    href={item.href}
                    className={cn(
                      'group flex items-center px-2 py-2 text-sm font-bold font-display rounded-md transition-colors',
                      isActive
                        ? 'bg-primary text-primary-foreground'
                        : 'text-muted-foreground hover:text-foreground hover:bg-accent'
                    )}
                    onClick={() => setSidebarOpen(false)}
                  >
                    <item.icon className="mr-3 h-5 w-5" />
                    {item.name}
                  </Link>
                );
              })}
            </nav>
            <div className="flex-shrink-0 flex border-t border-border p-4">
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button
                    variant="ghost"
                    className="w-full flex items-center gap-3 px-2 py-6 hover:bg-accent transition-all group"
                  >
                    <Avatar className="h-9 w-9 border-2 border-primary/20">
                      <AvatarImage src={`https://avatar.vercel.sh/${user?.username}`} />
                      <AvatarFallback className="bg-primary/10 text-primary font-bold">
                        {user?.username?.charAt(0).toUpperCase()}
                      </AvatarFallback>
                    </Avatar>
                    <div className="flex flex-col items-start flex-1 overflow-hidden">
                      <span className="text-sm font-bold font-display text-foreground truncate w-full text-left">
                        {user?.username}
                      </span>
                      <span className="text-xs text-muted-foreground truncate w-full text-left">
                        {user?.email}
                      </span>
                    </div>
                    <ChevronsUpDown className="h-4 w-4 text-muted-foreground" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="w-[calc(100vw-2rem)] md:w-56 mb-2" side="top" align="center">
                  <DropdownMenuLabel className="font-bold font-display">My Account</DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem asChild onClick={() => setSidebarOpen(false)}>
                    <Link href="/dashboard/profile" className="flex items-center gap-2 cursor-pointer font-medium">
                      <User className="h-4 w-4" />
                      <span>View Profile</span>
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild onClick={() => setSidebarOpen(false)}>
                    <Link href="/dashboard/settings" className="flex items-center gap-2 cursor-pointer font-medium">
                      <Settings className="h-4 w-4" />
                      <span>Settings</span>
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem onClick={copyProfileLink} className="flex items-center gap-2 cursor-pointer font-medium">
                    <Copy className="h-4 w-4" />
                    <span>Copy Profile Link</span>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem
                    onClick={() => {
                      setSidebarOpen(false);
                      handleLogout();
                    }}
                    className="flex items-center gap-2 cursor-pointer font-bold text-destructive focus:text-destructive"
                  >
                    <LogOut className="h-4 w-4" />
                    <span>Logout</span>
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
          </div>
        </SheetContent>
      </Sheet>

      {/* Main Content */}
      <div className="lg:pl-64 flex flex-col flex-1">
        <main className="flex-1 overflow-y-auto">
          <div className="py-6">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
              {children}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
