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
    Wand2
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
                title: title || `@${getValues('username')} | Zaplink`,
                description: bioText || `Check out ${title || `@${getValues('username')}`} on Zaplink - your personal link in bio page.`,
                keywords: [title, getValues('username'), 'bio', 'links', 'zaplink'].filter(Boolean).join(', '),
                ogImage: getValues('avatarUrl'),
                twitterCard: 'summary_large_image',
                jsonLd: {
                    "@context": "https://schema.org",
                    "@type": "Person",
                    "name": title || getValues('username'),
                    "description": bioText,
                    "url": `https://zap.link/${getValues('username')}`,
                    "image": getValues('avatarUrl')
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
                className="flex items-center justify-between pb-4 border-b"
            >
                <div className="flex items-center gap-3">
                    <div className="p-2 bg-primary/10 rounded-xl">
                        <Search className="w-5 h-5 text-primary" />
                    </div>
                    <div>
                        <h3 className="text-lg font-semibold text-foreground">
                            SEO Settings
                        </h3>
                        <p className="text-sm text-muted-foreground">
                            Optimize your page for search engines and social media.
                        </p>
                    </div>
                </div>
                <Button
                    type="button"
                    onClick={generateAutoSeo}
                    disabled={isGeneratingMeta}
                    variant="outline"
                    className="gap-2"
                    size="sm"
                >
                    <Wand2 className="w-4 h-4" />
                    {isGeneratingMeta ? "Generating..." : "Auto-Generate"}
                </Button>
            </motion.div>

            <Accordion type="multiple" defaultValue={['visibility', 'basic', 'preview']} className="space-y-4">

                {/* Visibility Settings */}
                <motion.div custom={0} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="visibility" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                        <AccordionTrigger className="hover:no-underline py-4">
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-primary/10 rounded-lg">
                                    <Globe className="w-4 h-4 text-primary" />
                                </div>
                                <div className="text-left">
                                    <span className="font-semibold text-sm">Page Visibility</span>
                                    <p className="text-xs text-muted-foreground font-normal">Control who can see your page</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-4">
                            <FormField
                                control={control}
                                name="isPublic"
                                render={({ field }) => (
                                    <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4 bg-card/50">
                                        <div className="space-y-0.5">
                                            <FormLabel className="text-base flex items-center gap-2">
                                                Public Page
                                                {field.value && <Badge variant="secondary" className="text-xs bg-emerald-500/10 text-emerald-500 hover:bg-emerald-500/20 shadow-none border-0">Live</Badge>}
                                            </FormLabel>
                                            <div className="text-sm text-muted-foreground">
                                                Make this page discoverable and accessible to everyone
                                            </div>
                                        </div>
                                        <FormControl>
                                            <Switch
                                                checked={field.value}
                                                onCheckedChange={field.onChange}
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
                    <AccordionItem value="basic" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                        <AccordionTrigger className="hover:no-underline py-4">
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-blue-500/10 rounded-lg">
                                    <Search className="w-4 h-4 text-blue-500" />
                                </div>
                                <div className="text-left">
                                    <span className="font-semibold text-sm">Basic SEO</span>
                                    <p className="text-xs text-muted-foreground font-normal">Metadata for search engine results</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-4 space-y-5">
                            <div className="space-y-2">
                                <Label htmlFor="seo-title">SEO Title</Label>
                                <Input
                                    id="seo-title"
                                    placeholder="Enter SEO title"
                                    value={parsedSeoMeta.title || ''}
                                    onChange={(e) => updateSeoMeta({ title: e.target.value.slice(0, 60) })}
                                    maxLength={60}
                                />
                                <div className="flex justify-between text-xs">
                                    <span className="text-muted-foreground">Shown in search results</span>
                                    <span className={cn(
                                        parsedSeoMeta.title?.length > 50 ? "text-amber-500" : "text-muted-foreground"
                                    )}>
                                        {parsedSeoMeta.title?.length || 0}/60
                                    </span>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="seo-description">Meta Description</Label>
                                <Textarea
                                    id="seo-description"
                                    placeholder="Enter meta description"
                                    value={parsedSeoMeta.description || ''}
                                    onChange={(e) => updateSeoMeta({ description: e.target.value.slice(0, 160) })}
                                    rows={3}
                                    maxLength={160}
                                    className="resize-none"
                                />
                                <div className="flex justify-between text-xs">
                                    <span className="text-muted-foreground">Brief summary of your page</span>
                                    <span className={cn(
                                        parsedSeoMeta.description?.length > 150 ? "text-amber-500" : "text-muted-foreground"
                                    )}>
                                        {parsedSeoMeta.description?.length || 0}/160
                                    </span>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="seo-keywords">Keywords</Label>
                                <Input
                                    id="seo-keywords"
                                    placeholder="bio, links, portfolio, social media"
                                    value={parsedSeoMeta.keywords || ''}
                                    onChange={(e) => updateSeoMeta({ keywords: e.target.value })}
                                />
                                <p className="text-xs text-muted-foreground">
                                    Separate keywords with commas.
                                </p>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Open Graph */}
                <motion.div custom={2} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="opengraph" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                        <AccordionTrigger className="hover:no-underline py-4">
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-pink-500/10 rounded-lg">
                                    <Share2 className="w-4 h-4 text-pink-500" />
                                </div>
                                <div className="text-left">
                                    <span className="font-semibold text-sm">Social Media Preview</span>
                                    <p className="text-xs text-muted-foreground font-normal">How your page looks when shared</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-4 space-y-4">
                            <div className="space-y-2">
                                <Label htmlFor="og-image">Open Graph Image URL</Label>
                                <div className="flex gap-2">
                                    <Input
                                        id="og-image"
                                        placeholder="https://example.com/og-image.jpg"
                                        value={parsedSeoMeta.ogImage || ''}
                                        onChange={(e) => updateSeoMeta({ ogImage: e.target.value })}
                                    />
                                </div>
                                <p className="text-xs text-muted-foreground">
                                    Recommended size: 1200×630px
                                </p>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="twitter-card">Twitter Card Type</Label>
                                <select
                                    id="twitter-card"
                                    className="w-full h-10 px-3 border rounded-md bg-background text-sm focus:ring-2 focus:ring-ring focus:outline-none transition-all"
                                    value={parsedSeoMeta.twitterCard || 'summary_large_image'}
                                    onChange={(e) => updateSeoMeta({ twitterCard: e.target.value })}
                                >
                                    <option value="summary">Summary Card</option>
                                    <option value="summary_large_image">Summary Card with Large Image</option>
                                </select>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Search Preview */}
                <motion.div custom={3} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="preview" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                        <AccordionTrigger className="hover:no-underline py-4">
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-amber-500/10 rounded-lg">
                                    <Eye className="w-4 h-4 text-amber-500" />
                                </div>
                                <div className="text-left">
                                    <span className="font-semibold text-sm">Search Preview</span>
                                    <p className="text-xs text-muted-foreground font-normal">Real-time Google search result preview</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-4">
                            <div className="border rounded-lg p-5 bg-card/50 shadow-sm">
                                <div className="space-y-1">
                                    <div className="flex items-center gap-2 text-xs text-muted-foreground mb-1">
                                        <div className="w-6 h-6 rounded-full bg-muted flex items-center justify-center text-[10px]">
                                            <Globe className="w-3 h-3" />
                                        </div>
                                        <span className="text-sm text-foreground">Zaplink</span>
                                        <span>•</span>
                                        <span>https://zap.link/{getValues('username')}</span>
                                    </div>
                                    <div className="text-[#1a0dab] dark:text-[#8ab4f8] text-xl font-medium hover:underline cursor-pointer line-clamp-1">
                                        {parsedSeoMeta.title || title || `@${getValues('username')} | Zaplink`}
                                    </div>
                                    <div className="text-muted-foreground text-sm line-clamp-2 leading-relaxed max-w-3xl">
                                        {parsedSeoMeta.description || bioText || 'No description available. Add one to improve your search visibility.'}
                                    </div>
                                </div>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

                {/* Advanced SEO */}
                <motion.div custom={4} initial="hidden" animate="visible" variants={cardVariants}>
                    <AccordionItem value="advanced" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                        <AccordionTrigger className="hover:no-underline py-4">
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-slate-500/10 rounded-lg">
                                    <Code2 className="w-4 h-4 text-slate-500" />
                                </div>
                                <div className="text-left">
                                    <span className="font-semibold text-sm">Advanced SEO</span>
                                    <p className="text-xs text-muted-foreground font-normal">Structured data and technical settings</p>
                                </div>
                            </div>
                        </AccordionTrigger>
                        <AccordionContent className="pb-4">
                            <div className="space-y-2">
                                <Label htmlFor="json-ld">JSON-LD Structured Data</Label>
                                <Textarea
                                    id="json-ld"
                                    placeholder={`{\n  "@context": "https://schema.org",\n  "@type": "Person",\n  "name": "Your Name"\n}`}
                                    value={parsedSeoMeta.jsonLd ? JSON.stringify(parsedSeoMeta.jsonLd, null, 2) : ''}
                                    onChange={(e) => {
                                        try {
                                            const jsonLd = e.target.value ? JSON.parse(e.target.value) : null;
                                            updateSeoMeta({ jsonLd });
                                        } catch (error) {
                                            // Invalid JSON, don't update
                                        }
                                    }}
                                    rows={8}
                                    className="font-mono text-xs resize-none"
                                />
                                <p className="text-xs text-muted-foreground">
                                    Valid JSON-LD structure only.
                                </p>
                            </div>
                        </AccordionContent>
                    </AccordionItem>
                </motion.div>

            </Accordion>
        </div>
    );
}
