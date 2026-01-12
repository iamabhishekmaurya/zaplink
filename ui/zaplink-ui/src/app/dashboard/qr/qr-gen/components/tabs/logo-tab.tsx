import React, { useRef } from 'react'
import { useFormContext } from 'react-hook-form'
import { FormField, FormItem, FormControl, FormLabel, FormMessage } from '@/components/ui/form'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Slider } from '@/components/ui/slider'
import { Switch } from '@/components/ui/switch'
import { LOGO_PRESETS, FormValues } from '../../constants'
import { Upload, X } from 'lucide-react'
import { cn } from '@/lib/utils'
import { toast } from 'sonner' // Assuming toast is available via sonner

export const LogoTab = () => {
    const form = useFormContext<FormValues>()
    const fileInputRef = useRef<HTMLInputElement>(null)

    const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (!file) return

        if (file.size > 2 * 1024 * 1024) {
            toast.error("Logo file too large (max 2MB)")
            return
        }

        const reader = new FileReader()
        reader.onloadend = () => {
            const base64String = reader.result as string
            form.setValue('logo.logoPath', base64String, { shouldDirty: true, shouldTouch: true })
        }
        reader.readAsDataURL(file)
    }

    return (
        <div className="space-y-6">
            <Label>Logo Settings</Label>
            <div className="space-y-4">
                <FormField
                    control={form.control}
                    name="logo.logoPath"
                    render={({ field }) => (
                        <FormItem>
                            <div className="flex gap-2 items-center">
                                <div className="relative flex-1">
                                    <FormControl>
                                        <Input placeholder="Logo URL or upload" {...field} value={field.value || ''}
                                            className={cn("pr-8", field.value?.startsWith('data:') && "text-primary")}
                                        />
                                    </FormControl>
                                    {field.value && (
                                        <button type="button" onClick={() => field.onChange('')} className="absolute right-2 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground">
                                            <X className="h-4 w-4" />
                                        </button>
                                    )}
                                </div>
                                <input type="file" ref={fileInputRef} className="hidden" accept="image/*" onChange={handleFileUpload} />
                                <Button type="button" variant="outline" size="icon" onClick={() => fileInputRef.current?.click()}>
                                    <Upload className="h-4 w-4" />
                                </Button>
                            </div>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                {/* Popular Logos */}
                <div className="grid grid-cols-5 gap-2">
                    {LOGO_PRESETS.map((brand) => (
                        <Button
                            key={brand.name}
                            type="button"
                            variant="outline"
                            onClick={() => form.setValue('logo.logoPath', brand.url)}
                            className="h-10 bg-muted/30 border hover:border-primary/50 p-0"
                            title={brand.name}
                        >
                            <img src={brand.url} alt={brand.name} className="w-5 h-5 object-contain" />
                        </Button>
                    ))}
                </div>

                {/* Sliders */}
                <>
                    <div className="grid sm:grid-cols-2 gap-4">
                        <FormField
                            control={form.control} name="logo.sizeRatio"
                            render={({ field }) => (
                                <FormItem>
                                    <div className="flex justify-between"><FormLabel>Size</FormLabel><span className="text-xs">{Math.round((field.value ?? 0.2) * 100)}%</span></div>
                                    <FormControl>
                                        <Slider min={10} max={50} step={1} value={[(field.value ?? 0.2) * 100]} onValueChange={(v) => field.onChange(v[0] / 100)} />
                                    </FormControl>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control} name="logo.padding"
                            render={({ field }) => (
                                <FormItem>
                                    <div className="flex justify-between"><FormLabel>Padding</FormLabel><span className="text-xs">{field.value ?? 0}px</span></div>
                                    <FormControl>
                                        <Slider min={0} max={20} step={1} value={[field.value ?? 0]} onValueChange={(v) => field.onChange(v[0])} />
                                    </FormControl>
                                </FormItem>
                            )}
                        />
                    </div>
                    <FormField
                        control={form.control} name="logo.backgroundEnabled"
                        render={({ field }) => (
                            <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
                                <div className="space-y-0.5"><FormLabel>Logo Background</FormLabel></div>
                                <FormControl><Switch checked={field.value} onCheckedChange={field.onChange} /></FormControl>
                            </FormItem>
                        )}
                    />
                </>
            </div>
        </div>
    )
}
