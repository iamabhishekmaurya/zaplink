"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
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
    UserCircle
} from "lucide-react";
import { useEffect } from "react";
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
            <div className="flex h-[calc(105vh-4rem)] w-full overflow-hidden bg-gradient-to-br from-background via-background to-muted/20">
                {/* Left Panel: Controls - Increased Width */}
                <div className="w-full md:w-[55%] lg:w-[50%] xl:w-[520px] 2xl:w-[580px] flex flex-col border-r bg-card/50 backdrop-blur-sm shadow-xl">

                    {/* Header / Save Bar - Modern Glass Effect */}
                    <div className="flex items-center justify-between p-4 border-b bg-gradient-to-r from-card via-card to-muted/30 backdrop-blur-md sticky top-0 z-10 shadow-sm">
                        <div className="flex items-center gap-3">
                            <div className={cn(
                                "w-3 h-3 rounded-full animate-pulse",
                                hasUnsavedChanges ? 'bg-amber-500' : 'bg-emerald-500'
                            )} />
                            <div className="flex flex-col">
                                <span className="font-semibold text-sm">{hasUnsavedChanges ? 'Unsaved Changes' : 'All Saved'}</span>
                                <span className="text-xs text-muted-foreground">{hasUnsavedChanges ? 'Press Ctrl+S to save' : 'Your page is live'}</span>
                            </div>
                        </div>
                        <Button
                            onClick={save}
                            disabled={!hasUnsavedChanges || isSaving}
                            size="sm"
                            className={cn(
                                "transition-all duration-300",
                                hasUnsavedChanges && "bg-primary hover:bg-primary/90 text-primary-foreground shadow-lg hover:shadow-xl"
                            )}
                        >
                            {isSaving ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <Save className="mr-2 h-4 w-4" />}
                            {isSaving ? 'Saving...' : 'Save Changes'}
                        </Button>
                    </div>

                    <Tabs defaultValue="profile" className="flex-1 flex flex-col overflow-hidden">
                        <div className="px-4 py-3 border-b bg-gradient-to-b from-card to-muted/20">
                            <TabsList className="grid w-full grid-cols-6 p-1 bg-muted/50">
                                <TabsTrigger value="profile" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-background data-[state=active]:shadow-sm">
                                    <UserCircle className="w-4 h-4" />
                                    <span className="hidden sm:inline">Profile</span>
                                </TabsTrigger>
                                <TabsTrigger value="links" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-background data-[state=active]:shadow-sm">
                                    <LayoutGrid className="w-4 h-4" />
                                    <span className="hidden sm:inline">Links</span>
                                </TabsTrigger>
                                <TabsTrigger value="design" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-background data-[state=active]:shadow-sm">
                                    <Sparkles className="w-4 h-4" />
                                    <span className="hidden sm:inline">Design</span>
                                </TabsTrigger>
                                <TabsTrigger value="layout" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-background data-[state=active]:shadow-sm">
                                    <LayoutDashboard className="w-4 h-4" />
                                    <span className="hidden sm:inline">Layout</span>
                                </TabsTrigger>
                                <TabsTrigger value="settings" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-background data-[state=active]:shadow-sm">
                                    <Settings2 className="w-4 h-4" />
                                    <span className="hidden sm:inline">SEO</span>
                                </TabsTrigger>
                                <TabsTrigger value="analytics" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-background data-[state=active]:shadow-sm">
                                    <TrendingUp className="w-4 h-4" />
                                    <span className="hidden sm:inline">Stats</span>
                                </TabsTrigger>
                            </TabsList>
                        </div>

                        <div className="flex-1 overflow-y-auto p-5 md:p-7 space-y-6">
                            <TabsContent value="profile" className="m-0 h-full">
                                <ProfileTab data={data} updateField={updateField} />
                            </TabsContent>

                            <TabsContent value="links" className="m-0 h-full">
                                <BioLinkManager
                                    pageId={data.id}
                                    links={data.bioLinks || []}
                                    onLinksUpdate={() => {
                                        refresh();
                                    }}
                                />
                            </TabsContent>

                            <TabsContent value="design" className="m-0 h-full">
                                <ThemeEditor
                                    theme={bioPageWithTheme.parsedTheme}
                                    onChange={updateTheme}
                                />
                            </TabsContent>

                            <TabsContent value="settings" className="m-0 h-full">
                                <SeoSettingsTab />
                            </TabsContent>

                            <TabsContent value="analytics" className="m-0 h-full">
                                <AnalyticsTab bioPage={data} />
                            </TabsContent>

                            <TabsContent value="layout" className="m-0 h-full">
                                <LayoutSelector
                                    theme={bioPageWithTheme.parsedTheme}
                                    onChange={updateTheme}
                                />
                            </TabsContent>
                        </div>
                    </Tabs>
                </div>

                {/* Right Panel: Preview */}
                <div className="hidden md:flex flex-1 flex-col bg-muted/30 relative">
                    <PreviewPanel page={bioPageWithTheme} className="h-full w-full" />
                </div>
            </div>
        </FormProvider>
    );
}
