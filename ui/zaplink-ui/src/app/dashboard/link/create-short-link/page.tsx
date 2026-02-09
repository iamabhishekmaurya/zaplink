'use client'

import { SmartRoutingRules } from '@/features/smart-routing/ui/SmartRoutingRules'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { TagInput } from '@/components/ui/tag-input'
import { shortlinkService } from '@/services/shortlinkService'
import { extractPlatformFromUrl, extractTitleFromUrl } from '@/services/shortlinkService'
import { RedirectRuleDto } from '@/lib/types/apiRequestType'
import { zodResolver } from '@hookform/resolvers/zod'
import { IconBrandYoutube } from '@tabler/icons-react'
import { ArrowLeft, Facebook, Github, Globe, Instagram, Link, Link2, Linkedin, Loader2, RefreshCw, Sparkles, Tag, Twitter } from 'lucide-react'
import { useRouter, useSearchParams } from 'next/navigation'
import { Suspense, useEffect, useState, useRef } from 'react'
import { useForm } from 'react-hook-form'
import * as z from 'zod'

const formSchema = z.object({
    originalUrl: z.string().url('Please enter a valid URL'),
    title: z.string().optional(),
    platform: z.string().optional(),
    tags: z.array(z.string()).optional(),
    rules: z.array(z.any()).optional() // Using any temporarily to bypass deeper zod validation for the DTO
})

type FormValues = z.infer<typeof formSchema>

const CreateShortLinkContent = () => {
    const router = useRouter()
    const searchParams = useSearchParams()

    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [initialLoading, setInitialLoading] = useState(false)
    const [isEditMode, setIsEditMode] = useState(false)
    const [loadedLink, setLoadedLink] = useState<any>(null) // Store loaded link data for updates
    const hasLoadedLink = useRef(false) // Track if link has been loaded to prevent duplicate calls

    const form = useForm<FormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            originalUrl: '',
            title: '',
            platform: '',
            tags: [],
            rules: []
        }
    })

    useEffect(() => {
        const editMode = searchParams.get('edit') === 'true'
        const linkId = searchParams.get('id')

        if (editMode && linkId && !hasLoadedLink.current) {
            setIsEditMode(true)
            hasLoadedLink.current = true
            loadLinkData(linkId)
        }
    }, [searchParams])

    const loadLinkData = async (linkId: string) => {
        try {
            setInitialLoading(true)
            const link = await shortlinkService.getShortlink(linkId)
            
            // Store the loaded link data
            setLoadedLink(link)

            if (link) {
                let parsedRules = link.rules || []

                if (typeof parsedRules === 'string') {
                    try {
                        parsedRules = JSON.parse(parsedRules)
                    } catch (e) {
                        parsedRules = []
                    }
                }

                form.reset({
                    originalUrl: link.originalUrl,
                    title: link.title || '',
                    platform: link.platform || '',
                    tags: link.tags || [],
                    rules: parsedRules
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


            const linkData = {
                title: values.title || extractTitleFromUrl(values.originalUrl),
                originalUrl: values.originalUrl,
                platform: values.platform || extractPlatformFromUrl(values.originalUrl),
                tags: values.tags || [],
                rules: values.rules as RedirectRuleDto[]
            }

            if (isEditMode) {
                const linkId = searchParams.get('id')
                if (linkId && loadedLink) {
                    await shortlinkService.updateShortLink(linkId, {
                        ...linkData,
                        shortUrlKey: loadedLink.shortUrlKey // Pass the stored shortUrlKey
                    })
                }
            } else {
                await shortlinkService.createShortLink(linkData)
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
        <div className="min-h-screen bg-background p-4 lg:p-8">
            <div className="max-w-7xl mx-auto space-y-8">
                {/* Header */}
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                    <div>
                        <h1 className="text-3xl font-bold tracking-tight">
                            {isEditMode ? 'Edit Short Link' : 'Create Short Link'}
                        </h1>
                        <p className="text-muted-foreground mt-1">
                            {isEditMode
                                ? 'Update your short link details and settings.'
                                : 'Transform long URLs into memorable short links.'
                            }
                        </p>
                    </div>
                    <div className="flex gap-2">
                        <Button variant="outline" onClick={handleBack} className="flex items-center gap-2">
                            <ArrowLeft className="h-4 w-4" /> Back to Links
                        </Button>
                        <Button variant="outline" onClick={() => form.reset()} className="flex items-center gap-2">
                            <RefreshCw className="h-4 w-4" /> Reset
                        </Button>
                    </div>
                </div>

                <div className="grid lg:grid-cols-12 gap-8 items-start">
                    {/* Left Panel: Form */}
                    <div className="lg:col-span-7 xl:col-span-8 space-y-6">
                        <Form {...form}>
                            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
                                <Card className="border-border/50 bg-background/50 backdrop-blur-sm shadow-sm">
                                    <CardHeader>
                                        <CardTitle className="flex items-center gap-2">
                                            <Link2 className="h-5 w-5 text-primary" /> Link Details
                                        </CardTitle>
                                    </CardHeader>
                                    <CardContent className="space-y-6">
                                        {/* Original URL */}
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
                                                            placeholder="https://example.com/long-url"
                                                            className="h-16 text-base border-input/50 focus:border-primary/50 bg-background/50"
                                                            {...field}
                                                            onChange={(e) => handleUrlChange(e.target.value)}
                                                        />
                                                    </FormControl>
                                                    <FormMessage />
                                                </FormItem>
                                            )}
                                        />

                                        <div className="grid md:grid-cols-2 gap-6">
                                            {/* Title */}
                                            <FormField
                                                control={form.control}
                                                name="title"
                                                render={({ field }) => (
                                                    <FormItem className="space-y-3">
                                                        <FormLabel className="text-base font-semibold">Title</FormLabel>
                                                        <FormControl>
                                                            <Input
                                                                placeholder="My Awesome Link"
                                                                className="h-9 border-input/50 focus:border-primary/50 bg-background/50"
                                                                {...field}
                                                            />
                                                        </FormControl>
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
                                                        <FormLabel className="text-base font-semibold">Platform</FormLabel>
                                                        <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                            <FormControl>
                                                                <SelectTrigger className="w-full h-12 border-input/50 focus:border-primary/50 bg-background/50 data-[placeholder]:text-muted-foreground">
                                                                    <SelectValue placeholder="Select platform" />
                                                                </SelectTrigger>
                                                            </FormControl>
                                                            <SelectContent className="bg-background/95 backdrop-blur-xl border-border/50">
                                                                {platforms.map((platform) => {
                                                                    const Icon = platform.icon
                                                                    return (
                                                                        <SelectItem key={platform.value} value={platform.value} className="focus:bg-primary/10">
                                                                            <div className="flex items-center gap-2">
                                                                                <Icon className="h-4 w-4" />
                                                                                <span>{platform.label}</span>
                                                                            </div>
                                                                        </SelectItem>
                                                                    )
                                                                })}
                                                            </SelectContent>
                                                        </Select>
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
                                                        <Tag className="h-4 w-4" /> Tags
                                                    </FormLabel>
                                                    <FormControl>
                                                        <TagInput
                                                            value={field.value as string[] || []}
                                                            onChange={field.onChange}
                                                            placeholder="marketing, social, campaign"
                                                        />
                                                    </FormControl>
                                                    <FormMessage />
                                                </FormItem>
                                            )}
                                        />

                                        {/* Smart Routing Rules */}
                                        <FormField
                                            control={form.control}
                                            name="rules"
                                            render={({ field }) => (
                                                <SmartRoutingRules
                                                    rules={field.value as RedirectRuleDto[] || []}
                                                    onChange={field.onChange}
                                                    variant="plain"
                                                />
                                            )}
                                        />

                                        {error && (
                                            <div className="p-4 text-sm text-destructive bg-destructive/10 border border-destructive/20 rounded-lg">
                                                {error}
                                            </div>
                                        )}

                                        <div className="flex gap-4 pt-4 justify-end">
                                            <Button
                                                type="submit"
                                                disabled={loading}
                                                className="h-12 px-6 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold shadow-lg shadow-primary/20 hover:shadow-primary/30 transition-all duration-300"
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
                                    </CardContent>
                                </Card>
                            </form>
                        </Form>
                    </div>

                    {/* Right Panel: Info */}
                    <div className="lg:col-span-5 xl:col-span-4 space-y-6 lg:sticky lg:top-8">
                        <Card className="shadow-lg border-2 border-primary/10 bg-primary/5 backdrop-blur-sm">
                            <CardContent className="p-6">
                                <h3 className="font-semibold text-lg mb-4 flex items-center gap-2">
                                    <Sparkles className="h-5 w-5 text-primary" />
                                    Why Short Links?
                                </h3>
                                <div className="space-y-4 text-sm text-muted-foreground">
                                    <div className="flex items-start gap-3 p-3 rounded-lg bg-background/50 border border-border/50">
                                        <div className="w-2 h-2 bg-primary rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Share memorable links that are easy to remember</p>
                                    </div>
                                    <div className="flex items-start gap-3 p-3 rounded-lg bg-background/50 border border-border/50">
                                        <div className="w-2 h-2 bg-primary rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Track clicks and analyze performance</p>
                                    </div>
                                    <div className="flex items-start gap-3 p-3 rounded-lg bg-background/50 border border-border/50">
                                        <div className="w-2 h-2 bg-primary rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Custom branding with your domain</p>
                                    </div>
                                    <div className="flex items-start gap-3 p-3 rounded-lg bg-background/50 border border-border/50">
                                        <div className="w-2 h-2 bg-primary rounded-full mt-1.5 flex-shrink-0"></div>
                                        <p>Organize with tags and categories</p>
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

const CreateShortLink = () => {
    return (
        <Suspense fallback={
            <div className="min-h-screen bg-background flex items-center justify-center">
                <Loader2 className="h-6 w-6 animate-spin" />
            </div>
        }>
            <CreateShortLinkContent />
        </Suspense>
    )
}

export default CreateShortLink
