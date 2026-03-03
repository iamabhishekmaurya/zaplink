"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Sheet, SheetContent, SheetTrigger, SheetTitle } from "@/components/ui/sheet";
import { Textarea } from "@/components/ui/textarea";
import { useBioPageEditor } from "@/features/bio-page/hooks/useBioPageEditor";
import { AnalyticsTab } from "@/features/bio-page/ui/analytics-tab";
import { AvatarUpload } from "@/features/bio-page/ui/avatar-upload";
import { BioLinkManager } from "@/features/bio-page/ui/bio-link-manager";
import { PreviewPanel } from "@/features/bio-page/ui/preview-panel/index";
import { SeoSettingsTab } from "@/features/bio-page/ui/seo-settings-tab";
import { ProfileTab } from "@/features/bio-page/ui/profile-tab";
import { ThemeEditor } from "@/features/bio-page/ui/theme-editor/index";
import { LayoutSelector } from "@/features/bio-page/ui/theme-editor/layout-selector";
import { cn } from "@/lib/utils";
import { BioPage } from "@/services/bioPageService";
import { motion } from "framer-motion";
import {
    LayoutDashboard,
    LayoutGrid,
    Link as LinkIcon,
    Loader2,
    Save,
    Settings2,
    Sparkles,
    TrendingUp,
    UserCircle,
    Smartphone
} from "lucide-react";
import { useEffect, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

interface EditorLayoutProps {
    initialData: BioPage;
}

export function EditorLayout({ initialData }: EditorLayoutProps) {
    const {
        data,
        bioPageWithTheme,
        isLoading,
        isSaving,
        hasUnsavedChanges,
        updateField,
        updateTheme,
        resetTheme,
        save,
        refresh,
        setData // To force refresh links
    } = useBioPageEditor(initialData);

    // Create form context for SEO settings and other form-based components
    const form = useForm({
        defaultValues: {
            username: data.username,
            title: data.title || '',
            bioText: data.bioText || '',
            avatarUrl: data.avatarUrl || '',
            themeConfig: data.themeConfig || '',
            seoMeta: data.seoMeta || '',
            isPublic: data.isPublic ?? true,
            coverUrl: data.coverUrl || '',
        }
    });

    // Update form values when data changes
    useEffect(() => {
        form.reset({
            username: data.username,
            title: data.title || '',
            bioText: data.bioText || '',
            avatarUrl: data.avatarUrl || '',
            themeConfig: data.themeConfig || '',
            seoMeta: data.seoMeta || '',
            isPublic: data.isPublic ?? true,
            coverUrl: data.coverUrl || '',
        });
    }, [data.username, data.title, data.bioText, data.avatarUrl, data.themeConfig, data.seoMeta, data.isPublic, data.coverUrl, form.reset]);

    // When links are updated in BioLinkManager, we might want to refresh 'data'
    // But BioLinkManager updates backend directly.
    // We should refresh 'data.links' so preview updates too.
    // We can pass a callback that re-fetches or manually updates local state.
    // For now, let's assume BioLinkManager calls a refresh callback.

    const handleLinksUpdate = async () => {
        // Re-fetch data? Or optimistic update?
        // Since BioLinkManager modifies backend, we should re-fetch to sync.
        // Or modify local state if BioLinkManager returns new list.
        // Let's implement basic re-fetch logic or just reload page for now? No, SPA.
        // We can use SWR or React Query, but we are using manual fetch in hook.

        // Temporary solution: force reload or parent refresh.
        // For now, we will add a 'refresh' method to hook or just window.location.reload() (bad UX).
        // Ideally, BioLinkManager should return updated links or we fetch them.
        // Let's stick to simplest: pass a fetcher.
    };

    return (
        <FormProvider {...form}>
            <div className="flex h-screen w-full overflow-hidden bg-gradient-to-br from-background via-background to-muted/20">
                {/* Left Panel: Controls */}
                <div className="w-full md:w-[70%] lg:w-[65%] xl:w-[750px] 2xl:w-[950px] flex flex-col border-r bg-card/40 backdrop-blur-md shadow-xl relative">

                    {/* Top Save Action Bar */}
                    <motion.div
                        initial={{ y: -20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        className="m-2 md:mx-4 lg:mx-6 mb-0 z-50 flex items-center justify-between py-1.5 px-3 bg-card/95 backdrop-blur-xl border border-border/50 shadow-sm transition-shadow duration-300 rounded-lg flex-shrink-0"
                    >
                        <div className="flex items-center gap-2.5">
                            <div className="relative flex h-2 w-2">
                                {hasUnsavedChanges && (
                                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-amber-400 opacity-75"></span>
                                )}
                                <span className={cn(
                                    "relative inline-flex rounded-full h-2 w-2 transition-colors",
                                    hasUnsavedChanges ? "bg-amber-500" : "bg-emerald-500"
                                )}></span>
                            </div>
                            <div className="flex flex-col">
                                <span className="font-semibold text-xs sm:text-[13px] leading-none flex items-center gap-1.5 sm:gap-2">
                                    {hasUnsavedChanges ? 'Unsaved Changes' : 'All Changes Saved'}
                                </span>
                                <span className="text-[9px] sm:text-[10px] text-muted-foreground leading-none mt-0.5">
                                    {hasUnsavedChanges ? 'Don\'t forget to publish' : 'Your page is live'}
                                </span>
                            </div>
                        </div>
                        <Button
                            onClick={save}
                            disabled={!hasUnsavedChanges || isSaving}
                            size="sm"
                            className={cn(
                                "h-7 rounded-md text-[11px] font-semibold px-3 transition-all duration-300",
                                hasUnsavedChanges
                                    ? "bg-primary text-primary-foreground shadow-sm hover:shadow hover:-translate-y-0.5"
                                    : "bg-muted text-muted-foreground"
                            )}
                        >
                            {isSaving ? <Loader2 className="mr-1 h-3 w-3 animate-spin" /> : <Save className="mr-1 h-3 w-[14px]" />}
                            <span className="hidden sm:inline">{isSaving ? 'Publishing...' : 'Publish'}</span>
                        </Button>

                        {/* Mobile Preview Button */}
                        <Sheet>
                            <SheetTrigger asChild>
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="md:hidden h-7 rounded-md text-[11px] font-semibold px-3 ml-2 border-primary/20 text-primary hover:bg-primary/5 shadow-sm"
                                >
                                    <Smartphone className="mr-1 h-3 w-3" />
                                    Preview
                                </Button>
                            </SheetTrigger>
                            <SheetContent side="bottom" className="h-[95vh] px-0 pt-6 pb-0 flex flex-col bg-muted/30 backdrop-blur-3xl border-t border-border/50 rounded-t-3xl sm:max-w-none">
                                <SheetTitle className="sr-only">Live Preview</SheetTitle>
                                <div className="flex-1 w-full relative overflow-hidden flex flex-col items-center justify-center">
                                    <PreviewPanel page={bioPageWithTheme} className="h-full w-full max-w-sm mx-auto shadow-2xl" />

                                    {/* Quick Link Share Helper (Mobile inside Sheet) */}
                                    <motion.div
                                        initial={{ opacity: 0, y: 20 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        className="absolute bottom-6 z-50 flex items-center gap-3 px-5 py-2.5 bg-background/90 backdrop-blur-md rounded-full border shadow-lg"
                                    >
                                        <span className="text-xs font-medium text-foreground">zap.link/{data.username}</span>
                                        <Button size="icon" variant="ghost" className="h-7 w-7 rounded-full bg-primary/10 hover:bg-primary/20" onClick={() => {
                                            navigator.clipboard.writeText(`https://zap.me/${data.username}`);
                                        }}>
                                            <LinkIcon className="h-3.5 w-3.5 text-primary" />
                                        </Button>
                                    </motion.div>
                                </div>
                            </SheetContent>
                        </Sheet>
                    </motion.div>

                    <Tabs defaultValue="profile" className="flex-1 flex flex-col overflow-hidden h-full">
                        {/* Premium Floating Tabs Navigation */}
                        <div className="px-4 py-2 md:px-6 md:py-2 lg:px-8 z-10 w-full">
                            <TabsList className="flex w-full overflow-x-auto no-scrollbar bg-background/60 backdrop-blur-xl border shadow-sm p-1.5 rounded-xl gap-1">
                                <TabsTrigger value="profile" className="flex-1 rounded-lg text-xs sm:text-sm gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-md transition-all duration-300 py-2.5">
                                    <UserCircle className="w-4 h-4" />
                                    <span className="hidden sm:inline">Profile</span>
                                </TabsTrigger>
                                <TabsTrigger value="links" className="flex-1 rounded-lg text-xs sm:text-sm gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-md transition-all duration-300 py-2.5">
                                    <LayoutGrid className="w-4 h-4" />
                                    <span className="hidden sm:inline">Links</span>
                                </TabsTrigger>
                                <TabsTrigger value="design" className="flex-1 rounded-lg text-xs sm:text-sm gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-md transition-all duration-300 py-2.5">
                                    <Sparkles className="w-4 h-4" />
                                    <span className="hidden md:inline">Design</span>
                                </TabsTrigger>
                                <TabsTrigger value="layout" className="flex-1 rounded-lg text-xs sm:text-sm gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-md transition-all duration-300 py-2.5">
                                    <LayoutDashboard className="w-4 h-4" />
                                    <span className="hidden md:inline">Layout</span>
                                </TabsTrigger>
                                <TabsTrigger value="settings" className="flex-1 rounded-lg text-xs sm:text-sm gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-md transition-all duration-300 py-2.5">
                                    <Settings2 className="w-4 h-4" />
                                    <span className="hidden md:inline">SEO</span>
                                </TabsTrigger>
                                <TabsTrigger value="analytics" className="flex-1 rounded-lg text-xs sm:text-sm gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-md transition-all duration-300 py-2.5">
                                    <TrendingUp className="w-4 h-4" />
                                    <span className="hidden lg:inline">Stats</span>
                                </TabsTrigger>
                            </TabsList>
                        </div>

                        {/* Scrollable Content Area */}
                        <div className="flex-1 overflow-y-auto px-4 md:px-6 lg:px-8 scroll-smooth mobile-scrollbar pr-2">
                            <TabsContent value="profile" className="m-0 h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                                <ProfileTab data={data} updateField={updateField} />
                            </TabsContent>

                            <TabsContent value="links" className="m-0 h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                                <BioLinkManager
                                    pageId={data.id}
                                    links={data.bioLinks || []}
                                    onLinksUpdate={() => {
                                        refresh();
                                    }}
                                    onLinksReorder={(newLinks) => {
                                        updateField('bioLinks', newLinks);
                                    }}
                                />
                            </TabsContent>

                            <TabsContent value="design" className="m-0 h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                                <ThemeEditor
                                    theme={bioPageWithTheme.parsedTheme}
                                    onChange={updateTheme}
                                />
                            </TabsContent>

                            <TabsContent value="settings" className="m-0 h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                                <SeoSettingsTab />
                            </TabsContent>

                            <TabsContent value="analytics" className="m-0 h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                                <AnalyticsTab bioPage={data} />
                            </TabsContent>

                            <TabsContent value="layout" className="m-0 h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                                <LayoutSelector
                                    theme={bioPageWithTheme.parsedTheme}
                                    onChange={updateTheme}
                                />
                            </TabsContent>
                        </div>
                    </Tabs>

                </div>

                {/* Right Panel: Live Preview */}
                <div className="hidden md:flex flex-1 flex-col bg-muted/10 relative">
                    <PreviewPanel page={bioPageWithTheme} className="h-full w-full" />

                    {/* Quick Link Share Helper */}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: 0.3 }}
                        className="absolute bottom-6 right-6 z-50 flex items-center gap-3 px-6 py-3 bg-background/80 backdrop-blur-md rounded-full border shadow-sm"
                    >
                        <span className="text-sm font-medium text-muted-foreground">zap.link/{data.username}</span>
                        <Button size="icon" variant="ghost" className="h-8 w-8 rounded-full" onClick={() => {
                            navigator.clipboard.writeText(`https://zap.me/${data.username}`);
                        }}>
                            <LinkIcon className="h-4 w-4" />
                        </Button>
                    </motion.div>
                </div>
            </div>
        </FormProvider>
    );
}
