import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Label } from "@/components/ui/label";

type FontOption = {
    label: string;
    value: string;
    className?: string;
};

const fontOptions: FontOption[] = [
    { label: 'Inter (System)', value: 'Inter, sans-serif' },
    { label: 'Roboto', value: 'Roboto, sans-serif' },
    { label: 'Montserrat', value: 'Montserrat, sans-serif' },
    { label: 'Open Sans', value: '"Open Sans", sans-serif' },
    { label: 'Lato', value: 'Lato, sans-serif' },
    { label: 'Poppins', value: 'Poppins, sans-serif' },
    { label: 'Playfair Display', value: '"Playfair Display", serif' },
    { label: 'Merriweather', value: 'Merriweather, serif' },
    { label: 'Courier Prime', value: '"Courier Prime", monospace' },
];

interface FontSelectorProps {
    value: string;
    onChange: (value: string) => void;
}

export function FontSelector({ value, onChange }: FontSelectorProps) {
    return (
        <div className="space-y-2">
            <Label>Font Family</Label>
            <Select value={value} onValueChange={onChange}>
                <SelectTrigger className="w-full">
                    <SelectValue placeholder="Select a font" />
                </SelectTrigger>
                <SelectContent>
                    {fontOptions.map((font) => (
                        <SelectItem key={font.value} value={font.value} style={{ fontFamily: font.value }}>
                            {font.label}
                        </SelectItem>
                    ))}
                </SelectContent>
            </Select>
        </div>
    );
}
