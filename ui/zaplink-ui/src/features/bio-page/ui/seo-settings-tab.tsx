"use client"

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Textarea } from "@/components/ui/textarea";
import { handleApiError, showSuccessToast } from "@/lib/error-handler";
import { cn } from "@/lib/utils";
import { motion } from "framer-motion";
import {
    Code2,
    Eye,
    Globe,
    Image as ImageIcon,
    Search,
    Share2,
    Wand2,
    Twitter,
    MessageSquare
} from "lucide-react";
import { useState } from "react";
import { useFormContext } from "react-hook-form";

const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: (i: number) => ({
        opacity: 1,
        y: 0,
        transition: {
            delay: i * 0.1,
            duration: 0.3,
        },
    }),
};

export function SeoSettingsTab() {
    const { control, setValue, watch, getValues } = useFormContext();
    const [isGeneratingMeta, setIsGeneratingMeta] = useState(false);

    const seoMeta = watch('seoMeta');
    const isPublic = watch('isPublic');
    const title = watch('title');
    const bioText = watch('bioText');
    const avatarUrl = watch('avatarUrl');
    const username = getValues('username');

    // Parse existing SEO meta
    const parsedSeoMeta = seoMeta ? JSON.parse(seoMeta) : {};

    const updateSeoMeta = (updates: any) => {
        const currentMeta = seoMeta ? JSON.parse(seoMeta) : {};
        const newMeta = { ...currentMeta, ...updates };
        setValue('seoMeta', JSON.stringify(newMeta));
    };

    const generateAutoSeo = async () => {
        setIsGeneratingMeta(true);
        try {
            // Simulate AI-powered SEO generation
            await new Promise(resolve => setTimeout(resolve, 1000));

            const autoSeo = {
                title: title || `@${username} | Zaplink`,
                description: bioText || `Check out ${title || `@${username}`} on Zaplink - your personal link in bio page.`,
                keywords: [title, username, 'bio', 'links', 'zaplink'].filter(Boolean).join(', '),
                ogImage: avatarUrl,
                twitterCard: 'summary_large_image',
                jsonLd: {
                    "@context": "https://schema.org",
                    "@type": "Person",
                    "name": title || username,
                    "description": bioText,
                    "url": `https://zap.link/${username}`,
                    "image": avatarUrl
                }
            };

            updateSeoMeta(autoSeo);
            showSuccessToast('SEO metadata generated successfully');
        } catch (error) {
            handleApiError(error, 'Failed to generate SEO metadata');
        } finally {
            setIsGeneratingMeta(false);
        }
    };

    return (
        <div className="space-y-6 pb-20">
            {/* Header */}
            <motion.div
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 sm:gap-6 pb-6 border-b border-border/40"
            >
                <div className="flex items-center gap-3 sm:gap-4 w-full sm:w-auto">
                    <div className="w-10 h-10 sm:w-12 sm:h-12 flex items-center justify-center shrink-0 bg-[#2A1D45] rounded-xl shadow-inner border border-purple-500/20">
                        <Search className="w-4 h-4 sm:w-5 sm:h-5 text-purple-400" />
                    </div>
                    <div className="flex-1 min-w-0">
                        <h3 className="text-lg sm:text-xl font-bold text-foreground tracking-tight truncate">
                            SEO & Discovery
                        </h3>
                        <p className="text-xs sm:text-sm text-muted-foreground mt-0.5 leading-tight sm:leading-normal">
                            Control how you appear on search engines and social platforms
                        </p>
                    </div>
                </div>
                <Button
                    type="button"
                    onClick={generateAutoSeo}
                    disabled={isGeneratingMeta}
                    variant="default"
                    className="w-full sm:w-auto mt-2 sm:mt-0 gap-2 shrink-0 rounded-xl shadow-md bg-[#8b5cf6] hover:bg-[#7c3aed] text-white transition-all px-5"
                    size="sm"
                >
                    <Wand2 className={cn("w-4 h-4", isGeneratingMeta && "animate-spin")} />
                    {isGeneratingMeta ? "Generating..." : "Auto-Fill via AI"}
                </Button>
            </motion.div>

            <Accordion type="multiple" defaultValue={['visibility', 'basic', 'preview']} className="space-y-4">

                {/* Visibility Settings */}
                <motion.div custom={0} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="visibility" className="border border-border/40 rounded-3xl bg-card/60 shadow-sm overflow-hidden">
                        <AccordionTrigger className="hover:no-underline py-5 px-6">
                            <div className="flex items-center gap-4">
                                <div className="w-10 h-10 flex items-center justify-center bg-[#2A1D45] rounded-xl">
                                    <Globe className="w-5 h-5 text-purple-400" />
                                </div>
                                <div className="text-left leading-tight">
                                    <span className="font-bold text-base tracking-tight">Page Visibility</span>
                                    <p className="text-sm text-muted-foreground mt-0.5">Control public access</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-6 px-6">
                            <FormField
                                control={control}
                                name="isPublic"
                                render={({ field }) => (
                                    <FormItem className="flex flex-row items-center justify-between rounded-2xl border border-border/30 p-5 lg:p-6 bg-[#130E1B] shadow-inner transition-colors">
                                        <div className="space-y-1.5">
                                            <FormLabel className="text-base font-bold flex items-center gap-2.5 text-foreground">
                                                Public Page
                                                {field.value && <Badge variant="secondary" className="text-[10px] sm:text-xs uppercase font-bold tracking-wider bg-emerald-500/10 text-emerald-500 hover:bg-emerald-500/20 shadow-none border max-h-[22px] border-emerald-500/20 px-2 rounded-xl">Live & Indexed</Badge>}
                                                {!field.value && <Badge variant="secondary" className="text-[10px] sm:text-xs uppercase font-bold tracking-wider bg-amber-500/10 text-amber-500 hover:bg-amber-500/20 shadow-none border max-h-[22px] border-amber-500/20 px-2 rounded-xl">Draft / Private</Badge>}
                                            </FormLabel>
                                            <div className="text-sm text-muted-foreground/80 font-medium">
                                                Make this page discoverable by search engines and visitors
                                            </div>
                                        </div>
                                        <FormControl>
                                            <Switch
                                                checked={field.value}
                                                onCheckedChange={field.onChange}
                                                className="data-[state=checked]:bg-emerald-500 scale-110"
                                            />
                                        </FormControl>
                                    </FormItem>
                                )}
                            />
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Basic SEO */}
                <motion.div custom={1} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="basic" className="border border-border/40 rounded-3xl bg-card/60 shadow-sm overflow-hidden">
                        <AccordionTrigger className="hover:no-underline py-5 px-6">
                            <div className="flex items-center gap-4">
                                <div className="w-10 h-10 flex items-center justify-center bg-[#1D3245] rounded-xl">
                                    <Search className="w-5 h-5 text-blue-400" />
                                </div>
                                <div className="text-left leading-tight">
                                    <span className="font-bold text-base tracking-tight">Metadata Settings</span>
                                    <p className="text-sm text-muted-foreground mt-0.5">Title, description, and keywords</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-8 px-6 space-y-7">
                            <div className="space-y-2">
                                <Label htmlFor="seo-title" className="text-sm font-bold text-foreground">SEO Title</Label>
                                <Input
                                    id="seo-title"
                                    placeholder="e.g. John Doe | Creative Director"
                                    value={parsedSeoMeta.title || ''}
                                    onChange={(e) => updateSeoMeta({ title: e.target.value.slice(0, 60) })}
                                    maxLength={60}
                                    className="h-12 rounded-2xl bg-black/20 border-border/20 focus:bg-black/40 focus:border-border/50 focus:ring-0 shadow-inner px-4 text-sm"
                                />
                                <div className="flex justify-between text-xs font-semibold mt-1 px-1">
                                    <span className="text-muted-foreground/60">The blue link shown in Google</span>
                                    <span className={cn(
                                        parsedSeoMeta.title?.length > 50 ? "text-amber-500" : "text-muted-foreground/60"
                                    )}>
                                        {parsedSeoMeta.title?.length || 0}/60
                                    </span>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="seo-description" className="text-sm font-bold text-foreground">Meta Description</Label>
                                <Textarea
                                    id="seo-description"
                                    placeholder="Enter a compelling description to encourage clicks..."
                                    value={parsedSeoMeta.description || ''}
                                    onChange={(e) => updateSeoMeta({ description: e.target.value.slice(0, 160) })}
                                    rows={3}
                                    maxLength={160}
                                    className="resize-none rounded-2xl bg-black/20 border-border/20 focus:bg-black/40 focus:border-border/50 focus:ring-0 shadow-inner px-4 py-3 text-sm leading-relaxed"
                                />
                                <div className="flex justify-between text-xs font-semibold mt-1 px-1">
                                    <span className="text-muted-foreground/60">Brief summary displayed below the title</span>
                                    <span className={cn(
                                        parsedSeoMeta.description?.length > 150 ? "text-amber-500" : "text-muted-foreground/60"
                                    )}>
                                        {parsedSeoMeta.description?.length || 0}/160
                                    </span>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="seo-keywords" className="text-sm font-bold text-foreground">Keywords</Label>
                                <Input
                                    id="seo-keywords"
                                    placeholder="designer, developer, portfolio, social media"
                                    value={parsedSeoMeta.keywords || ''}
                                    onChange={(e) => updateSeoMeta({ keywords: e.target.value })}
                                    className="h-12 rounded-2xl bg-black/20 border-border/20 focus:bg-black/40 focus:border-border/50 focus:ring-0 shadow-inner px-4 text-sm"
                                />
                                <p className="text-xs text-muted-foreground/60 font-semibold px-1 mt-1">
                                    Separate with commas. Less relevant for Google today, but still used by some minor search engines.
                                </p>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Google Search Preview */}
                <motion.div custom={2} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="preview" className="border border-border/40 rounded-3xl bg-card/60 shadow-sm overflow-hidden">
                        <AccordionTrigger className="hover:no-underline py-5 px-6">
                            <div className="flex items-center gap-4">
                                <div className="w-10 h-10 flex items-center justify-center bg-[#45321D] rounded-xl">
                                    <Eye className="w-5 h-5 text-orange-400" />
                                </div>
                                <div className="text-left leading-tight">
                                    <span className="font-bold text-base tracking-tight">Search Preview</span>
                                    <p className="text-sm text-muted-foreground mt-0.5">See how you look on Google</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-6 px-6">
                            <div className="border border-border/60 rounded-xl p-6 bg-background shadow-inner">
                                <div className="max-w-[600px] font-sans">
                                    <div className="flex items-center gap-3 text-sm text-[#4d5156] dark:text-[#9aa0a6] mb-2">
                                        <div className="w-7 h-7 rounded-xl bg-muted flex items-center justify-center border border-border/50 overflow-hidden shrink-0">
                                            {avatarUrl ? (
                                                <img src={avatarUrl} alt="favicon" className="w-full h-full object-cover" />
                                            ) : (
                                                <Globe className="w-4 h-4" />
                                            )}
                                        </div>
                                        <div className="flex flex-col leading-tight">
                                            <span className="text-[#202124] dark:text-[#dadce0] font-normal">Zaplink.io</span>
                                            <span className="text-xs opacity-80">https://zap.link/{username}</span>
                                        </div>
                                    </div>
                                    <div className="text-[#1a0dab] dark:text-[#8ab4f8] text-xl leading-[1.3] font-normal hover:underline cursor-pointer break-words">
                                        {parsedSeoMeta.title || title || `@${username} | Zaplink`}
                                    </div>
                                    <div className="text-[#4d5156] dark:text-[#bdc1c6] text-sm leading-[1.58] mt-1 break-words line-clamp-2">
                                        {parsedSeoMeta.description || bioText || 'No description available. Add one to improve your search visibility. Click to see links and social profiles.'}
                                    </div>
                                </div>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Open Graph & Social Preview */}
                <motion.div custom={3} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="opengraph" className="border border-border/40 rounded-3xl bg-card/60 shadow-sm overflow-hidden">
                        <AccordionTrigger className="hover:no-underline py-5 px-6">
                            <div className="flex items-center gap-4">
                                <div className="w-10 h-10 flex items-center justify-center bg-[#451D32] rounded-xl">
                                    <Share2 className="w-5 h-5 text-pink-400" />
                                </div>
                                <div className="text-left leading-tight">
                                    <span className="font-bold text-base tracking-tight">Social Media Preview</span>
                                    <p className="text-sm text-muted-foreground mt-0.5">Control your appearance on Twitter/iMessage</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-8 px-6 space-y-7">

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                {/* Settings */}
                                <div className="space-y-6">
                                    <div className="space-y-2">
                                        <Label htmlFor="og-image" className="text-sm font-bold text-foreground">Open Graph Image URL</Label>
                                        <Input
                                            id="og-image"
                                            placeholder="https://example.com/og-image.jpg"
                                            value={parsedSeoMeta.ogImage || ''}
                                            onChange={(e) => updateSeoMeta({ ogImage: e.target.value })}
                                            className="h-12 rounded-2xl bg-black/20 border-border/20 focus:bg-black/40 focus:border-border/50 focus:ring-0 shadow-inner px-4 text-sm"
                                        />
                                        <p className="text-xs text-muted-foreground/60 font-semibold px-1 mt-1">
                                            Recommended size: 1200×630px. Fallbacks to avatar if empty.
                                        </p>
                                    </div>

                                    <div className="space-y-2">
                                        <Label htmlFor="twitter-card" className="text-sm font-bold text-foreground">Twitter Card Style</Label>
                                        <select
                                            id="twitter-card"
                                            className="w-full h-12 px-4 shadow-inner border border-border/20 rounded-2xl bg-black/20 text-sm font-medium focus:bg-black/40 focus:border-border/50 focus:ring-0 outline-none transition-all appearance-none cursor-pointer"
                                            value={parsedSeoMeta.twitterCard || 'summary_large_image'}
                                            onChange={(e) => updateSeoMeta({ twitterCard: e.target.value })}
                                        >
                                            <option value="summary_large_image">Large Image Card (Recommended)</option>
                                            <option value="summary">Small Image Summary</option>
                                        </select>
                                    </div>
                                </div>

                                {/* Realistic Social Preview Card */}
                                <div className="border border-border/60 rounded-2xl p-4 bg-background/50 flex items-center justify-center">
                                    <div className="w-full max-w-[340px] border border-border/60 rounded-2xl overflow-hidden bg-background shadow-sm hover:bg-muted/30 transition-colors cursor-pointer group">
                                        {/* Fake Image Area */}
                                        <div className="w-full aspect-[1.91/1] bg-muted/50 border-b border-border/60 relative flex items-center justify-center overflow-hidden">
                                            {parsedSeoMeta.ogImage || avatarUrl ? (
                                                <img src={parsedSeoMeta.ogImage || avatarUrl} alt="OG" className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
                                            ) : (
                                                <ImageIcon className="w-10 h-10 text-muted-foreground/30" />
                                            )}
                                        </div>
                                        {/* Fake Text Area */}
                                        <div className="p-3">
                                            <p className="text-sm text-muted-foreground uppercase tracking-widest leading-none mb-1 font-medium">zap.link</p>
                                            <h4 className="font-semibold text-base leading-tight text-foreground line-clamp-1">
                                                {parsedSeoMeta.title || title || `@${username} | Zaplink`}
                                            </h4>
                                            <p className="text-sm text-muted-foreground mt-0.5 line-clamp-2 leading-snug">
                                                {parsedSeoMeta.description || bioText || 'Join me on Zaplink to see all my links in one place.'}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Advanced SEO */}
                <motion.div custom={4} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="advanced" className="border border-border/40 rounded-3xl bg-card/60 shadow-sm overflow-hidden">
                        <AccordionTrigger className="hover:no-underline py-5 px-6">
                            <div className="flex items-center gap-4">
                                <div className="w-10 h-10 flex items-center justify-center bg-[#292D3E] rounded-xl">
                                    <Code2 className="w-5 h-5 text-slate-400" />
                                </div>
                                <div className="text-left leading-tight">
                                    <span className="font-bold text-base tracking-tight">Advanced Schema</span>
                                    <p className="text-sm text-muted-foreground mt-0.5">JSON-LD for rich snippets</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-8 px-6">
                            <div className="space-y-2">
                                <Label htmlFor="json-ld" className="text-sm font-bold text-foreground flex items-center justify-between">
                                    JSON-LD Payload
                                    <Badge variant="outline" className="text-[10px] sm:text-xs font-mono tracking-wider border-border/40">ADVANCED</Badge>
                                </Label>
                                <Textarea
                                    id="json-ld"
                                    placeholder={`{\n  "@context": "https://schema.org",\n  "@type": "Person",\n  "name": "Your Name"\n}`}
                                    value={parsedSeoMeta.jsonLd ? JSON.stringify(parsedSeoMeta.jsonLd, null, 2) : ''}
                                    onChange={(e) => {
                                        try {
                                            const jsonLd = e.target.value ? JSON.parse(e.target.value) : null;
                                            updateSeoMeta({ jsonLd });
                                        } catch (error) {
                                            // Invalid JSON, don't update immediately
                                        }
                                    }}
                                    rows={10}
                                    className="font-mono text-xs resize-none rounded-xl bg-slate-950 text-slate-50 border-slate-800 focus:ring-slate-700 p-4"
                                />
                                <p className="text-xs text-muted-foreground font-medium flex items-center gap-1.5">
                                    <Globe className="w-3.5 h-3.5" />
                                    This script will be injected into the &lt;head&gt; of your public page. Ensure valid JSON structure.
                                </p>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

            </Accordion>
        </div>
    );
}
