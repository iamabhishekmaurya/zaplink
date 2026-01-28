import React, { useState, KeyboardEvent, ChangeEvent } from 'react'
import { X, Tag as TagIcon } from 'lucide-react'
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'

interface TagInputProps {
    value: string[];
    onChange: (tags: string[]) => void;
    placeholder?: string;
    className?: string;
}

export const TagInput: React.FC<TagInputProps> = ({
    value = [],
    onChange,
    placeholder = "Add a tag...",
    className
}) => {
    const [inputValue, setInputValue] = useState('')

    const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter' || e.key === ',') {
            e.preventDefault()
            addTag()
        } else if (e.key === 'Backspace' && !inputValue && value.length > 0) {
            removeTag(value.length - 1)
        }
    }

    const addTag = () => {
        const trimmed = inputValue.trim().replace(/^,|,$/g, '')
        if (trimmed && !value.includes(trimmed)) {
            onChange([...value, trimmed])
            setInputValue('')
        }
    }

    const removeTag = (index: number) => {
        onChange(value.filter((_, i) => i !== index))
    }

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        setInputValue(e.target.value)
    }

    // Handle blur to add incomplete tag (optional, often good UX)
    const handleBlur = () => {
        if (inputValue.trim()) {
            addTag()
        }
    }

    return (
        <div className={cn("flex flex-col gap-2", className)}>
            <div className="flex flex-wrap gap-2 mb-2">
                {value.map((tag, index) => (
                    <Badge key={index} variant="secondary" className="px-3 py-1 text-sm bg-primary/10 hover:bg-primary/20 text-primary border-primary/20 flex items-center gap-1">
                        {tag}
                        <Button
                            type="button"
                            variant="ghost"
                            size="icon"
                            className="h-4 w-4 p-0 ml-1 hover:bg-transparent text-primary/60 hover:text-primary"
                            onClick={() => removeTag(index)}
                        >
                            <X className="h-3 w-3" />
                        </Button>
                    </Badge>
                ))}
            </div>
            <div className="relative">
                <TagIcon className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                    value={inputValue}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                    onBlur={handleBlur}
                    placeholder={value.length === 0 ? placeholder : "Add another tag..."}
                    className="pl-9 h-9 bg-background/50 border-input/50 focus:border-primary/50"
                />
            </div>
            <p className="text-xs text-muted-foreground">
                Press Enter or Comma to add a tag.
            </p>
        </div>
    )
}
