'use client';

import { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle, CardFooter } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
    Plus,
    Copy,
    MoreVertical,
    ExternalLink,
    Search,
    Filter,
    Download,
    Trash2,
    Calendar,
    MousePointer2,
    ArrowUpDown,
    Check
} from 'lucide-react';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
    DropdownMenuSeparator
} from '@/components/ui/dropdown-menu';
import { motion, AnimatePresence } from 'framer-motion';
import { toast } from 'sonner';
import { useLinks } from '@/hooks/useLinks';
import { CreateLinkModal } from '@/components/links/CreateLinkModal';

export default function LinksPage() {
    const { links, fetchUserLinks, deleteLink, isLoading, error } = useLinks();
    const [searchQuery, setSearchQuery] = useState('');
    const [filterStatus, setFilterStatus] = useState<'all' | 'active' | 'archived'>('all');
    const [sortBy, setSortBy] = useState<'date' | 'clicks'>('date');
    const [selectedLinks, setSelectedLinks] = useState<string[]>([]);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

    useEffect(() => {
        fetchUserLinks();
    }, []);

    const filteredLinks = (links || []).filter(link => {
        if (!link) return false;

        const matchesSearch =
            (link.originalUrl || '').toLowerCase().includes(searchQuery.toLowerCase()) ||
            (link.shortUrl || '').toLowerCase().includes(searchQuery.toLowerCase());

        // Add more filter logic here if needed
        return matchesSearch;
    });

    const copyLink = (link: string) => {
        if (!link) return;
        navigator.clipboard.writeText(link);
        toast.success('Link copied to clipboard');
    };

    const toggleSelect = (id: string) => {
        if (!id) return;
        setSelectedLinks(prev =>
            prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
        );
    };

    return (
        <div className="container mx-auto px-4 py-10">
            <div className="flex flex-col gap-8">
                {/* Header Section */}
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                    <div>
                        <h1 className="text-3xl font-bold font-display tracking-tight">Manage Links</h1>
                        <p className="text-muted-foreground">Search, filter, and organize all your short links.</p>
                    </div>
                    <div className="flex items-center gap-3">
                        <Button variant="outline" className="gap-2 h-11 px-4 border-dashed border-border/60 hover:border-primary/50">
                            <Download className="h-4 w-4" /> Export
                        </Button>
                        <Button
                            className="gap-2 h-11 px-6 font-semibold font-display shadow-lg shadow-primary/20 transition-all active:scale-[0.98]"
                            onClick={() => setIsCreateModalOpen(true)}
                        >
                            <Plus className="h-5 w-5" /> Create Link
                        </Button>
                    </div>
                </div>
                <CreateLinkModal open={isCreateModalOpen} onOpenChange={setIsCreateModalOpen} />

                {/* Error Display */}
                {error && !isLoading && (
                    <motion.div
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="bg-destructive/10 border border-destructive/20 text-destructive p-4 rounded-xl flex items-center gap-3"
                    >
                        <div className="h-8 w-8 rounded-full bg-destructive/20 flex items-center justify-center shrink-0">
                            <Trash2 className="h-4 w-4" />
                        </div>
                        <p className="font-medium">{error}</p>
                        <Button variant="ghost" size="sm" onClick={() => fetchUserLinks()} className="ml-auto hover:bg-destructive/20">
                            Retry
                        </Button>
                    </motion.div>
                )}

                {/* Toolbar Section */}
                <Card className="border-border/40 shadow-sm overflow-hidden bg-background/50 backdrop-blur-sm">
                    <CardContent className="p-4 flex flex-col lg:flex-row gap-4">
                        <div className="relative flex-grow">
                            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                            <Input
                                placeholder="Search by URL or title..."
                                className="pl-10 h-11 bg-background/80"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                            />
                        </div>
                        <div className="flex items-center gap-3">
                            <DropdownMenu>
                                <DropdownMenuTrigger asChild>
                                    <Button variant="outline" className="h-11 gap-2 bg-background/80 border-border/40 hover:border-primary/50">
                                        <Filter className="h-4 w-4" />
                                        Status: <span className="font-semibold text-primary capitalize">{filterStatus}</span>
                                    </Button>
                                </DropdownMenuTrigger>
                                <DropdownMenuContent align="end" className="w-48 glass-card border-border/40">
                                    <DropdownMenuItem onClick={() => setFilterStatus('all')}>All Links</DropdownMenuItem>
                                    <DropdownMenuItem onClick={() => setFilterStatus('active')}>Active</DropdownMenuItem>
                                    <DropdownMenuItem onClick={() => setFilterStatus('archived')}>Archived</DropdownMenuItem>
                                </DropdownMenuContent>
                            </DropdownMenu>

                            <DropdownMenu>
                                <DropdownMenuTrigger asChild>
                                    <Button variant="outline" className="h-11 gap-2 bg-background/80 border-border/40 hover:border-primary/50">
                                        <ArrowUpDown className="h-4 w-4" />
                                        Sort: <span className="font-semibold text-primary capitalize">{sortBy}</span>
                                    </Button>
                                </DropdownMenuTrigger>
                                <DropdownMenuContent align="end" className="w-48 glass-card border-border/40">
                                    <DropdownMenuItem onClick={() => setSortBy('date')}>Date Created</DropdownMenuItem>
                                    <DropdownMenuItem onClick={() => setSortBy('clicks')}>Click Count</DropdownMenuItem>
                                </DropdownMenuContent>
                            </DropdownMenu>
                        </div>
                    </CardContent>
                </Card>

                {/* Link Table Section */}
                <Card className="border-border/40 shadow-sm overflow-hidden bg-background/50 backdrop-blur-sm relative min-h-[400px]">
                    <CardHeader className="p-6 border-b border-border/40 bg-muted/5">
                        <div className="flex items-center justify-between">
                            <div className="flex items-center gap-4">
                                <div className="h-10 w-10 rounded-xl bg-primary/10 flex items-center justify-center">
                                    <MousePointer2 className="h-5 w-5 text-primary" />
                                </div>
                                <div>
                                    <CardTitle className="text-xl font-bold font-display">All Links</CardTitle>
                                    <CardDescription>{filteredLinks.length} total links found</CardDescription>
                                </div>
                            </div>
                            {selectedLinks.length > 0 && (
                                <motion.div
                                    initial={{ opacity: 0, x: 20 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    className="flex items-center gap-2"
                                >
                                    <span className="text-sm font-medium text-muted-foreground mr-2">
                                        {selectedLinks.length} selected
                                    </span>
                                    <Button variant="destructive" size="sm" className="h-9 gap-2">
                                        <Trash2 className="h-4 w-4" /> Delete selected
                                    </Button>
                                    <Button variant="outline" size="sm" className="h-9" onClick={() => setSelectedLinks([])}>
                                        Cancel
                                    </Button>
                                </motion.div>
                            )}
                        </div>
                    </CardHeader>
                    <CardContent className="p-0">
                        {isLoading ? (
                            <div className="flex items-center justify-center h-[300px]">
                                <div className="flex flex-col items-center gap-4">
                                    <div className="h-10 w-10 border-4 border-primary/20 border-t-primary rounded-full animate-spin" />
                                    <p className="text-muted-foreground font-medium">Loading your links...</p>
                                </div>
                            </div>
                        ) : filteredLinks.length === 0 ? (
                            <div className="flex flex-col items-center justify-center h-[300px] text-center p-8">
                                <div className="h-20 w-20 bg-muted rounded-full flex items-center justify-center mb-6">
                                    <Search className="h-10 w-10 text-muted-foreground" />
                                </div>
                                <h3 className="text-xl font-bold font-display mb-2">No links found</h3>
                                <p className="text-muted-foreground max-w-sm">
                                    {searchQuery
                                        ? `We couldn't find any links matching "${searchQuery}"`
                                        : "You haven't created any links yet. Start shortening your first URL!"}
                                </p>
                                {searchQuery && (
                                    <Button variant="link" onClick={() => setSearchQuery('')} className="mt-2">
                                        Clear search
                                    </Button>
                                )}
                            </div>
                        ) : (
                            <div className="overflow-x-auto">
                                <table className="w-full">
                                    <thead className="bg-muted/30 border-b border-border/40">
                                        <tr className="text-left text-xs font-bold font-display text-muted-foreground uppercase tracking-wider">
                                            <th className="px-6 py-4 w-[50px]">
                                                <div className="flex items-center">
                                                    <div
                                                        className={`h-4 w-4 rounded border-2 border-muted-foreground/30 flex items-center justify-center cursor-pointer transition-colors ${selectedLinks.length === filteredLinks.length ? 'bg-primary border-primary' : ''}`}
                                                        onClick={() => {
                                                            if (selectedLinks.length === filteredLinks.length) setSelectedLinks([]);
                                                            else setSelectedLinks(filteredLinks.map(l => l.id));
                                                        }}
                                                    >
                                                        {selectedLinks.length === filteredLinks.length && <Check className="h-3 w-3 text-white" />}
                                                    </div>
                                                </div>
                                            </th>
                                            <th className="px-6 py-4">Link Details</th>
                                            <th className="px-6 py-4">Status</th>
                                            <th className="px-6 py-4">Performance</th>
                                            <th className="px-6 py-4">Created At</th>
                                            <th className="px-6 py-4 text-right">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y divide-border/40">
                                        {filteredLinks.map((link) => (
                                            <tr key={link.id} className={`group hover:bg-muted/20 transition-all duration-200 ${selectedLinks.includes(link.id) ? 'bg-primary/5' : ''}`}>
                                                <td className="px-6 py-4">
                                                    <div
                                                        className={`h-4 w-4 rounded border-2 flex items-center justify-center cursor-pointer transition-colors ${selectedLinks.includes(link.id) ? 'bg-primary border-primary' : 'border-muted-foreground/30'}`}
                                                        onClick={() => toggleSelect(link.id)}
                                                    >
                                                        {selectedLinks.includes(link.id) && <Check className="h-3 w-3 text-white" />}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="flex flex-col gap-1 max-w-[350px]">
                                                        <div className="flex items-center gap-2">
                                                            <img
                                                                src={`https://www.google.com/s2/favicons?domain=${new URL(link.originalUrl).hostname}&sz=32`}
                                                                alt=""
                                                                className="h-4 w-4 rounded flex-shrink-0"
                                                                onError={(e) => {
                                                                    e.currentTarget.src = 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4M12 8h.01"/></svg>';
                                                                }}
                                                            />
                                                            <span className="font-bold font-display text-primary truncate">
                                                                {link.shortUrl}
                                                            </span>
                                                            <Button
                                                                variant="ghost"
                                                                size="icon"
                                                                className="h-7 w-7 rounded-full opacity-0 group-hover:opacity-100 transition-all hover:bg-primary/10 hover:text-primary"
                                                                onClick={() => copyLink(link.shortUrl)}
                                                            >
                                                                <Copy className="h-3.5 w-3.5" />
                                                            </Button>
                                                        </div>
                                                        <div className="flex items-center gap-2 text-xs text-muted-foreground ml-6">
                                                            <span className="truncate">{link.originalUrl}</span>
                                                            <a href={link.originalUrl} target="_blank" rel="noopener noreferrer" className="hover:text-primary">
                                                                <ExternalLink className="h-3 w-3" />
                                                            </a>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-bold bg-green-500/10 text-green-500 border border-green-500/20">
                                                        <div className="h-1.5 w-1.5 rounded-full bg-green-500" />
                                                        Active
                                                    </span>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="flex flex-col gap-1">
                                                        <div className="flex items-center gap-2">
                                                            <MousePointer2 className="h-3.5 w-3.5 text-primary" />
                                                            <span className="text-sm font-bold">{link.clickCount.toLocaleString()}</span>
                                                            <span className="text-xs text-muted-foreground">clicks</span>
                                                        </div>
                                                        <div className="w-24 h-1.5 bg-muted rounded-full overflow-hidden">
                                                            <div className="h-full bg-primary" style={{ width: '65%' }} />
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                                        <Calendar className="h-3.5 w-3.5" />
                                                        {new Date(link.createdAt).toLocaleDateString()}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 text-right">
                                                    <DropdownMenu>
                                                        <DropdownMenuTrigger asChild>
                                                            <Button variant="ghost" size="icon" className="h-9 w-9 rounded-full hover:bg-muted group-hover:bg-background shadow-sm transition-all border border-transparent hover:border-border/40">
                                                                <MoreVertical className="h-4 w-4" />
                                                            </Button>
                                                        </DropdownMenuTrigger>
                                                        <DropdownMenuContent align="end" className="w-48 glass-card border-border/40 shadow-2xl">
                                                            <DropdownMenuItem className="gap-2 cursor-pointer">
                                                                <MousePointer2 className="h-4 w-4" /> View Analytics
                                                            </DropdownMenuItem>
                                                            <DropdownMenuItem className="gap-2 cursor-pointer">
                                                                <Copy className="h-4 w-4" /> Edit Link
                                                            </DropdownMenuItem>
                                                            <DropdownMenuSeparator className="bg-border/40" />
                                                            <DropdownMenuItem
                                                                className="gap-2 text-destructive focus:text-destructive cursor-pointer"
                                                                onClick={async () => {
                                                                    if (confirm('Are you sure you want to delete this link?')) {
                                                                        await deleteLink(link.id);
                                                                    }
                                                                }}
                                                            >
                                                                <Trash2 className="h-4 w-4" /> Delete Link
                                                            </DropdownMenuItem>
                                                        </DropdownMenuContent>
                                                    </DropdownMenu>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </CardContent>
                    <CardFooter className="p-4 border-t border-border/40 bg-muted/5 flex items-center justify-between">
                        <p className="text-xs text-muted-foreground font-medium italic">
                            * Click count and analytics may have a short delay.
                        </p>
                        <div className="flex items-center gap-2">
                            <Button variant="outline" size="sm" className="h-9" disabled>Previous</Button>
                            <Button variant="outline" size="sm" className="h-9" disabled>Next</Button>
                        </div>
                    </CardFooter>
                </Card>
            </div>
        </div>
    );
}
