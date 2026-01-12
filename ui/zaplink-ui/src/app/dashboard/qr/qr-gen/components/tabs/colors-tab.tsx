import React from 'react'
import { useFormContext } from 'react-hook-form'
import { FormField, FormItem, FormControl, FormLabel } from '@/components/ui/form'
import { Label } from '@/components/ui/label'
import { Switch } from '@/components/ui/switch'
import { ColorPicker } from '@/components/ui/color-picker' // Using absolute import as requested
import { PRESET_COLORS, FormValues } from '../../constants'
import { Input } from '@/components/ui/input'

export const ColorsTab = () => {
    const form = useFormContext<FormValues>()

    return (
        <div className="space-y-6">
            <div className="space-y-4">
                <div className="flex items-center justify-between">
                    <Label className="text-sm font-semibold">Quick Set All</Label>
                    <div className="flex gap-2">
                        {PRESET_COLORS.map(c => (
                            <button
                                key={c.code} type="button"
                                className="w-6 h-6 rounded-full border shadow-sm ring-offset-2 hover:ring-2 transition-all"
                                style={{ backgroundColor: c.code }}
                                onClick={() => {
                                    form.setValue('bodyColor', c.code)
                                    form.setValue('eyeColorOuter', c.code)
                                    form.setValue('eyeColorInner', c.code)
                                    form.setValue('bodyColorDark', c.code)
                                }}
                                title={c.name}
                            />
                        ))}
                    </div>
                </div>

                <div className="grid sm:grid-cols-2 gap-4">
                    <FormField
                        control={form.control} name="bodyColor"
                        render={({ field }) => (
                            <FormItem>
                                <FormControl>
                                    <ColorPicker
                                        label="Primary Color"
                                        value={field.value}
                                        onChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control} name="backgroundColor"
                        render={({ field }) => (
                            <FormItem>
                                <FormControl>
                                    <ColorPicker
                                        label="Background Color"
                                        value={field.value}
                                        onChange={field.onChange}
                                        disabled={form.watch('transparentBackground')}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                </div>

                <div className="grid sm:grid-cols-2 gap-6 pt-2">
                    <FormField
                        control={form.control} name="gradientLinear"
                        render={({ field }) => (
                            <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
                                <div className="space-y-0.5"><FormLabel>Linear Gradient</FormLabel></div>
                                <FormControl><Switch checked={field.value} onCheckedChange={field.onChange} /></FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control} name="transparentBackground"
                        render={({ field }) => (
                            <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
                                <div className="space-y-0.5"><FormLabel>Transparent Background</FormLabel></div>
                                <FormControl><Switch checked={field.value} onCheckedChange={field.onChange} /></FormControl>
                            </FormItem>
                        )}
                    />
                </div>

                {form.watch('gradientLinear') && (
                    <FormField
                        control={form.control} name="bodyColorDark"
                        render={({ field }) => (
                            <FormItem>
                                <FormControl>
                                    <ColorPicker
                                        label="Gradient End Color"
                                        value={field.value || '#000000'}
                                        onChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                )}
            </div>

            <div className="space-y-4 pt-4 border-t">
                <Label className="text-sm font-semibold">Eye Colors</Label>
                <div className="grid sm:grid-cols-2 gap-4">
                    <FormField
                        control={form.control} name="eyeColorOuter"
                        render={({ field }) => (
                            <FormItem>
                                <FormControl>
                                    <ColorPicker
                                        label="Eye Outer Color"
                                        value={field.value}
                                        onChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control} name="eyeColorInner"
                        render={({ field }) => (
                            <FormItem>
                                <FormControl>
                                    <ColorPicker
                                        label="Eye Inner Color"
                                        value={field.value}
                                        onChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                </div>
            </div>
        </div>
    )
}
