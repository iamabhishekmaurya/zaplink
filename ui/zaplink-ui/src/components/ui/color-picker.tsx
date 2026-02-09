import React from 'react'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { cn } from '@/lib/utils'

interface PresetColor {
    name: string
    code: string
}

const DEFAULT_PRESETS: PresetColor[] = [
    { name: 'Black', code: '#000000' },
    { name: 'White', code: '#FFFFFF' },
    { name: 'Zap Blue', code: '#0066FF' },
    { name: 'Success', code: '#10B981' },
    { name: 'Warning', code: '#F59E0B' },
    { name: 'Purple', code: '#8B5CF6' },
    { name: 'Pink', code: '#EC4899' },
]

interface ColorPickerProps {
    value: string
    onChange: (value: string) => void
    label?: string
    presets?: PresetColor[]
    className?: string
    disabled?: boolean
}

export const ColorPicker: React.FC<ColorPickerProps> = ({
    value,
    onChange,
    label,
    presets = DEFAULT_PRESETS,
    className,
    disabled = false
}) => {
    // Ensure value is never undefined to prevent controlled/uncontrolled input issues
    const normalizedValue = value || '#000000';

    const handlePresetChange = (color: string) => {
        onChange(color.toUpperCase())
    }

    const handleHexChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        onChange(e.target.value)
    }

    const handleNativeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        onChange(e.target.value.toUpperCase())
    }

    return (
        <div className={cn("space-y-1.5", className)}>
            {label && <Label className="text-sm font-semibold text-muted-foreground">{label}</Label>}
            <div className="flex gap-2 items-center">
                {/* Visual Color Swatch + Native Picker */}
                <div className="relative group shrink-0">
                    <div
                        className="w-10 h-10 rounded-md border 2 ring-offset-background group-hover:ring-2 ring-primary/20 transition-all cursor-pointer shadow-sm overflow-hidden"
                        style={{ backgroundColor: normalizedValue }}
                    />
                    <input
                        type="color"
                        value={normalizedValue}
                        onChange={handleNativeChange}
                        disabled={disabled}
                        className="absolute inset-0 opacity-0 w-full h-full cursor-pointer disabled:cursor-not-allowed"
                    />
                </div>

                {/* Hex Input */}
                <div className="flex-1 min-w-[100px]">
                    <Input
                        value={normalizedValue}
                        onChange={handleHexChange}
                        disabled={disabled}
                        placeholder="#000000"
                        className="font-mono uppercase text-sm h-10"
                        maxLength={7}
                    />
                </div>

                {/* Presets Dropdown */}
                <div className="w-[110px] shrink-0">
                    <Select onValueChange={handlePresetChange} disabled={disabled}>
                        <SelectTrigger className="h-10 px-3">
                            <SelectValue placeholder="Presets" />
                        </SelectTrigger>
                        <SelectContent>
                            {presets.map(c => (
                                <SelectItem key={c.name} value={c.code}>
                                    <div className="flex items-center gap-2">
                                        <div className="w-3 h-3 rounded-full border" style={{ backgroundColor: c.code }} />
                                        <span className="text-xs">{c.name}</span>
                                    </div>
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>
            </div>
        </div>
    )
}
