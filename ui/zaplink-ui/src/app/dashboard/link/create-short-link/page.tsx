'use client'

import { useState, useEffect } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { ArrowLeft, Loader2, Link2, Globe, Tag, Sparkles, Youtube, Github, Twitter, Linkedin, Instagram, Facebook, Link } from 'lucide-react'
import { IconBrandYoutube } from '@tabler/icons-react'
import { useShortlinks } from '@/hooks/useShortlinks'
import { extractTitleFromUrl, extractPlatformFromUrl } from '@/lib/api/shortlinkService'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import * as z from 'zod'

const formSchema = z.object({
    originalUrl: z.string().url('Please enter a valid URL'),
    title: z.string().optional(),
    platform: z.string().optional(),
    tags: z.string().optional()
})

type FormValues = z.infer<typeof formSchema>

const CreateShortLink = () => {
    const router = useRouter()
    const searchParams = useSearchParams()
    const { createShortlink, getShortlink, updateShortlink } = useShortlinks()
    
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [isEditMode, setIsEditMode] = useState(false)
    const [initialLoading, setInitialLoading] = useState(false)

    const form = useForm<FormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            originalUrl: '',
            title: '',
            platform: '',
            tags: ''
        }
    })

    useEffect(() => {
        const editMode = searchParams.get('edit') === 'true'
        const linkId = searchParams.get('id')
        
        if (editMode && linkId) {
            setIsEditMode(true)
            loadLinkData(linkId)
        }
    }, [searchParams])

    const loadLinkData = async (linkId: string) => {
        try {
            setInitialLoading(true)
            const link = await getShortlink(linkId)
            if (link) {
                form.reset({
                    originalUrl: link.originalUrl,
                    title: link.title || '',
                    platform: link.platform || '',
                    tags: link.tags?.join(', ') || ''
                })
            }
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to load link data')
        } finally {
            setInitialLoading(false)
        }
    }

    const handleUrlChange = (value: string) => {
        form.setValue('originalUrl', value)
        
        // Auto-extract title and platform when URL changes
        if (value) {
            try {
                const title = extractTitleFromUrl(value)
                const platform = extractPlatformFromUrl(value)
                
                // Only set if fields are empty
                if (!form.getValues('title')) {
                    form.setValue('title', title)
                }
                if (!form.getValues('platform')) {
                    form.setValue('platform', platform)
                }
            } catch (err) {
                // URL is invalid, don't auto-populate
            }
        }
    }

    const onSubmit = async (values: FormValues) => {
        try {
            setLoading(true)
            setError(null)
            
            const tagsArray = values.tags
                ? values.tags.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0)
                : []
            
            const linkData = {
                title: values.title || extractTitleFromUrl(values.originalUrl),
                originalUrl: values.originalUrl,
                platform: values.platform || extractPlatformFromUrl(values.originalUrl),
                tags: tagsArray
            }
            
            if (isEditMode) {
                const linkId = searchParams.get('id')
                if (linkId) {
                    await updateShortlink(linkId, linkData)
                }
            } else {
                await createShortlink(linkData)
            }
            
            // Navigate back to short links page after successful operation
            router.push('/dashboard/link/short-link')
        } catch (err: any) {
            setError(err.response?.data?.message || `Failed to ${isEditMode ? 'update' : 'create'} short link`)
        } finally {
            setLoading(false)
        }
    }

    const handleBack = () => {
        router.push('/dashboard/link/short-link')
    }

    const platforms = [
        { value: 'YouTube', label: 'YouTube', icon: IconBrandYoutube },
        { value: 'GitHub', label: 'GitHub', icon: Github },
        { value: 'Twitter', label: 'Twitter', icon: Twitter },
        { value: 'LinkedIn', label: 'LinkedIn', icon: Linkedin },
        { value: 'Instagram', label: 'Instagram', icon: Instagram },
        { value: 'Facebook', label: 'Facebook', icon: Facebook },
        { value: 'Website', label: 'Website', icon: Globe },
        { value: 'Portfolio', label: 'Portfolio', icon: Globe },
        { value: 'Blog', label: 'Blog', icon: Globe },
        { value: 'Other', label: 'Other', icon: Link }
    ] as const

    if (initialLoading) {
        return (
            <div className="min-h-screen bg-background">
                <div className="container mx-auto px-4 py-8 max-w-4xl">
                    <div className="flex items-center justify-center min-h-[400px]">
                        <div className="flex items-center gap-3">
                            <Loader2 className="h-6 w-6 animate-spin" />
                            <span className="text-lg">Loading link data...</span>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className="min-h-screen bg-background">
            <div className="container mx-auto px-4 py-8 max-w-4xl">
                {/* Header */}
                <div className="mb-8">
                    <Button
                        variant="ghost"
                        size="sm"
                        onClick={handleBack}
                        className="mb-4"
                    >
                        <ArrowLeft className="h-4 w-4 mr-2" />
                        Back to Short Links
                    </Button>
                    
                    <div className="text-center">
                        <div className="inline-flex items-center justify-center w-16 h-16 bg-primary rounded-2xl mb-4">
                            <Link2 className="h-8 w-8 text-primary-foreground" />
                        </div>
                        <h1 className="text-4xl font-bold tracking-tight mb-2">
                            {isEditMode ? 'Edit Short Link' : 'Create Short Link'}
                        </h1>
                        <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                            {isEditMode 
                                ? 'Update your short link details and settings'
                                : 'Transform your long URLs into short, memorable links that are easy to share and track'
                            }
                        </p>
                    </div>
                </div>

                {/* Main Content */}
                <div className="grid lg:grid-cols-3 gap-8">
                    {/* Form Section */}
                    <div className="lg:col-span-2">
                        <Card className="shadow-lg">
                            <CardHeader className="pb-4">
                                <CardTitle className="flex items-center gap-2 text-xl">
                                    <Sparkles className="h-5 w-5 text-primary" />
                                    Link Details
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-6">
                                <Form {...form}>
                                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                                        {/* Original URL - Main Field */}
                                        <FormField
                                            control={form.control}
                                            name="originalUrl"
                                            render={({ field }) => (
                                                <FormItem className="space-y-3">
                                                    <FormLabel className="text-base font-semibold flex items-center gap-2">
                                                        <Globe className="h-4 w-4" />
                                                        Original URL <span className="text-destructive">*</span>
                                                    </FormLabel>
                                                    <FormControl>
                                                        <Input
                                                            placeholder="https://example.com/your-very-long-url-that-needs-shortening"
                                                            className="h-12 text-base border-2 focus:border-primary focus:ring-primary/20"
                                                            {...field}
                                                            onChange={(e) => handleUrlChange(e.target.value)}
                                                        />
                                                    </FormControl>
                                                    <FormDescription className="text-sm">
                                                        Enter the full URL you want to shorten
                                                    </FormDescription>
                                                    <FormMessage />
                                                </FormItem>
                                            )}
                                        />

                                        {/* Title and Platform in Grid */}
                                        <div className="grid md:grid-cols-2 gap-6">
                                            {/* Title */}
                                            <FormField
                                                control={form.control}
                                                name="title"
                                                render={({ field }) => (
                                                    <FormItem className="space-y-3">
                                                        <FormLabel className="text-base font-semibold">
                                                            Title
                                                        </FormLabel>
                                                        <FormControl>
                                                            <Input
                                                                placeholder="My Awesome Link"
                                                                className="h-11 border-2 focus:border-primary focus:ring-primary/20"
                                                                {...field}
                                                            />
                                                        </FormControl>
                                                        <FormDescription className="text-sm">
                                                            Auto-populated from URL
                                                        </FormDescription>
                                                        <FormMessage />
                                                    </FormItem>
                                                )}
                                            />

                                            {/* Platform */}
                                            <FormField
                                                control={form.control}
                                                name="platform"
                                                render={({ field }) => (
                                                    <FormItem className="space-y-3">
                                                        <FormLabel className="text-base font-semibold">
                                                            Platform
                                                        </FormLabel>
                                                        <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                            <FormControl>
                                                                <SelectTrigger className="h-11 border-2 focus:border-primary focus:ring-primary/20">
                                                                    <SelectValue placeholder="Select platform" />
                                                                </SelectTrigger>
                                                            </FormControl>
                                                            <SelectContent>
                                                                {platforms.map((platform) => {
                                                                    const Icon = platform.icon
                                                                    return (
                                                                        <SelectItem key={platform.value} value={platform.value}>
                                                                            <div className="flex items-center gap-2">
                                                                                <Icon className="h-4 w-4" />
                                                                                <span>{platform.label}</span>
                                                                            </div>
                                                                        </SelectItem>
                                                                    )
                                                                })}
                                                            </SelectContent>
                                                        </Select>
                                                        <FormDescription className="text-sm">
                                                            Auto-detected from URL
                                                        </FormDescription>
                                                        <FormMessage />
                                                    </FormItem>
                                                )}
                                            />
                                        </div>

                                        {/* Tags */}
                                        <FormField
                                            control={form.control}
                                            name="tags"
                                            render={({ field }) => (
                                                <FormItem className="space-y-3">
                                                    <FormLabel className="text-base font-semibold flex items-center gap-2">
                                                        <Tag className="h-4 w-4" />
                                                        Tags
                                                    </FormLabel>
                                                    <FormControl>
                                                        <Textarea
                                                            placeholder="marketing, social, campaign, summer2024"
                                                            className="min-h-[100px] resize-none border-2 focus:border-primary focus:ring-primary/20"
                                                            {...field}
                                                        />
                                                    </FormControl>
                                                    <FormDescription className="text-sm">
                                                        Add tags to organize your links (separate with commas)
                                                    </FormDescription>
                                                    <FormMessage />
                                                </FormItem>
                                            )}
                                        />

                                        {/* Error Message */}
                                        {error && (
                                            <div className="p-4 text-sm text-destructive bg-destructive/10 border border-destructive/20 rounded-lg">
                                                {error}
                                            </div>
                                        )}

                                        {/* Action Buttons */}
                                        <div className="flex gap-4 pt-6">
                                            <Button
                                                type="button"
                                                variant="outline"
                                                onClick={handleBack}
                                                disabled={loading}
                                                className="h-12 px-8 border-2"
                                            >
                                                Cancel
                                            </Button>
                                            <Button
                                                type="submit"
                                                disabled={loading}
                                                className="h-12 px-8 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold shadow-lg hover:shadow-xl transition-all duration-200"
                                            >
                                                {loading ? (
                                                    <>
                                                        <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                                                        {isEditMode ? 'Updating...' : 'Creating...'}
                                                    </>
                                                ) : (
                                                    <>
                                                        <Sparkles className="h-4 w-4 mr-2" />
                                                        {isEditMode ? 'Update Short Link' : 'Create Short Link'}
                                                    </>
                                                )}
                                            </Button>
                                        </div>
                                    </form>
                                </Form>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Side Panel */}
                    <div className="lg:col-span-1 space-y-6">
                        {/* Features Card */}
                        <Card className="shadow-lg bg-muted/50">
                            <CardContent className="p-6">
                                <h3 className="font-semibold text-lg mb-4">
                                    ✨ Why Short Links?
                                </h3>
                                <div className="space-y-3 text-sm text-muted-foreground">
                                    <div className="flex items-start gap-3">
                                        <div className="w-2 h-2 bg-primary rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Share memorable links that are easy to remember</p>
                                    </div>
                                    <div className="flex items-start gap-3">
                                        <div className="w-2 h-2 bg-chart-2 rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Track clicks and analyze performance</p>
                                    </div>
                                    <div className="flex items-start gap-3">
                                        <div className="w-2 h-2 bg-chart-3 rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Custom branding with your domain</p>
                                    </div>
                                    <div className="flex items-start gap-3">
                                        <div className="w-2 h-2 bg-chart-4 rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Organize with tags and categories</p>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>

                        {/* Tips Card */}
                        <Card className="shadow-lg bg-muted/30">
                            <CardContent className="p-6">
                                <h3 className="font-semibold text-lg mb-4">
                                    💡 Pro Tips
                                </h3>
                                <div className="space-y-3 text-sm text-muted-foreground">
                                    <div className="flex items-start gap-3">
                                        <span className="text-lg">🎯</span>
                                        <p>Use descriptive titles for easy identification</p>
                                    </div>
                                    <div className="flex items-start gap-3">
                                        <span className="text-lg">🏷️</span>
                                        <p>Add relevant tags for better organization</p>
                                    </div>
                                    <div className="flex items-start gap-3">
                                        <span className="text-lg">📊</span>
                                        <p>Enable analytics to track performance</p>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CreateShortLink
