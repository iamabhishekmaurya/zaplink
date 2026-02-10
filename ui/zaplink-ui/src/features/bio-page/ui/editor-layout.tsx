"use client"

import { motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import { FormProvider, useForm } from "react-hook-form";
import { useEffect } from "react";
import { useBioPageEditor } from "@/features/bio-page/hooks/useBioPageEditor";
import { BioLinkManager } from "@/features/bio-page/ui/bio-link-manager";
import { PreviewPanel } from "@/features/bio-page/ui/preview-panel/index";
import { ThemeEditor } from "@/features/bio-page/ui/theme-editor/index";
import { SeoSettingsTab } from "@/features/bio-page/ui/seo-settings-tab";
import { AvatarUpload } from "@/features/bio-page/ui/avatar-upload";
import { AnalyticsTab } from "@/features/bio-page/ui/analytics-tab";
import { BioPage } from "@/services/bioPageService";
import { 
  BarChart, 
  Link as LinkIcon, 
  Loader2, 
  Palette, 
  Save, 
  Search, 
  UserCircle,
  Sparkles,
  LayoutGrid,
  Settings2,
  TrendingUp
} from "lucide-react";
import { cn } from "@/lib/utils";

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
            <div className="flex h-[calc(100vh-4rem)] w-full overflow-hidden bg-gradient-to-br from-background via-background to-muted/20">
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
                                hasUnsavedChanges && "bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-700 hover:to-indigo-700 shadow-lg hover:shadow-xl"
                            )}
                        >
                            {isSaving ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <Save className="mr-2 h-4 w-4" />}
                            {isSaving ? 'Saving...' : 'Save Changes'}
                        </Button>
                    </div>

                    <Tabs defaultValue="links" className="flex-1 flex flex-col overflow-hidden">
                        <div className="px-4 py-3 border-b bg-gradient-to-b from-card to-muted/20">
                            <TabsList className="grid w-full grid-cols-4 p-1 bg-muted/50">
                                <TabsTrigger value="links" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-white data-[state=active]:shadow-sm">
                                    <LayoutGrid className="w-4 h-4" />
                                    <span className="hidden sm:inline">Links</span>
                                </TabsTrigger>
                                <TabsTrigger value="design" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-white data-[state=active]:shadow-sm">
                                    <Sparkles className="w-4 h-4" />
                                    <span className="hidden sm:inline">Design</span>
                                </TabsTrigger>
                                <TabsTrigger value="settings" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-white data-[state=active]:shadow-sm">
                                    <Settings2 className="w-4 h-4" />
                                    <span className="hidden sm:inline">SEO</span>
                                </TabsTrigger>
                                <TabsTrigger value="analytics" className="text-xs sm:text-sm gap-2 data-[state=active]:bg-white data-[state=active]:shadow-sm">
                                    <TrendingUp className="w-4 h-4" />
                                    <span className="hidden sm:inline">Stats</span>
                                </TabsTrigger>
                            </TabsList>
                        </div>

                        <div className="flex-1 overflow-y-auto p-5 md:p-7 space-y-6">
                            <TabsContent value="links" className="m-0 h-full space-y-6">
                                {/* Modern Profile Card */}
                                <motion.div 
                                    initial={{ opacity: 0, y: 10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="space-y-5 bg-gradient-to-br from-white to-muted/30 border rounded-xl p-5 shadow-sm hover:shadow-md transition-shadow"
                                >
                                    <div className="flex items-center gap-2 pb-3 border-b border-border/50">
                                        <div className="p-2 bg-violet-100 rounded-lg">
                                            <UserCircle className="w-5 h-5 text-violet-600" />
                                        </div>
                                        <Label className="text-lg font-semibold bg-gradient-to-r from-violet-600 to-indigo-600 bg-clip-text text-transparent">
                                            Profile Information
                                        </Label>
                                    </div>
                                    
                                    <div className="grid gap-5">
                                        <div className="space-y-2.5">
                                            <Label htmlFor="title" className="text-sm font-medium text-foreground/80 flex items-center gap-2">
                                                Page Title
                                                <span className="text-xs text-muted-foreground">(Shown at top of your page)</span>
                                            </Label>
                                            <Input
                                                id="title"
                                                value={data.title || ''}
                                                onChange={(e) => updateField('title', e.target.value)}
                                                placeholder="@username or Your Name"
                                                className="h-11 transition-all focus:ring-2 focus:ring-violet-500/20"
                                            />
                                        </div>
                                        
                                        <div className="space-y-2.5">
                                            <Label htmlFor="bio" className="text-sm font-medium text-foreground/80 flex items-center gap-2">
                                                Bio
                                                <span className="text-xs text-muted-foreground">(Tell visitors about yourself)</span>
                                            </Label>
                                            <Textarea
                                                id="bio"
                                                value={data.bioText || ''}
                                                onChange={(e) => updateField('bioText', e.target.value)}
                                                placeholder="Share your story, what you do, or what visitors can find here..."
                                                rows={4}
                                                className="resize-none transition-all focus:ring-2 focus:ring-violet-500/20"
                                            />
                                        </div>
                                        
                                        {/* Avatar Upload - Integrated Better */}
                                        <div className="pt-2">
                                            <AvatarUpload
                                                currentAvatar={data.avatarUrl}
                                                onAvatarChange={(url) => updateField('avatarUrl', url)}
                                            />
                                        </div>
                                    </div>
                                </motion.div>

                                {/* Links Manager - Styled Container */}
                                <motion.div
                                    initial={{ opacity: 0, y: 10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    transition={{ delay: 0.1 }}
                                >
                                    <div className="flex items-center gap-2 mb-4">
                                        <div className="p-2 bg-indigo-100 rounded-lg">
                                            <LinkIcon className="w-5 h-5 text-indigo-600" />
                                        </div>
                                        <Label className="text-lg font-semibold bg-gradient-to-r from-indigo-600 to-violet-600 bg-clip-text text-transparent">
                                            Your Links
                                        </Label>
                                    </div>
                                    <BioLinkManager
                                        pageId={data.id}
                                        links={data.bioLinks || []}
                                        onLinksUpdate={() => {
                                            refresh();
                                        }}
                                    />
                                </motion.div>
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
