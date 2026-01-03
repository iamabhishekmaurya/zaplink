'use client';

import React, { useState, useEffect, useCallback, useRef } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { Slider } from '@/components/ui/slider';
import { Switch } from '@/components/ui/switch';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Input } from '@/components/ui/input';
import { ColorPicker } from '@/components/ui/color-picker';
import { Download, Link as LinkIcon, AlertCircle, Sparkles, Upload, X } from 'lucide-react';
import { QRConfigType, QRApi } from '@/lib/api/qr';
import { toast } from 'sonner';
import { cn } from '@/lib/utils';
// useDebounce removed, implemented locally
// If useDebounce doesn't exist, I'll implement a simple one inside or rely on useEffect delay.
// I will check for useDebounce existence first, but for now I'll implement a simple useDebounce inside to be safe or use setTimeout.

function useDebounceValue<T>(value: T, delay: number): T {
    const [debouncedValue, setDebouncedValue] = useState<T>(value);
    useEffect(() => {
        const handler = setTimeout(() => setDebouncedValue(value), delay);
        return () => clearTimeout(handler);
    }, [value, delay]);
    return debouncedValue;
}

interface QRGeneratorProps {
    defaultUrl?: string;
}

const DEFAULT_CONFIG: QRConfigType = {
    data: 'https://zaplink.io',
    size: 1024,
    margin: 0,
    errorCorrectionLevel: 'H',
    transparentBackground: false,
    backgroundColor: '#FFFFFF',
    body: {
        shape: 'SQUARE',
        color: '#000000',
        colorDark: '#000000',
        gradientLinear: false
    },
    eye: {
        shape: 'SQUARE',
        colorOuter: '#000000',
        colorInner: '#000000'
    },
    logo: {
        logoPath: '',
        sizeRatio: 0.2,
        padding: 2,
        backgroundColor: '#FFFFFF',
        backgroundEnabled: true,
        backgroundRounded: true,
        backgroundCornerRadius: 20,
        removeQuietZone: true,
        marginSize: 0,
    }
};

export function QRGenerator({ defaultUrl }: QRGeneratorProps) {
    const fileInputRef = useRef<HTMLInputElement>(null);
    const [config, setConfig] = useState<QRConfigType>({ ...DEFAULT_CONFIG, data: defaultUrl || DEFAULT_CONFIG.data });
    const [qrImage, setQrImage] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const debouncedConfig = useDebounceValue(config, 500);

    const generateQR = useCallback(async () => {
        setIsLoading(true);
        try {
            const url = await QRApi.generateStyledQR(debouncedConfig);
            setQrImage(url);
        } catch (error) {
            console.error('Failed to generate QR', error);
            // toast.error('Failed to update QR preview'); // Avoid spamming toasts on debounce
        } finally {
            setIsLoading(false);
        }
    }, [debouncedConfig]);

    useEffect(() => {
        generateQR();
    }, [generateQR]);

    const handleDownload = () => {
        if (qrImage) {
            const link = document.createElement('a');
            link.href = qrImage;
            link.download = `zaplink-qr-${Date.now()}.png`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            toast.success('QR Code downloaded!');
        }
    };

    const updateBody = (key: keyof typeof config.body, value: unknown) => {
        // @ts-ignore
        setConfig(prev => ({ ...prev, body: { ...prev.body, [key]: value } }));
    };

    const updateEye = (key: keyof typeof config.eye, value: unknown) => {
        // @ts-ignore
        setConfig(prev => ({ ...prev, eye: { ...prev.eye, [key]: value } }));
    };

    const updateLogo = (key: keyof NonNullable<typeof config.logo>, value: unknown) => {
        setConfig(prev => {
            const currentLogo = prev.logo || DEFAULT_CONFIG.logo!;
            // @ts-ignore
            return { ...prev, logo: { ...currentLogo, [key]: value } };
        });
    };

    const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (file.size > 2 * 1024 * 1024) {
            toast.error("Logo file too large (max 2MB)");
            return;
        }

        const reader = new FileReader();
        reader.onloadend = () => {
            const base64String = reader.result as string;
            updateLogo('logoPath', base64String);
            toast.success("Logo uploaded successfully");
        };
        reader.readAsDataURL(file);
    };

    const applyPreset = async (style: string) => {
        setIsLoading(true);
        try {
            // We could simply call the endpoint to get the image, OR update the local state to match the style.
            // Updating local state is harder without knowing the preset config values.
            // For now, let's just cheat and fetch the image directly to show it, but warned user that customizing will reset it.
            // Ideally, the backend would return the CONFIG for a preset, but it returns the IMAGE.
            // So, let's actually just define the presets locally for now to allow further editing.

            let newConfig = { ...config };
            switch (style) {
                case 'zap':
                    newConfig.body = { ...newConfig.body, shape: 'CIRCLE', color: '#000000' };
                    newConfig.eye = { ...newConfig.eye, shape: 'ROUNDED', colorOuter: '#000000', colorInner: '#000000' };
                    break;
                case 'modern':
                    newConfig.body = { ...newConfig.body, shape: 'LIQUID', color: '#2563EB', colorDark: '#9333EA', gradientLinear: true };
                    newConfig.eye = { ...newConfig.eye, shape: 'CIRCLE', colorOuter: '#2563EB', colorInner: '#9333EA' };
                    break;
                case 'playful':
                    newConfig.body = { ...newConfig.body, shape: 'DOT', color: '#EC4899' };
                    newConfig.eye = { ...newConfig.eye, shape: 'LEAF', colorOuter: '#EC4899', colorInner: '#F59E0B' };
                    break;
            }
            setConfig(newConfig);
            toast.success(`Applied ${style} style`);
        } catch (e) {
            toast.error("Failed to apply preset");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 h-auto">
            {/* Controls */}
            <div className="lg:col-span-2 space-y-6">
                <Card className="border shadow-sm bg-card/50 backdrop-blur-sm rounded-2xl">
                    <CardContent className="p-6 md:p-8">
                        <Tabs defaultValue="design" className="w-full">
                            <TabsList className="w-full grid grid-cols-4 mb-6 bg-muted/30 border-border/40 rounded-xl p-1">
                                <TabsTrigger value="content">Content</TabsTrigger>
                                <TabsTrigger value="design">Design</TabsTrigger>
                                <TabsTrigger value="colors">Colors</TabsTrigger>
                                <TabsTrigger value="logo">Logo</TabsTrigger>
                            </TabsList>

                            {/* CONTENT TAB */}
                            <TabsContent value="content" className="space-y-6">
                                <div className="space-y-3">
                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">QR Code Content</Label>
                                    <Input
                                        placeholder="Enter URL or text to encode"
                                        value={config.data}
                                        onChange={(e) => setConfig(prev => ({ ...prev, data: e.target.value }))}
                                        className="h-11 bg-muted/30 border-border/40 focus:border-primary transition-all font-medium rounded-xl"
                                    />
                                    <p className="text-xs text-muted-foreground px-1">Enter a URL, text, or any content you want to encode in the QR code.</p>
                                </div>
                            </TabsContent>

                            {/* DESIGN TAB */}
                            <TabsContent value="design" className="space-y-6">
                                {/* Presets */}
                                <div className="space-y-3">
                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Quick Styles</Label>
                                    <div className="grid grid-cols-3 gap-2">
                                        <Button variant="outline" size="sm" onClick={() => applyPreset('zap')} className="h-11 rounded-xl font-medium border-border/40 hover:border-primary/30 hover:bg-primary/10 transition-all text-muted-foreground">Zap</Button>
                                        <Button variant="outline" size="sm" onClick={() => applyPreset('modern')} className="h-11 rounded-xl font-medium border-border/40 hover:border-primary/30 hover:bg-primary/10 transition-all text-muted-foreground">Modern</Button>
                                        <Button variant="outline" size="sm" onClick={() => applyPreset('playful')} className="h-11 rounded-xl font-medium border-border/40 hover:border-primary/30 hover:bg-primary/10 transition-all text-muted-foreground">Playful</Button>
                                    </div>
                                </div>

                                <div className="space-y-4">
                                    <div className="space-y-3">
                                        <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Body Shape</Label>
                                        <Select
                                            value={config.body.shape}
                                            onValueChange={(val) => updateBody('shape', val)}
                                        >
                                            <SelectTrigger className="h-11 bg-muted/30 border-border/40 focus:border-primary transition-all font-medium rounded-xl">
                                                <SelectValue />
                                            </SelectTrigger>
                                            <SelectContent className="rounded-xl border-border/40 bg-background/95 backdrop-blur-sm">
                                                <SelectItem value="SQUARE" className="rounded-lg">Square</SelectItem>
                                                <SelectItem value="DOT" className="rounded-lg">Dot</SelectItem>
                                                <SelectItem value="CIRCLE" className="rounded-lg">Circle</SelectItem>
                                                <SelectItem value="ROUNDED" className="rounded-lg">Rounded</SelectItem>
                                                <SelectItem value="LIQUID" className="rounded-lg">Liquid</SelectItem>
                                            </SelectContent>
                                        </Select>
                                    </div>

                                    <div className="space-y-3">
                                        <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Eye Shape</Label>
                                        <Select
                                            value={config.eye.shape}
                                            onValueChange={(val) => updateEye('shape', val)}
                                        >
                                            <SelectTrigger className="h-11 bg-muted/30 border-border/40 focus:border-primary transition-all font-medium rounded-xl">
                                                <SelectValue />
                                            </SelectTrigger>
                                            <SelectContent className="rounded-xl border-border/40 bg-background/95 backdrop-blur-sm">
                                                <SelectItem value="SQUARE" className="rounded-lg">Square</SelectItem>
                                                <SelectItem value="CIRCLE" className="rounded-lg">Circle</SelectItem>
                                                <SelectItem value="ROUNDED" className="rounded-lg">Rounded</SelectItem>
                                                <SelectItem value="LEAF" className="rounded-lg">Leaf</SelectItem>
                                            </SelectContent>
                                        </Select>
                                    </div>

                                    <div className="space-y-3">
                                        <div className="flex justify-between">
                                            <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">QR Margin</Label>
                                            <span className="text-xs text-muted-foreground">{config.margin} modules</span>
                                        </div>
                                        <Slider
                                            value={[config.margin]}
                                            onValueChange={(vals) => setConfig(prev => ({ ...prev, margin: vals[0] }))}
                                            max={4}
                                            min={0}
                                            step={1}
                                            className="w-full"
                                        />
                                        <p className="text-xs text-muted-foreground px-1">
                                            Whitespace around the QR code (0 = no padding)
                                        </p>
                                    </div>
                                </div>
                            </TabsContent>

                            {/* COLORS TAB */}
                            <TabsContent value="colors" className="space-y-6">
                                <div className="flex items-center justify-between">
                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Transparent Background</Label>
                                    <Switch
                                        checked={config.transparentBackground}
                                        onCheckedChange={(checked) => setConfig(prev => ({ ...prev, transparentBackground: checked }))}
                                    />
                                </div>

                                {!config.transparentBackground && (
                                    <ColorPicker
                                        label="Background Color"
                                        color={config.backgroundColor}
                                        onChange={(c) => setConfig(prev => ({ ...prev, backgroundColor: c }))}
                                    />
                                )}

                                <div className="space-y-4">
                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Body Color</Label>
                                    <ColorPicker
                                        label="Primary Color"
                                        color={config.body.color}
                                        onChange={(c) => updateBody('color', c)}
                                    />

                                    <div className="flex items-center gap-2">
                                        <Switch
                                            id="gradient"
                                            checked={config.body.colorDark !== undefined && config.body.colorDark !== config.body.color}
                                            onCheckedChange={(checked) => updateBody('colorDark', checked ? '#000000' : undefined)}
                                        />
                                        <Label htmlFor="gradient" className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Enable Gradient</Label>
                                    </div>

                                    {config.body.colorDark !== undefined && config.body.colorDark !== config.body.color && (
                                        <>
                                            <ColorPicker
                                                label="Gradient End Color"
                                                color={config.body.colorDark || '#000000'}
                                                onChange={(c) => updateBody('colorDark', c)}
                                            />
                                            <div className="flex items-center justify-between">
                                                <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Linear Gradient</Label>
                                                <Switch
                                                    checked={config.body.gradientLinear}
                                                    onCheckedChange={(checked) => updateBody('gradientLinear', checked)}
                                                />
                                            </div>
                                        </>
                                    )}
                                </div>

                                <div className="space-y-4">
                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Eye Color</Label>
                                    <div className="grid grid-cols-2 gap-4">
                                        <ColorPicker
                                            label="Outer Frame"
                                            color={config.eye.colorOuter}
                                            onChange={(c) => updateEye('colorOuter', c)}
                                        />
                                        <ColorPicker
                                            label="Inner Dot"
                                            color={config.eye.colorInner}
                                            onChange={(c) => updateEye('colorInner', c)}
                                        />
                                    </div>
                                </div>
                            </TabsContent>

                            {/* LOGO TAB */}
                            <TabsContent value="logo" className="space-y-6">
                                <div className="flex items-center justify-between">
                                    <div className="space-y-0.5">
                                        <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Enable Logo</Label>
                                        <p className="text-xs text-muted-foreground ml-1">Add a logo to center of your QR code</p>
                                    </div>
                                    <Switch
                                        checked={config.logo !== null && config.logo !== undefined && !!config.logo.logoPath}
                                        onCheckedChange={(checked) => {
                                            if (checked) {
                                                setConfig(prev => ({ ...prev, logo: { ...DEFAULT_CONFIG.logo!, logoPath: '' } }));
                                            } else {
                                                setConfig(prev => ({ ...prev, logo: undefined }));
                                            }
                                        }}
                                    />
                                </div>

                                {config.logo !== null && config.logo !== undefined && (
                                    <div className="space-y-4">
                                        <div className="space-y-2">
                                            <div className="flex gap-2">
                                                <div className="relative flex-1">
                                                    <Input
                                                        placeholder="Logo URL or uploaded file"
                                                        value={config.logo?.logoPath?.startsWith('data:') ? 'Local file uploaded' : (config.logo?.logoPath || '')}
                                                        onChange={(e) => updateLogo('logoPath', e.target.value)}
                                                        className={cn(
                                                            "pr-10 h-11 bg-muted/30 border-border/40 focus:border-primary transition-all font-medium rounded-xl",
                                                            config.logo?.logoPath?.startsWith('data:') && "bg-primary/5 text-primary font-medium"
                                                        )}
                                                    />
                                                    {(config.logo?.logoPath) && (
                                                        <button
                                                            type="button"
                                                            onClick={() => updateLogo('logoPath', '')}
                                                            className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground p-0.5 rounded-full hover:bg-muted"
                                                            title="Clear logo"
                                                        >
                                                            <X className="h-3 w-3" />
                                                        </button>
                                                    )}
                                                </div>
                                                <input
                                                    type="file"
                                                    ref={fileInputRef}
                                                    className="hidden"
                                                    accept="image/*"
                                                    onChange={handleFileUpload}
                                                />
                                                <Button
                                                    variant="outline"
                                                    size="icon"
                                                    className="shrink-0 rounded-xl"
                                                    title="Upload Logo"
                                                    onClick={() => fileInputRef.current?.click()}
                                                >
                                                    <Upload className="h-4 w-4" />
                                                </Button>
                                            </div>
                                            <p className="text-xs text-muted-foreground">
                                                {config.logo?.logoPath?.startsWith('data:')
                                                    ? "Using uploaded file. You can also paste an image URL instead."
                                                    : "Upload a file or provide a simplified square PNG/JPG URL."}
                                            </p>
                                        </div>

                                        {/* Popular Logos Section */}
                                        <div className="space-y-3">
                                            <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Popular Logos</Label>
                                            <div className="grid grid-cols-4 gap-2">
                                                {[
                                                    { name: 'GitHub', url: 'https://cdn-icons-png.flaticon.com/512/3291/3291695.png' },
                                                    { name: 'Twitter', url: 'https://cdn-icons-png.flaticon.com/512/733/733579.png' },
                                                    { name: 'LinkedIn', url: 'https://cdn-icons-png.flaticon.com/512/174/174857.png' },
                                                    { name: 'Instagram', url: 'https://cdn-icons-png.flaticon.com/512/2111/2111463.png' },
                                                    { name: 'Facebook', url: 'https://cdn-icons-png.flaticon.com/512/733/733547.png' },
                                                    { name: 'YouTube', url: 'https://cdn-icons-png.flaticon.com/512/1384/1384060.png' },
                                                    { name: 'Google', url: 'https://cdn-icons-png.flaticon.com/512/281/281764.png' },
                                                    { name: 'Apple', url: 'https://cdn-icons-png.flaticon.com/512/179/179309.png' },
                                                ].map((brand) => (
                                                    <button
                                                        key={brand.name}
                                                        type="button"
                                                        onClick={() => updateLogo('logoPath', brand.url)}
                                                        className="group relative h-12 bg-muted/30 border-border/40 hover:border-primary/30 hover:bg-primary/10 transition-all rounded-xl p-2 flex items-center justify-center"
                                                        title={`Use ${brand.name} logo`}
                                                    >
                                                        <img
                                                            src={brand.url}
                                                            alt={brand.name}
                                                            className="w-6 h-6 object-contain"
                                                            onError={(e) => {
                                                                e.currentTarget.style.display = 'none';
                                                                e.currentTarget.nextElementSibling?.classList.remove('hidden');
                                                            }}
                                                        />
                                                        <div className="hidden text-xs text-muted-foreground group-hover:text-foreground">
                                                            {brand.name.slice(0, 3)}
                                                        </div>
                                                    </button>
                                                ))}
                                            </div>
                                            <p className="text-xs text-muted-foreground px-1">
                                                Quick access to popular brand and social media logos
                                            </p>
                                        </div>
                                    </div>
                                )}

                                {config.logo?.logoPath && (
                                    <>
                                        <div className="space-y-4">
                                            <div className="space-y-2">
                                                <div className="flex justify-between">
                                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Size Ratio</Label>
                                                    <span className="text-xs text-muted-foreground">{Math.round((config.logo?.sizeRatio || 0.2) * 100)}%</span>
                                                </div>
                                                <Slider
                                                    value={[(config.logo?.sizeRatio || 0.2) * 100]}
                                                    min={10}
                                                    max={30}
                                                    step={1}
                                                    onValueChange={(vals: number[]) => updateLogo('sizeRatio', vals[0] / 100)}
                                                />
                                            </div>
                                            <div className="space-y-2">
                                                <div className="flex justify-between">
                                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Padding</Label>
                                                    <span className="text-xs text-muted-foreground">{config.logo?.padding}px</span>
                                                </div>
                                                <Slider
                                                    value={[config.logo?.padding || 0]}
                                                    min={0}
                                                    max={20}
                                                    step={1}
                                                    onValueChange={(vals: number[]) => updateLogo('padding', vals[0])}
                                                />
                                                <p className="text-xs text-muted-foreground px-1">
                                                    White space around the logo (0 = no padding)
                                                </p>
                                            </div>
                                            <div className="space-y-2">
                                                <div className="flex justify-between">
                                                    <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">QR Module Margin</Label>
                                                    <span className="text-xs text-muted-foreground">{config.logo?.marginSize || 0} modules</span>
                                                </div>
                                                <Slider
                                                    value={[config.logo?.marginSize || 0]}
                                                    min={0}
                                                    max={5}
                                                    step={1}
                                                    onValueChange={(vals: number[]) => updateLogo('marginSize', vals[0])}
                                                />
                                                <p className="text-xs text-muted-foreground px-1">
                                                    QR modules removed around logo (0 = tight fit)
                                                </p>
                                            </div>
                                        </div>

                                        <div className="space-y-4 pt-4 border-t">
                                            <div className="flex items-center justify-between">
                                                <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Logo Background</Label>
                                                <Switch
                                                    checked={config.logo?.backgroundEnabled}
                                                    onCheckedChange={(checked) => updateLogo('backgroundEnabled', checked)}
                                                />
                                            </div>

                                            {config.logo?.backgroundEnabled && (
                                                <>
                                                    <ColorPicker
                                                        label="Background Color"
                                                        color={config.logo?.backgroundColor || '#FFFFFF'}
                                                        onChange={(c) => updateLogo('backgroundColor', c)}
                                                    />
                                                    <div className="flex items-center justify-between">
                                                        <Label className="text-sm font-semibold font-display uppercase tracking-wider text-muted-foreground ml-1">Rounded Corners</Label>
                                                        <Switch
                                                            checked={config.logo?.backgroundRounded}
                                                            onCheckedChange={(checked) => updateLogo('backgroundRounded', checked)}
                                                        />
                                                    </div>
                                                </>
                                            )}
                                        </div>
                                    </>
                                )}
                            </TabsContent>
                        </Tabs>
                    </CardContent>
                </Card>
            </div>

            {/* Preview Column */}
            <div className="flex flex-col items-center justify-start space-y-6 pt-4">
                <div className="relative group">
                    <div className="absolute -inset-1 bg-gradient-to-r from-primary to-purple-600 rounded-lg blur opacity-15 group-hover:opacity-25 transition duration-1000 group-hover:duration-200"></div>
                    <div className="relative bg-white dark:bg-neutral-900 rounded-xl p-4 shadow-xl border overflow-hidden">
                        {isLoading && (
                            <div className="absolute inset-0 bg-white/50 dark:bg-black/50 backdrop-blur-[1px] flex items-center justify-center z-10">
                                <Sparkles className="h-8 w-8 text-primary animate-spin" />
                            </div>
                        )}
                        {qrImage ? (
                            <img src={qrImage} alt="QR Code Preview" className="w-[280px] h-[280px] object-cover rounded-sm" />
                        ) : (
                            <div className="w-[280px] h-[280px] bg-muted flex items-center justify-center rounded-sm">
                                <AlertCircle className="h-10 w-10 text-muted-foreground/50" />
                            </div>
                        )}
                    </div>
                </div>

                <div className="text-center space-y-1">
                    <p className="text-sm font-medium text-muted-foreground break-all max-w-[280px] truncate">
                        {config.data}
                    </p>
                </div>

                <Button
                    className="gap-2 h-11 px-6 font-semibold shadow-lg shadow-primary/20 transition-all hover:shadow-primary/30"
                    onClick={handleDownload}
                    disabled={!qrImage || isLoading}
                >
                    <Download className="mr-2 h-4 w-4" /> Download QR
                </Button>
            </div>
        </div>
    );
}
