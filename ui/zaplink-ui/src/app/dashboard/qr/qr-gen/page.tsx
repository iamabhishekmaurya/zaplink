'use client'

import React, { useState, useEffect, useRef, Suspense } from 'react'
import { useForm } from 'react-hook-form'
import { useRouter, useSearchParams } from 'next/navigation'
import { zodResolver } from '@hookform/resolvers/zod'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { RefreshCw, Settings2, Share2, ArrowLeft, Plus } from 'lucide-react'
import { QRService } from '@/lib/api/QRServerApi'
import { QRConfigType } from '@/lib/types/apiRequestType'
import { toast } from 'sonner'
import { Defaults, formSchema, FormValues } from './constants'
import { DesignTab } from './components/tabs/design-tab'
import { ColorsTab } from './components/tabs/colors-tab'
import { LogoTab } from './components/tabs/logo-tab'
import { AdvancedTab } from './components/tabs/advanced-tab'
import { QrPreview } from './components/qr-preview'
import { DynamicQrService } from '@/lib/api/dynamicQr'

// --- Main Component ---

const excludeData = (values: FormValues | null) => {
    if (!values) return null
    const { data, ...rest } = values
    return rest
}

const mapToApiConfig = (values: FormValues): QRConfigType => {
    const logoConfig = values.logo ? {
        logoPath: values.logo.logoPath || undefined,
        sizeRatio: values.logo.sizeRatio ?? 0.2,
        padding: values.logo.padding ?? 0,
        backgroundColor: values.logo.backgroundColor ?? '#FFFFFF',
        backgroundEnabled: values.logo.backgroundEnabled ?? true,
        backgroundRounded: values.logo.backgroundRounded ?? true,
        backgroundCornerRadius: values.logo.backgroundCornerRadius ?? 20,
        removeQuietZone: values.logo.removeQuietZone ?? true,
        marginSize: values.logo.marginSize ?? 0,
    } : undefined

    return {
        data: values.data,
        size: 1024,
        margin: values.margin,
        errorCorrectionLevel: 'H',
        transparentBackground: values.transparentBackground,
        backgroundColor: values.backgroundColor,
        body: {
            shape: values.bodyShape,
            color: values.bodyColor,
            colorDark: values.bodyColorDark,
            gradientLinear: values.gradientLinear,
        },
        eye: {
            shape: values.eyeShape,
            colorOuter: values.eyeColorOuter,
            colorInner: values.eyeColorInner,
        },
        logo: logoConfig
    }
}

const QrGeneratorContent = () => {
    const router = useRouter()
    const searchParams = useSearchParams()
    const urlParam = searchParams.get('url')

    const [previewUrl, setPreviewUrl] = useState<string | null>(null)
    const [isGenerating, setIsGenerating] = useState(false)
    const [lastGeneratedValues, setLastGeneratedValues] = useState<FormValues | null>(null)
    const isGeneratingRef = useRef(false)
    const [mounted, setMounted] = useState(false)

    useEffect(() => {
        setMounted(true)
    }, [])

    const form = useForm<FormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            ...Defaults,
            data: urlParam || Defaults.data
        },
        mode: 'onChange',
    })

    // Update form if urlParam changes (e.g. navigation)
    useEffect(() => {
        if (urlParam) {
            form.setValue('data', urlParam)
        }
    }, [urlParam, form])

    // Watch all values to trigger auto-generation on style changes
    const watchedValues = form.watch()

    useEffect(() => {
        const handler = setTimeout(() => {
            const allValues = form.getValues()

            // Only generate if form is valid AND it's a new config
            // We compare actual values to prevent infinite loops from object references
            const isStyleChanged = JSON.stringify(excludeData(allValues)) !== JSON.stringify(excludeData(lastGeneratedValues))

            if (form.formState.isValid && isStyleChanged && !isGeneratingRef.current) {
                isGeneratingRef.current = true
                generateQR(allValues).finally(() => {
                    isGeneratingRef.current = false
                })
            }
        }, 800)

        return () => clearTimeout(handler)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [JSON.stringify(excludeData(watchedValues)), isGenerating])

    const generateQR = async (values: FormValues) => {
        try {
            setIsGenerating(true)

            // Map form values to API request type
            // Send logo config if logo object exists in values, even if path is empty (for bg settings)
            const logoConfig = values.logo ? {
                logoPath: values.logo.logoPath || undefined,
                sizeRatio: values.logo.sizeRatio ?? 0.2,
                padding: values.logo.padding ?? 0,
                backgroundColor: values.logo.backgroundColor ?? '#FFFFFF',
                backgroundEnabled: values.logo.backgroundEnabled ?? true,
                backgroundRounded: values.logo.backgroundRounded ?? true,
                backgroundCornerRadius: values.logo.backgroundCornerRadius ?? 20,
                removeQuietZone: values.logo.removeQuietZone ?? true,
                marginSize: values.logo.marginSize ?? 0,
            } : undefined

            console.log("Generating QR with config", { isGenerating, logoConfig })

            const config: QRConfigType = {
                data: values.data,
                size: 1024, // High res for download
                margin: values.margin,
                errorCorrectionLevel: 'H',
                transparentBackground: values.transparentBackground,
                backgroundColor: values.backgroundColor,
                body: {
                    shape: values.bodyShape,
                    color: values.bodyColor,
                    colorDark: values.bodyColorDark,
                    gradientLinear: values.gradientLinear,
                },
                eye: {
                    shape: values.eyeShape,
                    colorOuter: values.eyeColorOuter,
                    colorInner: values.eyeColorInner,
                },
                logo: logoConfig
            }

            const url = await QRService.generateStyledQR(config)
            setPreviewUrl(url)
            // Deep clone to avoid reference issues
            setLastGeneratedValues(JSON.parse(JSON.stringify(values)))
        } catch (error) {
            console.error('Failed to generate QR', error)
            toast.error('Failed to generate QR code')
        } finally {
            setIsGenerating(false)
        }
    }

    const handleManualGenerate = () => {
        const allValues = form.getValues()
        if (form.formState.isValid) {
            generateQR(allValues)
        }
    }

    const handleSave = async (name: string) => {
        const values = form.getValues()
        if (!values.data) {
            toast.error("Please enter a destination URL")
            return
        }

        try {
            const config = mapToApiConfig(values)
            // Calculate expiration date
            let expirationDate: string | undefined
            if (values.expirationDays && values.expirationDays > 0) {
                const date = new Date()
                date.setDate(date.getDate() + values.expirationDays)
                expirationDate = date.toISOString()
            }

            // Parse allowed domains
            let allowedDomains: string[] | undefined
            if ((values.domainRestriction === 'allowed' || values.domainRestriction === 'blocked') && values.allowedDomains) {
                allowedDomains = values.allowedDomains.split(/[,\n]+/).map(d => d.trim()).filter(Boolean)
            }

            await DynamicQrService.createDynamicQr({
                qrName: name,
                destinationUrl: values.data,
                qrConfig: config,
                expirationDate: expirationDate,
                password: values.passwordProtection ? values.password : undefined,
                scanLimit: values.scanLimit === 0 ? undefined : values.scanLimit, // 0 usually means unlimited in UI but backend treats >0 as limit.
                allowedDomains: allowedDomains,
                trackAnalytics: values.trackAnalytics
            })
            toast.success('QR Code saved successfully and is now active!')
        } catch (error) {
            console.error('Failed to save QR', error)
            toast.error('Failed to save QR code. Please try again.')
        }
    }

    const handleDownload = (format: 'png' | 'svg' = 'png') => {
        if (!previewUrl) return
        const link = document.createElement('a')
        link.href = previewUrl
        link.download = `zaipmeqr-${Date.now()}.${format}`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        toast.success('QR Code downloaded')
    }

    const applyPreset = (style: string) => {
        const currentData = form.getValues('data')
        let newValues = { ...Defaults, data: currentData }

        switch (style) {
            case 'zap':
                newValues.bodyShape = 'CIRCLE'
                newValues.bodyColor = '#000000'
                newValues.eyeShape = 'ROUNDED'
                newValues.eyeColorOuter = '#000000'
                newValues.eyeColorInner = '#000000'
                break
            case 'modern':
                newValues.bodyShape = 'LIQUID'
                newValues.bodyColor = '#2563EB'
                newValues.bodyColorDark = '#9333EA'
                newValues.gradientLinear = true
                newValues.eyeShape = 'CIRCLE'
                newValues.eyeColorOuter = '#2563EB'
                newValues.eyeColorInner = '#9333EA'
                break
            case 'playful':
                newValues.bodyShape = 'DOT'
                newValues.bodyColor = '#EC4899'
                newValues.eyeShape = 'LEAF'
                newValues.eyeColorOuter = '#EC4899'
                newValues.eyeColorInner = '#F59E0B'
                break
        }

        form.reset(newValues)
        // Trigger generation immediately after preset
        setTimeout(() => generateQR(newValues), 100)
    }

    if (!mounted) {
        return <div className="min-h-screen flex items-center justify-center">Loading...</div>
    }

    return (
        <div className="min-h-screen bg-background p-4 lg:p-8">
            <div className="max-w-7xl mx-auto space-y-8">
                {/* Header */}
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                    <div>
                        <h1 className="text-3xl font-bold tracking-tight">QR Generator</h1>
                        <p className="text-muted-foreground mt-1">Create stunning, custom QR codes for your brand.</p>
                    </div>
                    <div className="flex gap-2">
                        <Button variant="outline" onClick={() => router.push('/dashboard/qr')} className="flex items-center gap-2">
                            <ArrowLeft className="h-4 w-4" /> Back to QR Codes
                        </Button>
                        <Button variant="outline" onClick={() => form.reset(Defaults)}>
                            <RefreshCw className="mr-2 h-4 w-4" /> Reset
                        </Button>
                    </div>
                </div>

                <div className="grid lg:grid-cols-12 gap-8 items-start">
                    {/* Left Panel: Controls */}
                    <div className="lg:col-span-7 xl:col-span-8 space-y-6">
                        <Form {...form}>
                            <form className="space-y-6">
                                {/* Content Section */}
                                <Card>
                                    <CardHeader>
                                        <CardTitle className="flex items-center gap-2">
                                            <Share2 className="h-5 w-5 text-primary" /> Content
                                        </CardTitle>
                                        <CardDescription>What should this QR code link to?</CardDescription>
                                    </CardHeader>
                                    <CardContent>
                                        <FormField
                                            control={form.control}
                                            name="data"
                                            render={({ field }) => (
                                                <FormItem>
                                                    <FormLabel>URL or Text</FormLabel>
                                                    <FormControl>
                                                        <div className="flex gap-2">
                                                            <Input placeholder="https://zaipme.io/my-link" {...field} className="h-12" />
                                                            <Button type="button" onClick={() => generateQR(form.getValues())} className="h-12 px-6" disabled={isGenerating}>
                                                                Generate
                                                            </Button>
                                                        </div>
                                                    </FormControl>
                                                    <FormMessage />
                                                </FormItem>
                                            )}
                                        />
                                    </CardContent>
                                </Card>

                                {/* Customization Tabs */}
                                <Card>
                                    <CardHeader>
                                        <CardTitle className="flex items-center gap-2">
                                            <Settings2 className="h-5 w-5 text-primary" /> Customization
                                        </CardTitle>
                                    </CardHeader>
                                    <CardContent>
                                        <Tabs defaultValue="design" className="w-full">
                                            <TabsList className="grid w-full grid-cols-4 mb-6">
                                                <TabsTrigger value="design">Design</TabsTrigger>
                                                <TabsTrigger value="colors">Colors</TabsTrigger>
                                                <TabsTrigger value="logo">Logo</TabsTrigger>
                                                <TabsTrigger value="advanced">Advanced</TabsTrigger>
                                            </TabsList>

                                            <TabsContent value="design">
                                                <DesignTab onApplyPreset={applyPreset} />
                                            </TabsContent>

                                            <TabsContent value="colors">
                                                <ColorsTab />
                                            </TabsContent>

                                            <TabsContent value="logo">
                                                <LogoTab />
                                            </TabsContent>

                                            <TabsContent value="advanced">
                                                <AdvancedTab onSave={handleSave} />
                                            </TabsContent>
                                        </Tabs>
                                    </CardContent>
                                </Card>
                            </form>
                        </Form>
                    </div>

                    {/* Right Panel: Preview */}
                    <div className="lg:col-span-5 xl:col-span-4">
                        <QrPreview
                            previewUrl={previewUrl}
                            isGenerating={isGenerating}
                            onDownload={handleDownload}
                            onSave={handleSave}
                        />
                    </div>
                </div>
            </div>
        </div>
    )
}

const QrGenerator = () => {
    return (
        <Suspense fallback={<div className="min-h-screen flex items-center justify-center">Loading...</div>}>
            <QrGeneratorContent />
        </Suspense>
    )
}

export default QrGenerator