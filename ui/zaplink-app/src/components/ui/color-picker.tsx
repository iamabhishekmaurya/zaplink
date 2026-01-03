import React from 'react';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { cn } from '@/lib/utils';

interface ColorPickerProps {
    color: string;
    onChange: (color: string) => void;
    label?: string;
    className?: string;
}

const PRESET_COLORS = [
    '#000000', '#FFFFFF', '#6366F1', '#EC4899', '#14B8A6', '#F59E0B',
    '#3B82F6', '#EF4444', '#10B981', '#8B5CF6', '#F97316', '#64748B'
];

export function ColorPicker({ color, onChange, label, className }: ColorPickerProps) {
    return (
        <div className={cn("flex flex-col gap-2", className)}>
            {label && <Label className="text-xs font-semibold uppercase text-muted-foreground">{label}</Label>}
            <Popover>
                <PopoverTrigger asChild>
                    <Button
                        variant="outline"
                        className="w-full justify-start gap-2 px-3 font-mono text-xs"
                    >
                        <div
                            className="h-4 w-4 rounded-full border shadow-sm shrink-0"
                            style={{ backgroundColor: color }}
                        />
                        {color}
                    </Button>
                </PopoverTrigger>
                <PopoverContent className="w-64 p-3">
                    <div className="flex flex-col gap-3">
                        <div className="flex flex-wrap gap-1.5">
                            {PRESET_COLORS.map((preset) => (
                                <button
                                    key={preset}
                                    className={cn(
                                        "h-6 w-6 rounded-md border shadow-sm transition-transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-primary/50",
                                        color === preset && "ring-2 ring-primary border-primary"
                                    )}
                                    style={{ backgroundColor: preset }}
                                    onClick={() => onChange(preset)}
                                />
                            ))}
                        </div>
                        <div className="flex items-center gap-2 pt-2 border-t">
                            <div
                                className="h-8 w-8 rounded-md border shadow-sm shrink-0"
                                style={{ backgroundColor: color }}
                            />
                            <Input
                                value={color}
                                onChange={(e) => onChange(e.target.value)}
                                className="h-8 font-mono text-xs"
                                placeholder="#000000"
                            />
                        </div>
                    </div>
                </PopoverContent>
            </Popover>
        </div>
    );
}
