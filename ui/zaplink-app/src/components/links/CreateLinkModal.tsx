'use client';

import { useState } from 'react';
import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetHeader,
    SheetTitle,
    SheetTrigger,
} from '@/components/ui/sheet';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Link2, Rocket, Copy, Check, ExternalLink } from 'lucide-react';
import { useLinks } from '@/hooks/useLinks';
import { motion, AnimatePresence } from 'framer-motion';
import { toast } from 'sonner';

interface CreateLinkModalProps {
    trigger?: React.ReactNode;
    open?: boolean;
    onOpenChange?: (open: boolean) => void;
}

export function CreateLinkModal({ trigger, open, onOpenChange }: CreateLinkModalProps) {
    const [url, setUrl] = useState('');
    const [shortenedUrl, setShortenedUrl] = useState<string | null>(null);
    const [isCopied, setIsCopied] = useState(false);
    const { shortenUrl, isLoading } = useLinks();

    const handleShorten = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!url) return;

        const result = await shortenUrl(url);
        if (result) {
            setShortenedUrl(result.shortUrl);
        }
    };

    const copyToClipboard = () => {
        if (shortenedUrl) {
            navigator.clipboard.writeText(shortenedUrl);
            setIsCopied(true);
            toast.success('Copied to clipboard!');
            setTimeout(() => setIsCopied(false), 2000);
        }
    };

    const reset = () => {
        setUrl('');
        setShortenedUrl(null);
        setIsCopied(false);
    };

    return (
        <Sheet open={open} onOpenChange={(val) => {
            if (!val) reset();
            onOpenChange?.(val);
        }}>
            {trigger && <SheetTrigger asChild>{trigger}</SheetTrigger>}
            <SheetContent side="right" className="w-full sm:max-w-md border-l border-border/40 bg-background/95 backdrop-blur-xl p-0">
                <div className="flex flex-col h-full">
                    <SheetHeader className="p-6 border-b border-border/40 bg-muted/5">
                        <div className="flex items-center gap-3 mb-2">
                            <div className="h-10 w-10 rounded-xl bg-primary/10 flex items-center justify-center">
                                <Link2 className="h-5 w-5 text-primary" />
                            </div>
                            <div>
                                <SheetTitle className="text-2xl font-bold font-display tracking-tight">Create Short Link</SheetTitle>
                                <SheetDescription className="font-medium">
                                    Transform your long URL into a short, trackable link.
                                </SheetDescription>
                            </div>
                        </div>
                    </SheetHeader>

                    <div className="flex-1 overflow-y-auto p-6">
                        <form onSubmit={handleShorten} className="space-y-6">
                            <div className="space-y-3">
                                <Label htmlFor="url" className="text-sm font-bold font-display uppercase tracking-wider text-muted-foreground ml-1">
                                    Destination URL
                                </Label>
                                <div className="relative group">
                                    <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-muted-foreground group-focus-within:text-primary transition-colors">
                                        <Link2 className="h-5 w-5" />
                                    </div>
                                    <Input
                                        id="url"
                                        placeholder="https://example.com/very/long/url"
                                        className="h-12 pl-12 bg-muted/30 border-border/40 focus:border-primary transition-all font-medium rounded-xl"
                                        value={url}
                                        onChange={(e) => setUrl(e.target.value)}
                                        disabled={isLoading || !!shortenedUrl}
                                    />
                                </div>
                                <p className="text-[10px] text-muted-foreground px-1">
                                    You can also add custom aliases and tags in the Link Settings after creation.
                                </p>
                            </div>

                            {!shortenedUrl && (
                                <Button
                                    type="submit"
                                    className="w-full h-12 rounded-xl text-base font-bold font-display shadow-lg shadow-primary/20 hover:shadow-primary/30 transition-all active:scale-[0.98]"
                                    disabled={isLoading || !url}
                                >
                                    {isLoading ? (
                                        <div className="flex items-center gap-2">
                                            <div className="h-4 w-4 border-2 border-white/30 border-t-white animate-spin rounded-full" />
                                            <span>Creating...</span>
                                        </div>
                                    ) : (
                                        <>
                                            <Rocket className="w-4 h-4 mr-2" />
                                            Shorten Link
                                        </>
                                    )}
                                </Button>
                            )}
                        </form>

                        <AnimatePresence>
                            {shortenedUrl && (
                                <motion.div
                                    initial={{ opacity: 0, scale: 0.95, y: 10 }}
                                    animate={{ opacity: 1, scale: 1, y: 0 }}
                                    className="mt-8 p-6 rounded-2xl bg-primary/5 border border-primary/20 relative overflow-hidden group"
                                >
                                    <div className="absolute top-0 right-0 p-4 opacity-10 group-hover:opacity-20 transition-opacity">
                                        <Rocket className="h-12 w-12 text-primary rotate-12" />
                                    </div>

                                    <div className="relative z-10 flex flex-col gap-4">
                                        <div className="flex flex-col gap-1">
                                            <span className="text-[10px] font-bold uppercase tracking-widest text-primary/70">Short Link Ready</span>
                                            <span className="text-xl font-bold font-display text-foreground break-all leading-tight">
                                                {shortenedUrl}
                                            </span>
                                        </div>

                                        <div className="flex gap-2">
                                            <Button
                                                onClick={copyToClipboard}
                                                className="flex-1 h-11 gap-2 rounded-xl font-bold font-display shadow-sm"
                                            >
                                                {isCopied ? <Check className="h-4 w-4" /> : <Copy className="h-4 w-4" />}
                                                {isCopied ? 'Copied' : 'Copy Link'}
                                            </Button>
                                            <Button
                                                variant="outline"
                                                size="icon"
                                                className="h-11 w-11 rounded-xl bg-background hover:bg-muted border-border/40"
                                                asChild
                                            >
                                                <a href={shortenedUrl} target="_blank" rel="noopener noreferrer">
                                                    <ExternalLink className="h-4 w-4" />
                                                </a>
                                            </Button>
                                        </div>

                                        <Button
                                            variant="ghost"
                                            className="text-xs font-semibold text-muted-foreground hover:text-primary transition-colors"
                                            onClick={reset}
                                        >
                                            Create another one
                                        </Button>
                                    </div>
                                </motion.div>
                            )}
                        </AnimatePresence>
                    </div>

                    <div className="p-6 border-t border-border/40 bg-muted/5 mt-auto">
                        <div className="flex flex-col gap-4">
                            <div className="flex items-center gap-2 text-xs text-muted-foreground font-medium">
                                <div className="h-1.5 w-1.5 rounded-full bg-green-500 animate-pulse" />
                                Links are automatically HTTPS encrypted
                            </div>
                        </div>
                    </div>
                </div>
            </SheetContent>
        </Sheet>
    );
}
