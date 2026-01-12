import React from 'react'
import { useFormContext } from 'react-hook-form'
import { FormField, FormItem, FormLabel, FormControl } from '@/components/ui/form'
import { Slider } from '@/components/ui/slider'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { StyleSelector } from '../style-selector'
import { BODY_SHAPES, EYE_SHAPES, FormValues } from '../../constants'
import { cn } from '@/lib/utils'

interface DesignTabProps {
    onApplyPreset: (style: string) => void
}

export const DesignTab: React.FC<DesignTabProps> = ({ onApplyPreset }) => {
    const form = useFormContext<FormValues>()

    return (
        <div className="space-y-6">
            <div className="space-y-3">
                <Label>Quick Styles</Label>
                <div className="grid grid-cols-3 gap-2">
                    <Button variant="outline" type="button" onClick={() => onApplyPreset('zap')} className="h-10">Zap</Button>
                    <Button variant="outline" type="button" onClick={() => onApplyPreset('modern')} className="h-10">Modern</Button>
                    <Button variant="outline" type="button" onClick={() => onApplyPreset('playful')} className="h-10">Playful</Button>
                </div>
            </div>

            <div className="space-y-6">
                <FormField
                    control={form.control}
                    name="bodyShape"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Pattern Style</FormLabel>
                            <FormControl>
                                <StyleSelector
                                    value={field.value}
                                    onChange={field.onChange}
                                    options={BODY_SHAPES}
                                />
                            </FormControl>
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="eyeShape"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Corner Style</FormLabel>
                            <FormControl>
                                <div className="grid grid-cols-4 gap-2 sm:flex sm:flex-wrap">
                                    {EYE_SHAPES.map((option) => (
                                        <button
                                            key={option.value}
                                            type="button"
                                            onClick={() => field.onChange(option.value)}
                                            className={cn(
                                                "w-full sm:w-24 aspect-square flex flex-col items-center justify-center gap-2 rounded-lg border-2 p-2 transition-all hover:bg-muted/50",
                                                field.value === option.value
                                                    ? "border-primary bg-primary/5 text-primary"
                                                    : "border-transparent bg-muted/20 text-muted-foreground hover:border-border"
                                            )}
                                            title={option.label}
                                        >
                                            <svg viewBox="0 0 24 24" className="w-8 h-8">
                                                {option.icon}
                                            </svg>
                                            <span className="text-xs font-medium">
                                                {option.label}
                                            </span>
                                        </button>
                                    ))}
                                </div>
                            </FormControl>
                        </FormItem>
                    )}
                />
            </div>

            <FormField
                control={form.control}
                name="margin"
                render={({ field }) => (
                    <FormItem>
                        <div className="flex justify-between">
                            <FormLabel>Margin Size</FormLabel>
                            <span className="text-xs text-muted-foreground">{field.value}</span>
                        </div>
                        <FormControl>
                            <Slider
                                min={0} max={10} step={1}
                                value={[field.value]}
                                onValueChange={(vals) => field.onChange(vals[0])}
                            />
                        </FormControl>
                    </FormItem>
                )}
            />
        </div>
    )
}
