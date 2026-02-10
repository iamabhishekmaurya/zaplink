import { ThemeConfig } from "@/ui/design-system/theme-utils";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";

interface ButtonStylePickerProps {
    layout: ThemeConfig['layout'];
    onChange: (updates: Partial<ThemeConfig['layout']>) => void;
}

export function ButtonStylePicker({ layout, onChange }: ButtonStylePickerProps) {
    return (
        <div className="space-y-6">
            <div className="space-y-3">
                <Label>Button Style</Label>
                <RadioGroup
                    value={layout.buttonStyle}
                    onValueChange={(val) => onChange({ buttonStyle: val as ThemeConfig['layout']['buttonStyle'] })}
                    className="grid grid-cols-4 gap-2"
                >
                    {['filled', 'outline', 'ghost', 'glass'].map((style) => (
                        <div key={style}>
                            <RadioGroupItem value={style} id={`style-${style}`} className="sr-only" />
                            <Label
                                htmlFor={`style-${style}`}
                                className={`flex h-10 cursor-pointer items-center justify-center rounded-md border-2 border-muted bg-popover px-3 py-2 hover:bg-accent text-sm font-medium transition-all ${layout.buttonStyle === style ? 'border-primary ring-2 ring-primary/20' : ''
                                    }`}
                            >
                                {style}
                            </Label>
                        </div>
                    ))}
                </RadioGroup>
            </div>

            <div className="space-y-3">
                <Label>Button Shape</Label>
                <RadioGroup
                    value={layout.buttonShape}
                    onValueChange={(val) => onChange({ buttonShape: val as ThemeConfig['layout']['buttonShape'] })}
                    className="grid grid-cols-4 gap-2"
                >
                    {['square', 'hard', 'rounded', 'pill'].map((shape) => (
                        <div key={shape}>
                            <RadioGroupItem value={shape} id={`shape-${shape}`} className="sr-only" />
                            <Label
                                htmlFor={`shape-${shape}`}
                                className={`flex h-10 w-full cursor-pointer items-center justify-center border-2 border-muted bg-popover hover:bg-accent transition-all ${layout.buttonShape === shape ? 'border-primary ring-2 ring-primary/20' : ''
                                    }`}
                                style={{
                                    borderRadius:
                                        shape === 'pill' ? '9999px' : shape === 'rounded' ? '8px' : shape === 'hard' ? '2px' : '0px',
                                }}
                            >
                                <div className="h-4 w-4 bg-current opacity-20" />
                            </Label>
                        </div>
                    ))}
                </RadioGroup>
            </div>

            <div className="space-y-3">
                <Label>Shadow</Label>
                <RadioGroup
                    value={layout.buttonShadow}
                    onValueChange={(val) => onChange({ buttonShadow: val as ThemeConfig['layout']['buttonShadow'] })}
                    className="grid grid-cols-5 gap-2"
                >
                    {['none', 'sm', 'md', 'lg', 'glow'].map((shadow) => (
                        <div key={shadow}>
                            <RadioGroupItem value={shadow} id={`shadow-${shadow}`} className="sr-only" />
                            <Label
                                htmlFor={`shadow-${shadow}`}
                                className={`flex h-10 cursor-pointer items-center justify-center rounded-md border-2 border-muted bg-popover hover:bg-accent text-xs font-medium transition-all ${layout.buttonShadow === shadow ? 'border-primary ring-2 ring-primary/20' : ''
                                    }`}
                            >
                                {shadow}
                            </Label>
                        </div>
                    ))}
                </RadioGroup>
            </div>
        </div>
    );
}
