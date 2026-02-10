"use client"

import { motion } from "framer-motion";
import { useFormContext } from "react-hook-form";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Switch } from "@/components/ui/switch";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useState } from "react";
import { handleApiError, showSuccessToast } from "@/lib/error-handler";
import { 
  Search, 
  Globe, 
  Share2, 
  Code2, 
  Eye, 
  Wand2,
  CheckCircle2,
  Image as ImageIcon
} from "lucide-react";
import { cn } from "@/lib/utils";

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
                    <div className="p-2 bg-gradient-to-br from-emerald-100 to-teal-100 rounded-xl">
                        <Search className="w-5 h-5 text-emerald-600" />
                    </div>
                    <div>
                        <h3 className="text-lg font-semibold bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                            SEO Settings
                        </h3>
                        <p className="text-sm text-muted-foreground">
                            Optimize your page for search engines
                        </p>
                    </div>
                </div>
                <Button 
                    type="button" 
                    onClick={generateAutoSeo}
                    disabled={isGeneratingMeta}
                    className="bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-700 hover:to-teal-700 shadow-md"
                >
                    <Wand2 className="w-4 h-4 mr-2" />
                    {isGeneratingMeta ? "Generating..." : "Auto-Generate"}
                </Button>
            </motion.div>

            {/* Visibility Settings */}
            <motion.div custom={0} initial="hidden" animate="visible" variants={cardVariants}>
                <Card className="border shadow-sm overflow-hidden">
                    <CardHeader className="bg-gradient-to-r from-blue-50 to-indigo-50 border-b pb-3">
                        <div className="flex items-center gap-2">
                            <Globe className="w-4 h-4 text-blue-600" />
                            <CardTitle className="text-base font-semibold">Page Visibility</CardTitle>
                        </div>
                    </CardHeader>
                    <CardContent className="pt-4">
                        <FormField
                            control={control}
                            name="isPublic"
                            render={({ field }) => (
                                <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4 bg-gradient-to-r from-white to-muted/20">
                                    <div className="space-y-1">
                                        <FormLabel className="text-base font-semibold flex items-center gap-2">
                                            Public Page
                                            {field.value && <Badge variant="secondary" className="text-[10px] bg-emerald-100 text-emerald-700">Live</Badge>}
                                        </FormLabel>
                                        <div className="text-sm text-muted-foreground">
                                            Make this page discoverable and accessible to everyone
                                        </div>
                                    </div>
                                    <FormControl>
                                        <Switch 
                                            checked={field.value} 
                                            onCheckedChange={field.onChange}
                                            className="data-[state=checked]:bg-emerald-500"
                                        />
                                    </FormControl>
                                </FormItem>
                            )}
                        />
                    </CardContent>
                </Card>
            </motion.div>

            {/* Basic SEO */}
            <motion.div custom={1} initial="hidden" animate="visible" variants={cardVariants}>
                <Card className="border shadow-sm overflow-hidden">
                    <CardHeader className="bg-gradient-to-r from-violet-50 to-purple-50 border-b pb-3">
                        <div className="flex items-center gap-2">
                            <Search className="w-4 h-4 text-violet-600" />
                            <CardTitle className="text-base font-semibold">Basic SEO</CardTitle>
                        </div>
                    </CardHeader>
                    <CardContent className="pt-4 space-y-5">
                        <div className="space-y-2.5">
                            <Label htmlFor="seo-title" className="text-sm font-medium flex items-center gap-2">
                                SEO Title
                                <span className="text-xs text-muted-foreground font-normal">(60 chars max)</span>
                            </Label>
                            <Input
                                id="seo-title"
                                placeholder="Enter SEO title"
                                value={parsedSeoMeta.title || ''}
                                onChange={(e) => updateSeoMeta({ title: e.target.value.slice(0, 60) })}
                                maxLength={60}
                                className="h-11"
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

                        <div className="space-y-2.5">
                            <Label htmlFor="seo-description" className="text-sm font-medium flex items-center gap-2">
                                Meta Description
                                <span className="text-xs text-muted-foreground font-normal">(160 chars max)</span>
                            </Label>
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

                        <div className="space-y-2.5">
                            <Label htmlFor="seo-keywords" className="text-sm font-medium">Keywords</Label>
                            <Input
                                id="seo-keywords"
                                placeholder="bio, links, portfolio, social media"
                                value={parsedSeoMeta.keywords || ''}
                                onChange={(e) => updateSeoMeta({ keywords: e.target.value })}
                                className="h-11"
                            />
                            <p className="text-xs text-muted-foreground">
                                Separate keywords with commas. Helps search engines understand your content.
                            </p>
                        </div>
                    </CardContent>
                </Card>
            </motion.div>

            {/* Open Graph */}
            <motion.div custom={2} initial="hidden" animate="visible" variants={cardVariants}>
                <Card className="border shadow-sm overflow-hidden">
                    <CardHeader className="bg-gradient-to-r from-pink-50 to-rose-50 border-b pb-3">
                        <div className="flex items-center gap-2">
                            <Share2 className="w-4 h-4 text-pink-600" />
                            <CardTitle className="text-base font-semibold">Social Media Preview</CardTitle>
                        </div>
                    </CardHeader>
                    <CardContent className="pt-4 space-y-4">
                        <div className="space-y-2.5">
                            <Label htmlFor="og-image" className="text-sm font-medium flex items-center gap-2">
                                <ImageIcon className="w-4 h-4" />
                                Open Graph Image
                            </Label>
                            <Input
                                id="og-image"
                                placeholder="https://example.com/og-image.jpg"
                                value={parsedSeoMeta.ogImage || ''}
                                onChange={(e) => updateSeoMeta({ ogImage: e.target.value })}
                                className="h-11"
                            />
                            <p className="text-xs text-muted-foreground">
                                Image shown when your page is shared on social media (1200×630px recommended)
                            </p>
                        </div>

                        <div className="space-y-2.5">
                            <Label htmlFor="twitter-card" className="text-sm font-medium">Twitter Card Type</Label>
                            <select
                                id="twitter-card"
                                className="w-full h-11 px-3 border rounded-md bg-white text-sm focus:ring-2 focus:ring-pink-500/20 focus:border-pink-500 transition-all"
                                value={parsedSeoMeta.twitterCard || 'summary_large_image'}
                                onChange={(e) => updateSeoMeta({ twitterCard: e.target.value })}
                            >
                                <option value="summary">Summary Card</option>
                                <option value="summary_large_image">Summary Card with Large Image</option>
                            </select>
                        </div>
                    </CardContent>
                </Card>
            </motion.div>

            {/* Search Preview */}
            <motion.div custom={3} initial="hidden" animate="visible" variants={cardVariants}>
                <Card className="border shadow-sm overflow-hidden">
                    <CardHeader className="bg-gradient-to-r from-amber-50 to-orange-50 border-b pb-3">
                        <div className="flex items-center gap-2">
                            <Eye className="w-4 h-4 text-amber-600" />
                            <CardTitle className="text-base font-semibold">Search Preview</CardTitle>
                        </div>
                    </CardHeader>
                    <CardContent className="pt-4">
                        <div className="border rounded-xl p-5 bg-gradient-to-br from-white to-slate-50 shadow-inner">
                            <div className="space-y-2">
                                <div className="flex items-center gap-2 text-xs text-muted-foreground mb-1">
                                    <Globe className="w-3 h-3" />
                                    zap.link/{getValues('username')}
                                </div>
                                <div className="text-blue-600 text-lg font-medium hover:underline cursor-pointer line-clamp-1">
                                    {parsedSeoMeta.title || title || `@${getValues('username')} | Zaplink`}
                                </div>
                                <div className="text-gray-600 text-sm line-clamp-2 leading-relaxed">
                                    {parsedSeoMeta.description || bioText || 'No description available. Add one to improve your search visibility.'}
                                </div>
                                {parsedSeoMeta.keywords && (
                                    <div className="flex flex-wrap gap-1 pt-2">
                                        {parsedSeoMeta.keywords.split(',').slice(0, 5).map((kw: string, i: number) => (
                                            <span key={i} className="text-[10px] px-2 py-0.5 bg-gray-100 text-gray-600 rounded-full">
                                                {kw.trim()}
                                            </span>
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </motion.div>

            {/* Advanced SEO */}
            <motion.div custom={4} initial="hidden" animate="visible" variants={cardVariants}>
                <Card className="border shadow-sm overflow-hidden">
                    <CardHeader className="bg-gradient-to-r from-slate-50 to-gray-50 border-b pb-3">
                        <div className="flex items-center gap-2">
                            <Code2 className="w-4 h-4 text-slate-600" />
                            <CardTitle className="text-base font-semibold">Advanced SEO</CardTitle>
                        </div>
                    </CardHeader>
                    <CardContent className="pt-4 space-y-3">
                        <div className="space-y-2.5">
                            <Label htmlFor="json-ld" className="text-sm font-medium">JSON-LD Structured Data</Label>
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
                                className="font-mono text-xs resize-none bg-slate-50"
                            />
                            <p className="text-xs text-muted-foreground">
                                Structured data helps search engines understand your content better
                            </p>
                        </div>
                    </CardContent>
                </Card>
            </motion.div>
        </div>
    );
}
