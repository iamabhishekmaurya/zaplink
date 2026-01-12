import React from 'react'
import { cn } from '@/lib/utils'

interface StyleSelectorProps {
    value: string
    onChange: (value: string) => void
    options: { value: string; label: string; icon: React.ReactNode }[]
}

export const StyleSelector: React.FC<StyleSelectorProps> = ({
    value,
    onChange,
    options
}) => {
    return (
        <div className="grid grid-cols-4 sm:grid-cols-7 gap-2">
            {options.map((option) => (
                <button
                    key={option.value}
                    type="button"
                    onClick={() => onChange(option.value)}
                    className={cn(
                        "aspect-square flex flex-col items-center justify-center gap-1 rounded-lg border-2 p-2 transition-all hover:bg-muted/50",
                        value === option.value
                            ? "border-primary bg-primary/5 text-primary"
                            : "border-transparent bg-muted/20 text-muted-foreground hover:border-border"
                    )}
                    title={option.label}
                >
                    <svg viewBox="0 0 24 24" className="w-6 h-6 sm:w-8 sm:h-8">
                        {option.icon}
                    </svg>
                    <span className="text-[10px] font-medium truncate w-full text-center hidden sm:block">
                        {option.label}
                    </span>
                </button>
            ))}
        </div>
    )
}
